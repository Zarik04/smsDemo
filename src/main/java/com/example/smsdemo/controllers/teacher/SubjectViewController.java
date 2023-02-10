package com.example.smsdemo.controllers.teacher;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.*;
import com.example.smsdemo.models.courseSources.Discussion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubjectViewController implements Initializable {

    private static Course subject;

    @FXML
    private ChoiceBox<String> groupList;

    @FXML
    private TableView<Student> enrolledStudents;

    @FXML
    private TableColumn<Student, Integer> counter;

    @FXML
    private TableColumn<Student, String> studentID;

    @FXML
    private TableColumn<Student, String> studentGroup;

    @FXML
    private TableColumn<Student, String> studentName;

    @FXML
    private TableColumn<Student, String> studentSurname;

    @FXML
    private TableColumn<Student, String> studentMiddleName;

    @FXML
    private TableColumn<Student, String> studentGender;

    @FXML
    private TableColumn<Student, String> studentBirthDate;

    @FXML
    private TableColumn<Student, String> studentPhone;

    @FXML
    private TableColumn<Student, String> studentEmail;

    @FXML
    private VBox discussions;
    @FXML
    private TextArea discussionText;

    private ObservableList<Student> studentObservableList = FXCollections.observableArrayList();
    private ObservableList<String> groupIdList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateStudentsTable("SELECT ALL");
            setGroupList();
            addAllDiscussions();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "teacher/main-teacher.fxml", "Main Page");
    }

    @FXML
    protected void onSubjectsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "teacher/teacher-subjects.fxml", "Subjects");
    }

    @FXML
    protected void onChooseGroupButton(ActionEvent event) throws Exception{
        String selectedID = groupList.getSelectionModel().getSelectedItem();
        if (selectedID!=null){
            populateStudentsTable(selectedID);
        }
    }


    private void populateStudentsTable(String groupID) throws Exception{
        ArrayList<Integer> studentsID = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

        enrolledStudents.getItems().clear();

        DbUtil.setDatabase("sms");
        ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM student;");

        while (rest.next()){
            studentsID.add(rest.getInt("ID"));
        }
        rest.close();
        DbUtil.dbDisconnect();

        DbUtil.setDatabase("sms_courses");
        for (int ID:studentsID){
            Student student = new Student();
            student.setUserID(String.valueOf(ID));

            rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM s%s WHERE " +
                            "course_id = \"C%s\" AND course_name = \"%s\";", student.getUserID(),
                    subject.getCourseID(), subject.getCourseName()));

            if (rest.next()) students.add(student);
            rest.close();
            DbUtil.dbDisconnect();
        }

        DbUtil.setDatabase("sms");
        for (Student student:students){
            rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM student " +
                    "WHERE ID = %d;", Integer.valueOf(student.getUserID())));
            if (!rest.next()) continue;

            student.setGroupID(rest.getString("group_id"));
            student.setName(rest.getString("f_name"));
            student.setSurname(rest.getString("s_name"));
            student.setMiddleName(rest.getString("m_name"));
            if (rest.getString("gender").equalsIgnoreCase("m")) student.setGender("Male");
            else student.setGender("Female");
            student.setBirthDate(rest.getString("birth_date"));
            student.setPhone("+"+rest.getString("phone"));
            student.setUserName(rest.getString("email"));
            if (rest.getInt("log_status")==1) student.setOnline(true);
            else student.setOnline(false);
            student.setLogStatus();
            if (rest.getInt("reg_status")==1) student.setRegistered(true);
            else student.setRegistered(false);
            student.setRegStatus();


        }



        if (groupID.equalsIgnoreCase("select all")){
            int cnt = 0;

            for (Student student:students){
                cnt++;
                student.setCounter(cnt);
                studentObservableList.add(student);
            }

        }else {
            int cnt = 0;
            for (Student student:students){
                if (student.getGroupID().equals(groupID)){
                    cnt++;
                    student.setCounter(cnt);
                    studentObservableList.add(student);
                }
            }
        }


        counter.setCellValueFactory(new PropertyValueFactory<Student, Integer>("counter"));
        studentID.setCellValueFactory(new PropertyValueFactory<Student, String>("userID"));
        studentGroup.setCellValueFactory(new PropertyValueFactory<Student, String>("groupID"));
        studentName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        studentSurname.setCellValueFactory(new PropertyValueFactory<Student, String>("surname"));
        studentMiddleName.setCellValueFactory(new PropertyValueFactory<Student, String>("middleName"));
        studentGender.setCellValueFactory(new PropertyValueFactory<Student, String>("gender"));
        studentBirthDate.setCellValueFactory(new PropertyValueFactory<Student, String>("birthDate"));
        studentPhone.setCellValueFactory(new PropertyValueFactory<Student, String>("phone"));
        studentEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("userName"));


        enrolledStudents.setItems(studentObservableList);


    }

    private void setGroupList() throws Exception{
        groupIdList.add("SELECT ALL");
        for (Student student:studentObservableList){
            groupIdList.add(student.getGroupID());
        }
        groupList.setItems(groupIdList);
    }

    public static Course getSubject() {
        return subject;
    }

    public static void setSubject(Course subject) {
        SubjectViewController.subject = subject;
    }



    public void addAllDiscussions(){
        ArrayList<Discussion> discussionArrayList = new ArrayList<>();
        discussions.getChildren().removeAll(discussions.getChildren());
        DbUtil.setDatabase("sms_courses_discussions");

        try {

            ResultSet allDiscussions = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s;", subject.getCourseID()));

            while (allDiscussions.next()){

                Discussion newDiscussion = new Discussion();
                newDiscussion.setID(String.valueOf(allDiscussions.getInt("ID")));
                newDiscussion.setDate(allDiscussions.getString("date"));

                String senderID = allDiscussions.getString("sender");
                User sender;
                if (senderID.startsWith("T")) sender = new Teacher();
                else sender = new Student();
                sender.setUserID(senderID.substring(1));

                newDiscussion.setSender(sender);
                newDiscussion.setMessage(allDiscussions.getString("message"));

                discussionArrayList.add(newDiscussion);

            }

            allDiscussions.close();
            DbUtil.dbDisconnect();

            for (Discussion discussion: discussionArrayList){
                DbUtil.setDatabase("sms");
                User sender = discussion.getSender();

                System.out.println(sender.getUserType());
                System.out.println(sender.getUserID());

                ResultSet rest;

                if (sender.getUserType().equalsIgnoreCase("student"))
                    rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM student WHERE ID = %d;", Integer.valueOf(sender.getUserID())));
                else rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM teacher WHERE ID = %d;", Integer.valueOf(sender.getUserID())));

                rest.next();

                sender.setName(rest.getString("f_name"));
                sender.setSurname(rest.getString("s_name"));

                if (rest.getString("gender").equalsIgnoreCase("m")) sender.setGender("Male");
                else sender.setGender("Female");

                rest.close();
                DbUtil.dbDisconnect();


                HBox discussionBox = (HBox) FXMLLoader.load(HelloApplication.class.getResource("course/discussion.fxml"));
                for (Node child: discussionBox.getChildren()){
                    String childId = child.getId();
                    if (childId.equalsIgnoreCase("senderLogo")){
                        if (sender.getGender().equalsIgnoreCase("Male"))
                            ((ImageView) child).setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaM.png")));
                        else ((ImageView) child).setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaW.png")));
                    }else if (childId.equalsIgnoreCase("senderInfo")){
                        VBox senderInfoBox = (VBox) child;
                        ((Label) senderInfoBox.getChildren().get(0)).setText(sender.getName()+" "+sender.getSurname());
                        ((Label) senderInfoBox.getChildren().get(1)).setText(sender.getUserType());
                    } else if (childId.equalsIgnoreCase("messageInfo")) {
                        VBox messageInfoBox = (VBox) child;
                        ((Label) messageInfoBox.getChildren().get(0)).setText(discussion.getMessage());
                        ((Label) messageInfoBox.getChildren().get(1)).setText(discussion.getDate());
                    }
                }

                discussions.getChildren().add((Node) discussionBox);




            }


        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    @FXML
    protected void onSendBtn(){
        if (!discussionText.getText().isEmpty()){
            try {
                sendDiscussionMessage(discussionText.getText(), LogSession.getUser());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }


    private void sendDiscussionMessage(String messageText, User user) throws Exception{
        DbUtil.setDatabase("sms_courses_discussions");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = formatter.format(LocalDate.now());
        DbUtil.dbExecuteUpdate(String.format("INSERT INTO c%s (date, sender, message) " +
                        "VALUES (\"%s\", \"%s\", \"%s\");", subject.getCourseID(), date,
                String.valueOf(user.getUserType().toUpperCase().charAt(0))+user.getUserID(), messageText));
        addAllDiscussions();
        discussionText.setText("");
    }


}
