package com.example.smsdemo.controllers.teacher;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.controllers.utils.Validator;
import com.example.smsdemo.models.Course;
import com.example.smsdemo.models.LogSession;
import com.example.smsdemo.models.Teacher;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TeacherSubjectsController implements Initializable {

    @FXML
    private FlowPane subjectsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{

            VBox subjectModel = (VBox) FXMLLoader.load(HelloApplication.class.getResource("course/subject.fxml"));
            FontAwesomeIconView subjectLogo = (FontAwesomeIconView) subjectModel.getChildren().get(0);
            Label subjectName = (Label) subjectModel.getChildren().get(1);

            getAllSubjects();
            for (Course subject:((Teacher)LogSession.getUser()).getCourses()){
                subjectLogo.setGlyphName(subject.getCourseLogo());
                subjectName.setText(subject.getCourseName());

                subjectModel.setOnMouseClicked(mouseEvent->{
                    System.out.println(subject.getCourseName()+" clicked!\n");
                    SubjectViewController.setSubject(subject);
                    try {
                        SceneChanger.changeScene(mouseEvent, "teacher/subject-view.fxml", subject.getCourseName());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                });

                subjectsPane.getChildren().add((Node) subjectModel);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "teacher/main-teacher.fxml", "Main Page");
    }

    private void getAllSubjects() throws Exception{
        Course subject;
        ArrayList<Course> allSubjects = new ArrayList<>();
        DbUtil.setDatabase("sms_courses");

        ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM course_list WHERE " +
                        "course_teacher = \"T%s\";", LogSession.getUser().getUserID()));

        while (rest.next()){
            subject = new Course();
            subject.setCourseID(String.valueOf(rest.getInt("ID")));
            subject.setCourseLogo(rest.getString("course_logo"));
            subject.setCourseName(rest.getString("course_name"));
            subject.setCourseTeacher((Teacher) LogSession.getUser());
            subject.setStartDate(rest.getString("start_date"));
            subject.setEndDate(rest.getString("end_date"));

            allSubjects.add(subject);
        }

        rest.close();
        DbUtil.dbDisconnect();

        ((Teacher) LogSession.getUser()).setCourses(allSubjects);
    }

}
