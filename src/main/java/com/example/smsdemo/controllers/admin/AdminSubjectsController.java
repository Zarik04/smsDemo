package com.example.smsdemo.controllers.admin;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.Course;
import com.example.smsdemo.models.Student;
import com.example.smsdemo.models.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminSubjectsController implements Initializable {
    @FXML
    private TableView<Course> courseList;

    @FXML
    private TableColumn<Course, Integer> courseCounter;

    @FXML
    private TableColumn<Course, String> courseID;

    @FXML
    private TableColumn<Course, String> courseName;

    @FXML
    private TableColumn<Course, String> courseLogo;

    @FXML
    private TableColumn courseTeacher;

    @FXML
    private TableColumn<Course, String> startDate;

    @FXML
    private TableColumn<Course, String> endDate;

    @FXML
    private TableColumn actionField;

    private ObservableList<Course> courses = FXCollections.observableArrayList();


    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField logoField;

    @FXML
    private TextField teacherField;

    @FXML
    private TextField startDateField;

    @FXML
    private TextField endDateField;

    @FXML
    private Label message;
    @FXML
    private Button actionButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addAllCourses();
            populateTable();
            actionButton.setText("Add");
            actionButton.setOnAction(eventBtn -> {
                System.out.println("Add");
                try {
                    addNewCourse();

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "admin/main-admin.fxml", "Main Page");
    }

    @FXML
    protected void onStudentsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }

    @FXML
    protected void onGroupsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "admin/groups-view.fxml", "Groups");
    }

    @FXML
    protected void onResetButton(ActionEvent event) {
        if (message.getText().startsWith("Successfully")){
            try {
                SceneChanger.changeScene(event, "admin/admin-subjects.fxml", "Subjects");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else {
            idField.setText("");
            nameField.setText("");
            logoField.setText("");
            teacherField.setText("");
            startDateField.setText("");
            endDateField.setText("");
            message.setText("");
            actionButton.setText("Add");
            actionButton.setOnAction(eventBtn -> {
                System.out.println("Add");
                try {
                    addNewCourse();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });
        }



    }



    private void addAllCourses() throws Exception{
        DbUtil.setDatabase("sms_courses");
        ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM course_list;");
        Course course;
        Teacher courseTeacher;
        int counter = 0;
        while (rest.next()){
            course = new Course();
            counter++;
            course.setCourseCounter(counter);
            course.setCourseID(String.valueOf(rest.getInt("ID")));
            course.setCourseName(rest.getString("course_name"));
            course.setCourseLogo(rest.getString("course_logo"));
            courseTeacher = new Teacher();
            courseTeacher.setUserID(rest.getString("course_teacher").substring(1));
            course.setCourseTeacher(courseTeacher);
            course.setStartDate(rest.getString("start_date"));
            course.setEndDate(rest.getString("end_date"));
            courses.add(course);
        }
        rest.close();
        DbUtil.dbDisconnect();

        DbUtil.setDatabase("sms");
        for (Course eachCourse:courses){
            courseTeacher = eachCourse.getCourseTeacher();
            rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM teacher WHERE ID = %d;", Integer.valueOf(courseTeacher.getUserID())));
            if (rest.next()){
                courseTeacher.setName(rest.getString("f_name"));
                courseTeacher.setSurname(rest.getString("s_name"));
                courseTeacher.setMiddleName(rest.getString("m_name"));
                courseTeacher.setUserName(rest.getString("email"));
            }
            rest.close();
            DbUtil.dbDisconnect();
        }
    }

    private void populateTable() throws Exception{
        courseCounter.setCellValueFactory(new PropertyValueFactory<Course, Integer>("courseCounter"));
        courseID.setCellValueFactory(new PropertyValueFactory<Course, String>("courseID"));
        courseName.setCellValueFactory(new PropertyValueFactory<Course, String>("courseName"));
        courseLogo.setCellValueFactory(new PropertyValueFactory<Course, String>("courseLogo"));
        startDate.setCellValueFactory(new PropertyValueFactory<Course, String>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<Course, String>("endDate"));

        courseList.setItems(courses);

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
                        final Button deleteButton = new Button();
                        Course course = getTableRow().getItem();
                        if (course!=null){
                            deleteButton.setText("Delete");
                            deleteButton.setOnAction(event -> {
                                try {
                                    ArrayList<String> allStudentIDs = new ArrayList<>();
                                    DbUtil.setDatabase("sms");
                                    ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM student;");
                                    while (rest.next()){
                                        Student newStudent = new Student();
                                        newStudent.setUserID(String.valueOf(rest.getInt("ID")));
                                        allStudentIDs.add(newStudent.getUserID());
                                    }
                                    rest.close();
                                    DbUtil.dbDisconnect();

                                    DbUtil.setDatabase("sms_courses");
                                    for (String ID: allStudentIDs){
                                        DbUtil.dbExecuteUpdate(String.format("DELETE FROM s%s WHERE course_id = \"C%s\";", ID, course.getCourseID()));
                                    }

                                    DbUtil.dbExecuteUpdate(String.format("DELETE FROM course_list WHERE ID = %d;", Integer.valueOf(course.getCourseID())));

                                    DbUtil.setDatabase("sms_courses_discussions");
                                    DbUtil.dbExecuteUpdate(String.format("DROP TABLE c%s;", course.getCourseID()));
                                    SceneChanger.changeScene(event, "admin/admin-subjects.fxml", "Subjects");
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            });
                        }
                        setGraphic(deleteButton);
                        setText(null);
                    }
                }
            };
            return  cell;
        };

        courseTeacher.setCellFactory(teacherCellFactory);
        actionField.setCellFactory(actionCellFactory);

        courseList.setOnMouseClicked(mouseEvent -> {
            Course course = courseList.getSelectionModel().getSelectedItem();
            if (course != null){

                idField.setText(course.getCourseID());
                nameField.setText(course.getCourseName());
                logoField.setText(course.getCourseLogo());
                teacherField.setText(course.getCourseTeacher().getName()+" "+course.getCourseTeacher().getSurname());
                startDateField.setText(course.getStartDate());
                endDateField.setText(course.getEndDate());

                actionButton.setText("Edit");
                actionButton.setOnAction(event -> {
                    System.out.println("Edit");
                    if (nameField.getText().isEmpty()||logoField.getText().isEmpty()||teacherField.getText().isEmpty()||startDateField.getText().isEmpty()||endDateField.getText().isEmpty()){
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("Do not leave important fields empty !");
                    } else if (!teacherField.getText().contains(" ")){
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("Please, write full name of course teacher !");
                    } else if(courseExists(nameField.getText())&&!course.getCourseName().equalsIgnoreCase(nameField.getText())){
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("This course already exists !");
                    } else if (checkTeacherName(teacherField.getText()).isEmpty()){
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("There is no such teacher !");
                    } else {
                        Teacher courseTeacher = new Teacher();
                        courseTeacher.setUserID(checkTeacherName(teacherField.getText()));
                        try {
                            DbUtil.setDatabase("sms_courses");
                            DbUtil.dbExecuteUpdate(String.format("UPDATE course_list SET " +
                                    "course_name = \"%s\", course_logo = \"%s\", course_teacher = \"T%s\", start_date = \"%s\", " +
                                    "end_date = \"%s\" WHERE ID = %d;", nameField.getText(), logoField.getText(),courseTeacher.getUserID(),
                                    startDateField.getText(), endDateField.getText(), Integer.valueOf(course.getCourseID())));
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        message.setTextFill(Paint.valueOf("green"));
                        message.setText("Successfully Updated !");
                    }

                });
            }
        });


    }

    private void addNewCourse() throws Exception{
        nameField.setText(nameField.getText().trim());
        teacherField.setText(teacherField.getText().trim());
        startDateField.setText(startDateField.getText().trim());
        endDateField.setText(endDateField.getText().trim());
        if (nameField.getText().isEmpty()||logoField.getText().isEmpty()||teacherField.getText().isEmpty()||startDateField.getText().isEmpty()||endDateField.getText().isEmpty()){
            message.setTextFill(Paint.valueOf("red"));
            message.setText("Please, do not leave important fields empty !");
        }else {


            if (courseExists(nameField.getText())){

                message.setTextFill(Paint.valueOf("red"));
                message.setText("This course already exists !");

            }else {

                String teacherID = checkTeacherName(teacherField.getText());


                if (teacherID.isEmpty()){
                    message.setTextFill(Paint.valueOf("red"));
                    message.setText("There is no such teacher.");
                }else {
                    Teacher teacher = new Teacher();
                    teacher.setUserID(teacherID);


                    Course course = new Course();
                    course.setCourseName(nameField.getText());
                    course.setCourseLogo(logoField.getText());
                    course.setCourseTeacher(teacher);
                    course.setStartDate(startDateField.getText());
                    course.setEndDate(endDateField.getText());

                    try {
                        DbUtil.setDatabase("sms_courses");
                        DbUtil.dbExecuteUpdate(String.format("INSERT INTO course_list " +
                                        "(course_name, course_logo, course_teacher, start_date, end_date) VALUES " +
                                        "(\"%s\", \"%s\",\"T%s\", \"%s\", \"%s\");",
                                course.getCourseName(), course.getCourseLogo(),course.getCourseTeacher().getUserID(),
                                course.getStartDate(), course.getEndDate()));

                        ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM course_list WHERE course_name = \"%s\";", course.getCourseName()));
                        if (rest.next()) course.setCourseID(String.valueOf(rest.getInt("ID")));
                        rest.close();
                        DbUtil.dbDisconnect();

                        DbUtil.setDatabase("sms_courses_discussions");
                        DbUtil.dbExecuteUpdate(String.format("CREATE TABLE c%s (" +
                                "ID INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
                                "date VARCHAR(80), " +
                                "sender VARCHAR(10), " +
                                "message VARCHAR(1000) " +
                                ");", course.getCourseID()));

                        DbUtil.setDatabase("sms_courses_resources");
                        DbUtil.dbExecuteUpdate(String.format("CREATE TABLE c%s (" +
                                "ID INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
                                "topic VARCHAR(250), " +
                                "description VARCHAR(450), " +
                                "author VARCHAR(10), " +
                                "date VARCHAR(100) " +
                                ");", course.getCourseID()));

                        File sourceFolder = new File(String.format(
                                "course resources/%s", course.getCourseID()));
                        sourceFolder.mkdir();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    message.setTextFill(Paint.valueOf("green"));
                    message.setText("Successfully Added !");


                }


            }





        }

    }

    private String checkTeacherName(String fullName){
        String[] teacherDetails = teacherField.getText().split(" ");
        String teacherID="";
        DbUtil.setDatabase("sms");
        ResultSet rest;

        try {
            rest = DbUtil.dbExecuteQuery(String.format("SELECT ID FROM teacher WHERE f_name = \"%s\" AND s_name = \"%s\";", teacherDetails[0], teacherDetails[1]));

            if (rest.next()){
                teacherID = String.valueOf(rest.getInt("ID"));
            }else {
                rest.close();
                DbUtil.dbDisconnect();
                rest = DbUtil.dbExecuteQuery(String.format("SELECT ID FROM teacher WHERE f_name = \"%s\" AND s_name = \"%s\";", teacherDetails[1], teacherDetails[0]));
                if (rest.next()){
                    teacherID = String.valueOf(rest.getInt("ID"));
                }
            }

            rest.close();
            DbUtil.dbDisconnect();

        }catch (Exception ex){
            ex.printStackTrace();
        }


        return teacherID;
    }

    private boolean courseExists(String courseName){
        ResultSet rest ;
        boolean exists=false;

        try {
            DbUtil.setDatabase("sms_courses");
            rest = DbUtil.dbExecuteQuery("SELECT * FROM course_list;");

            while (rest.next()){
                if (rest.getString("course_name").equalsIgnoreCase(courseName)){
                    exists = true;
                    break;
                }
            }

            rest.close();
            DbUtil.dbDisconnect();
        }catch (Exception ex){
            ex.printStackTrace();
        }


        return  exists;
    }


}
