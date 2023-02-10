package com.example.smsdemo.controllers.student;

import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.Course;
import com.example.smsdemo.models.LogSession;
import com.example.smsdemo.models.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class CourseListController implements Initializable {
    @FXML
    private TableView<Course> CourseList;

    @FXML
    private TableColumn<Course, Integer> courseNumber;

    @FXML
    private TableColumn<Course, String> courseName;

    @FXML
    private TableColumn courseTeacher;

    @FXML
    private TableColumn<Course, String> courseStartDate;

    @FXML
    private TableColumn<Course, String> courseEndDate;

    @FXML
    private TableColumn actionField;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addAllCourses();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        courseNumber.setCellValueFactory(new PropertyValueFactory<Course, Integer>("courseCounter"));
        courseName.setCellValueFactory(new PropertyValueFactory<Course, String>("courseName"));
        courseStartDate.setCellValueFactory(new PropertyValueFactory<Course, String>("startDate"));
        courseEndDate.setCellValueFactory(new PropertyValueFactory<Course, String>("endDate"));

        CourseList.setItems(courseList);

        Callback<TableColumn<Course, String>, TableCell<Course, String>> teacherCellFactory = (param) ->{
            final TableCell<Course, String> cell = new TableCell<Course, String>(){
              @Override
              public void updateItem(String item, boolean empty){
                  super.updateItem(item, empty);
                  if (empty){
                      setGraphic(null);
                      setText(null);
                  }else {
                      String teacherName="";
                      Course course = getTableRow().getItem();
                      if (course!=null){
                          teacherName = course.getCourseTeacher().getName() + " " + course.getCourseTeacher().getSurname();
                      }
                      setGraphic(null);
                      setText(teacherName);
                  }
              }
            };
            return cell;
        };


        Callback<TableColumn<Course, String>, TableCell<Course, String>> actionCellFactory = (param) ->{
            final TableCell<Course, String> cell = new TableCell<Course, String>(){
                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty){
                        setGraphic(null);
                        setText(null);
                    }else {

                        final Button actionButton = new Button();
                        Course course = getTableRow().getItem();

                        if (course!=null){

                            try {
                                if (checkEnrolled(course.getCourseID())) {
                                    actionButton.setText("Quit Course");
                                    actionButton.setOnAction(event -> {
                                        System.out.println("Delete");
                                        try {
                                            quitCourse(course);
                                            SceneChanger.changeScene(event, "admin/courseList.fxml", "Courses");
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                    });
                                } else {
                                    actionButton.setText("Enroll Course");
                                    actionButton.setOnAction(event -> {
                                        System.out.println("Enroll");

                                        try {
                                            enrollCourse(course);
                                            SceneChanger.changeScene(event, "student/courseList.fxml", "Courses");
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        setGraphic(actionButton);
                        setText(null);

                    }

                }

            };
            return cell;
        };

        courseTeacher.setCellFactory(teacherCellFactory);
        actionField.setCellFactory(actionCellFactory);

    }

    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "student/main-view.fxml", "Main Page");

    }

    @FXML
    protected void onClassroomButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "student/lms.fxml", "Classroom");
    }

    private void addAllCourses() throws Exception{
        DbUtil.setDatabase("sms_courses");
        ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM course_list;");
        Course course;
        Teacher teacher;
        int cnt = 0;

        try {
            while (rest.next()){
                course = new Course();
                cnt++;
                course.setCourseCounter(cnt);
                course.setCourseID(String.valueOf(rest.getInt("ID")));
                System.out.println(rest.getInt("ID"));
                course.setCourseName(rest.getString("course_name"));
                course.setCourseLogo(rest.getString("course_logo"));
                teacher = new Teacher();
                teacher.setUserID(rest.getString("course_teacher").substring(1));
                course.setCourseTeacher(teacher);
                course.setStartDate(rest.getString("start_date"));
                course.setEndDate(rest.getString("end_date"));
                courseList.add(course);
            }

            rest.close();
            DbUtil.dbDisconnect();

            DbUtil.setDatabase("sms");

            for (Course eachCourse:courseList){
                teacher = eachCourse.getCourseTeacher();
                rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM teacher WHERE ID = %d;", Integer.valueOf(teacher.getUserID())));
                if (rest.next()){
                    teacher.setName(rest.getString("f_name"));
                    teacher.setSurname(rest.getString("s_name"));
                    teacher.setMiddleName(rest.getString("m_name"));
                    teacher.setUserName(rest.getString("email"));
                }
                rest.close();
                DbUtil.dbDisconnect();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private boolean checkEnrolled(String courseID) throws Exception{
        DbUtil.setDatabase("sms_courses");
        boolean result = false;
        String query = String.format("SELECT * from s%s WHERE course_id = \"C%s\";", LogSession.getUser().getUserID(), courseID);
        try {
            ResultSet rest = DbUtil.dbExecuteQuery(query);
            if (rest.next()) result = true;
            rest.close();
            DbUtil.dbDisconnect();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    private void quitCourse(Course course) throws Exception{
        DbUtil.setDatabase("sms_courses");
        try {
            DbUtil.dbExecuteUpdate(String.format("DELETE FROM s%s WHERE course_id = \"C%s\";", LogSession.getUser().getUserID(), course.getCourseID()));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void enrollCourse(Course course) throws Exception{
        DbUtil.setDatabase("sms_courses");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String enrolledDate;

        try {
            enrolledDate = formatter.format(LocalDate.now());
            DbUtil.dbExecuteUpdate(String.format("INSERT INTO s%s (course_name, course_id, course_logo,start_date, enrolled_date, end_date, status, teacher) VALUES (\"%s\", \"C%s\", \"%s\",\"%s\", \"%s\",\"%s\",%d, \"T%s\")", LogSession.getUser().getUserID(), course.getCourseName(), course.getCourseID(), course.getCourseLogo(),course.getStartDate(), enrolledDate, course.getEndDate(), 1, course.getCourseTeacher().getUserID()));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
