package com.example.smsdemo.controllers.admin;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.Edit;
import com.example.smsdemo.models.Group;
import com.example.smsdemo.models.Student;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentsController implements Initializable {

    private static Group selectedGroup;

    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, Integer> number;
    @FXML
    private TableColumn<Student, String> ID;
    @FXML
    private TableColumn<Student, String> fName;
    @FXML
    private TableColumn<Student, String> sName;
    @FXML
    private TableColumn<Student, String> mName;
    @FXML
    private TableColumn<Student, String> gender;
    @FXML
    private TableColumn<Student, String> birthDate;
    @FXML
    private TableColumn<Student, String> phone;
    @FXML
    private TableColumn<Student, String> email;
    @FXML
    private TableColumn<Student, String> logStatus;
    @FXML
    private TableColumn<Student, String> regStatus;

    @FXML
    private Button editBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button joinBtn;

    @FXML
    private Button kickBtn;

    @FXML
    private HBox groupButtons;



    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (selectedGroup!=null) addBtn.setVisible(false);
        else groupButtons.setVisible(false);
        try {
            addStudents();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        number.setCellValueFactory(new PropertyValueFactory<Student, Integer>("counter"));
        ID.setCellValueFactory(new PropertyValueFactory<Student, String>("userID"));
        fName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        sName.setCellValueFactory(new PropertyValueFactory<Student, String>("surname"));
        mName.setCellValueFactory(new PropertyValueFactory<Student, String>("middleName"));
        gender.setCellValueFactory(new PropertyValueFactory<Student, String>("gender"));
        birthDate.setCellValueFactory(new PropertyValueFactory<Student, String>("birthDate"));
        phone.setCellValueFactory(new PropertyValueFactory<Student, String>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<Student, String>("userName"));
        logStatus.setCellValueFactory(new PropertyValueFactory<Student, String>("logStatus"));
        regStatus.setCellValueFactory(new PropertyValueFactory<Student, String>("regStatus"));
        studentsTable.setItems(studentList);


    }


    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception{
        selectedGroup = null;
        SceneChanger.changeScene(event,"admin/main-admin.fxml", "Admin Page");
    }
    @FXML
    protected void onGroupsButton(ActionEvent event) throws Exception{
        selectedGroup=null;
        SceneChanger.changeScene(event, "admin/groups-view.fxml", "Groups");
    }
    @FXML
    protected void onSubjectsButton(ActionEvent event) throws Exception{
        selectedGroup = null;
        SceneChanger.changeScene(event, "admin/admin-subjects.fxml", "Subjects");
    }
    @FXML
    protected void onStudentButton(ActionEvent event) throws Exception {
        selectedGroup = null;
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }

    private void addStudents() throws Exception{
        try {
            DbUtil.setDatabase("sms");
            ResultSet rest;

            if (selectedGroup!=null) rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM student WHERE group_id = \"G%s\";",
            selectedGroup.getID()));
            else rest = DbUtil.dbExecuteQuery("SELECT * FROM student;");
            Student student;
            int cnt = 0;
            while (rest.next()){
                student = new Student();
                cnt++;
                student.setCounter(cnt);
                student.setUserID(String.valueOf(rest.getInt("ID")));
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
                studentList.add(student);
            }
            rest.close();
            DbUtil.dbDisconnect();
        }catch (Exception ex){
            System.out.println("\nThere is no connection with server!\n");
        }
    }


    @FXML
    protected void onStudentsTable(MouseEvent event){
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent!=null){

            Edit.setID(selectedStudent.getUserID());
            Edit.setFirstName(selectedStudent.getName());
            Edit.setSecondName(selectedStudent.getSurname());
            Edit.setMiddleName(selectedStudent.getMiddleName());
            Edit.setGender(selectedStudent.getGender());
            Edit.setBirthDate(selectedStudent.getBirthDate());
            Edit.setPhone(selectedStudent.getPhone());
            Edit.setEmail(selectedStudent.getUserName());
            Edit.setConfirmed(selectedStudent.isRegistered());

            resetBtn.setDisable(false);
            editBtn.setDisable(false);
            deleteBtn.setDisable(false);
            if (selectedGroup!=null){
                kickBtn.setDisable(false);
                joinBtn.setDisable(true);
            }
            addBtn.setDisable(true);
        }
    }


    @FXML
    protected void onEditBtn(ActionEvent event){
        try {
            SceneChanger.changeScene(event, "admin/update-form.fxml", "Update Details");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteBtn(ActionEvent event){
        try {
            SceneChanger.changeScene(event, "admin/delete-preview.fxml", "Delete Message");

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }



    @FXML
    protected void onAddBtn(ActionEvent event){
        try {
            SceneChanger.changeScene(event, "admin/update-form.fxml", "Update Details");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onResetBtn(ActionEvent event){
        studentsTable.getSelectionModel().clearSelection();
        Edit.setAllNull();
        if (selectedGroup!=null){
            kickBtn.setDisable(true);
            joinBtn.setDisable(false);
        }
        resetBtn.setDisable(true);
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
        addBtn.setDisable(false);
    }

    @FXML
    protected void onJoinBtn(ActionEvent event) throws Exception{
        StudentsListController.setGroupID(selectedGroup.getID());
        SceneChanger.changeScene(event, "admin/students-without-group.fxml", "Students` List");
    }

    @FXML
    protected void onKickBtn(ActionEvent event) throws Exception{
        DbUtil.setDatabase("sms");
        DbUtil.dbExecuteUpdate(String.format("UPDATE student SET group_id = NULL WHERE ID = %d;", Integer.valueOf(Edit.getID())));
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }
    public static void setSelectedGroup(Group group){
        StudentsController.selectedGroup = group;
    }



}
