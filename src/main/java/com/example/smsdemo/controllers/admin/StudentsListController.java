package com.example.smsdemo.controllers.admin;

import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.Admin;
import com.example.smsdemo.models.Group;
import com.example.smsdemo.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudentsListController implements Initializable {

    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, Integer> number;
    @FXML
    private TableColumn checkBox;
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

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private static ArrayList<Student> studentsToAdd = new ArrayList<>();
    private static String groupID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addStudents();
        }catch (Exception e){
            e.printStackTrace();
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

        Callback<TableColumn<Student, String>, TableCell<Student, String>> checkBoxCellFactory = (param) -> {
            final TableCell<Student, String> cell = new TableCell<Student, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final CheckBox checkBox = new CheckBox();
                        Student student = getTableRow().getItem();
                        if (student != null) {
                            if (!student.isRegistered()) checkBox.setDisable(true);
                            checkBox.setOnAction(event -> {
                                boolean add = true;
                                if (checkBox.isSelected()){
                                    for (Student addedStudent:studentsToAdd){
                                        if (addedStudent.getUserID().equals(student.getUserID())) {
                                            add = false;
                                            break;
                                        }
                                    }
                                    if (add) studentsToAdd.add(student);
                                }
                            });
//                            deleteButton.setText("Delete");
//                            deleteButton.setOnAction(event -> {
//                                try {
//                                    DbUtil.setDatabase("sms");
//                                    DbUtil.dbExecuteUpdate(String.format("DELETE FROM groups WHERE ID = %d;",
//                                            Integer.valueOf(group.getID())));
//                                    DbUtil.dbDisconnect();
//                                    SceneChanger.changeScene(event, "admin/groups-view.fxml", "Groups");
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            });
                            setGraphic(checkBox);
                            setText(null);
                        }

                    }
                }
            };
            return cell;
        };

        checkBox.setCellFactory(checkBoxCellFactory);

    }



    private void addStudents() throws Exception{
        try {
            DbUtil.setDatabase("sms");
            ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM student WHERE group_id IS NULL;");
            Student student;
            int cnt = 0;
            while (rest.next()){
                student = new Student();
                cnt++;
                student.setCounter(cnt);
                student.setUserID(String.valueOf(rest.getInt("ID")));
//                System.out.println(student.getUserID());
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
    protected void onAddButton(ActionEvent event) throws Exception{
        if (studentsToAdd.size()>0){
            DbUtil.setDatabase("sms");
            for (Student student:studentsToAdd){
                DbUtil.dbExecuteUpdate(String.format("UPDATE student SET group_id = \"G%s\" WHERE ID = %d;",
                        groupID, Integer.valueOf(student.getUserID())));
//                DbUtil.dbExecuteUpdate(String.format("UPDATE groups SET students_number = %d WHERE ID = %d;", ));
//                System.out.printf("%s %s %s\n", student.getName(), student.getSurname(), student.getMiddleName());
//                System.out.println(student.getCounter());
//                System.out.println(studentsTable.getColumns().get(1).getGraphic());
            }
            studentsToAdd.clear();
            SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
        }

    }

    @FXML
    protected void onBackButton(ActionEvent event) throws Exception{
        studentsToAdd.clear();
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }


    public static String getGroupID() {
        return groupID;
    }

    public static void setGroupID(String groupID) {
        StudentsListController.groupID = groupID;
    }
}
