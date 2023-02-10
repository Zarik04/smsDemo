package com.example.smsdemo.controllers.student;

import com.example.smsdemo.HelloApplication;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.Course;
import com.example.smsdemo.models.LogSession;
import com.example.smsdemo.models.Student;
import com.example.smsdemo.models.Teacher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ClassroomController implements Initializable {
    @FXML
    protected FlowPane subjectsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getSubjectsListUpdated();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Student student = (Student) LogSession.getUser();
        ArrayList<Course> enrolledCourses = student.getCourses();

        if (enrolledCourses!=null){
            try {

                for (int i=0; i<enrolledCourses.size(); i++){
                    Course course = enrolledCourses.get(i);
                    Teacher teacher = course.getCourseTeacher();
                    VBox subject = (VBox) FXMLLoader.load(HelloApplication.class.getResource("course/subject.fxml"));

                    FontAwesomeIconView courseLogo = (FontAwesomeIconView) subject.getChildren().get(0);
                    courseLogo.setGlyphName(course.getCourseLogo());

                    Label subjectName = (Label) subject.getChildren().get(1);
                    subjectName.setText(course.getCourseName());

                    subject.setOnMouseClicked(mouseEvent -> {

                        System.out.println(String.format("\n%s clicked !\n", subjectName.getText()));
                        SubjectController.setCourse(course);
                        try {
                            SceneChanger.changeScene(mouseEvent, "student/courseView.fxml", course.getCourseName());

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    });

                    subjectsPane.getChildren().add((Node) subject);
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }

    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception {
        SceneChanger.changeScene(event,  "student/main-view.fxml", "Main Page");
    }


    @FXML
    protected void onEnrollButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "student/courseList.fxml", "Courses");
    }

    private void getSubjectsListUpdated() throws Exception{
        ArrayList<Course> enrolledCourses = new ArrayList<>();
        Course course;
        Student student = (Student) LogSession.getUser();
        DbUtil.setDatabase("sms_courses");
        try {
            ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM s%s;", student.getUserID()));
            while (rest.next()){
                course = new Course();
                course.setCourseName(rest.getString("course_name"));
                course.setCourseID(rest.getString("course_id").substring(1));
                course.setCourseLogo(rest.getString("course_logo"));
                course.setStartDate(rest.getString("start_date"));
                course.setEndDate(rest.getString("end_date"));
                Teacher courseTeacher = new Teacher();
                courseTeacher.setUserID(rest.getString("teacher").substring(1));
                course.setCourseTeacher(courseTeacher);

                enrolledCourses.add(course);
            }
            rest.close();
            DbUtil.dbDisconnect();
            student.setCourses(enrolledCourses);

            DbUtil.setDatabase("sms");
            for (Course enrolledCourse: enrolledCourses){
                Teacher courseTeacher = enrolledCourse.getCourseTeacher();
                ResultSet rst = DbUtil.dbExecuteQuery(String.format("SELECT * FROM teacher WHERE ID = %d;", Integer.valueOf(courseTeacher.getUserID())));
                if (rst.next()){
                    courseTeacher.setName(rst.getString("f_name"));
                    courseTeacher.setSurname(rst.getString("s_name"));
                    courseTeacher.setMiddleName(rst.getString("m_name"));
                    courseTeacher.setUserName(rst.getString("email"));
                }

                rst.close();
                DbUtil.dbDisconnect();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
