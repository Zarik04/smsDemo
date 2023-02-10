package com.example.smsdemo.controllers.admin;

import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.controllers.utils.Validator;
import com.example.smsdemo.models.Course;
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
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GroupsController implements Initializable {

    //  Groups Table and its columns
    @FXML
    private TableView<Group> groupList;
    @FXML
    private TableColumn<Group, Integer> groupCounter;
    @FXML
    private TableColumn<Group, String> groupID;
    @FXML
    private TableColumn<Group, String> groupTiming;
    @FXML
    private TableColumn<Group, String> dateCreated;
    @FXML
    private TableColumn<Group, Integer> studentsCount;
    @FXML
    private TableColumn<Group, Integer> maxCount;
    @FXML
    private TableColumn actionField;

    private ObservableList<Group> groupObservableList = FXCollections.observableArrayList();

    private Group selectedGroup;



//  Update Form

    @FXML
    private TextField idField;
    @FXML
    private TextField dateCreatedField;
    @FXML
    private ToggleGroup timingToggle;
    @FXML
    private TextField maxCountField;
    @FXML
    private Label message;
    @FXML
    private Button actionButton;


    @FXML
    private Button viewButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            actionButton.setText("Add");
            actionButton.setOnAction(event -> {
                try {
                    addNewGroup(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            populateGroupsTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception {
        SceneChanger.changeScene(event, "admin/main-admin.fxml", "Main Page");
    }

    @FXML
    protected void onStudentsButton(ActionEvent event) throws Exception {
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }
    @FXML
    protected void onSubjectsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "admin/admin-subjects.fxml", "Subjects");
    }

    @FXML
    protected void onResetButton(ActionEvent event) {
        groupList.getSelectionModel().clearSelection();
        selectedGroup = null;
        viewButton.setDisable(true);
        idField.setText("");
        dateCreatedField.setText("");
        if (Validator.isToggleSelected(timingToggle)) timingToggle.getSelectedToggle().setSelected(false);
        maxCountField.setText("");
        message.setText("");

        actionButton.setText("Add");
        actionButton.setOnAction(actionEvent -> {
            try {
                addNewGroup(actionEvent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    private void editCurrentGroup(ActionEvent event) throws Exception {

    }

    private void addNewGroup(ActionEvent event) throws Exception {
        dateCreatedField.setText(dateCreatedField.getText().trim());
        maxCountField.setText(maxCountField.getText().trim());

        if (dateCreatedField.getText().isEmpty() || !Validator.isToggleSelected(timingToggle) || maxCountField.getText().isEmpty()) {

            message.setTextFill(Paint.valueOf("red"));
            message.setText("Please, do not leave important fields empty !");

        } else if (!Validator.isNumeric(maxCountField.getText())) {
            message.setTextFill(Paint.valueOf("red"));
            message.setText("Number of students must be positive integer !");
        } else {
            try {
                DbUtil.setDatabase("sms");
                DbUtil.dbExecuteUpdate(String.format("INSERT INTO groups (timing, date_created, students_number, max_students)" +
                                " VALUES (\"%s\", \"%s\", %d, %d);", ((RadioButton) timingToggle.getSelectedToggle()).getText().toLowerCase(),
                        dateCreatedField.getText(), 0, Integer.valueOf(maxCountField.getText())));
                SceneChanger.changeScene(event, "admin/groups-view.fxml", "Groups");
            } catch (Exception e) {
                message.setTextFill(Paint.valueOf("red"));
                message.setText("There is no connection with server !");
                e.printStackTrace();
            }
        }
    }

    private void populateGroupsTable() throws Exception {
        DbUtil.setDatabase("sms");
        ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM groups;");
        int cnt = 0;
        while (rest.next()) {
            cnt++;
            Group newGroup = new Group();
            newGroup.setCounter(cnt);
            newGroup.setID(String.valueOf(rest.getInt("ID")));
            newGroup.setTiming(rest.getString("timing"));
            newGroup.setCreatedDate(rest.getString("date_created"));
            newGroup.setNumberOfStudents(rest.getInt("students_number"));
            newGroup.setMaxNumberOfStudents(rest.getInt("max_students"));

            groupObservableList.add(newGroup);
        }

        rest.close();
        DbUtil.dbDisconnect();

        groupCounter.setCellValueFactory(new PropertyValueFactory<Group, Integer>("counter"));
        groupID.setCellValueFactory(new PropertyValueFactory<Group, String>("ID"));
        groupTiming.setCellValueFactory(new PropertyValueFactory<Group, String>("timing"));
        dateCreated.setCellValueFactory(new PropertyValueFactory<Group, String>("createdDate"));
        studentsCount.setCellValueFactory(new PropertyValueFactory<Group, Integer>("numberOfStudents"));
        maxCount.setCellValueFactory(new PropertyValueFactory<Group, Integer>("maxNumberOfStudents"));

        groupList.setItems(groupObservableList);


        Callback<TableColumn<Group, String>, TableCell<Group, String>> actionCellFactory = (param) -> {
            final TableCell<Group, String> cell = new TableCell<Group, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button deleteButton = new Button();
                        Group group = getTableRow().getItem();
                        if (group != null) {
                            deleteButton.setText("Delete");
                            deleteButton.setOnAction(event -> {
                                try {
                                    DbUtil.setDatabase("sms");
                                    DbUtil.dbExecuteUpdate(String.format("DELETE FROM groups WHERE ID = %d;",
                                            Integer.valueOf(group.getID())));
                                    DbUtil.dbDisconnect();
                                    SceneChanger.changeScene(event, "admin/groups-view.fxml", "Groups");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                        }
                        setGraphic(deleteButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };

        actionField.setCellFactory(actionCellFactory);


    }

    @FXML
    protected void onGroupsList(MouseEvent mouseEvent) {
        selectedGroup = groupList.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            idField.setText(selectedGroup.getID());
            dateCreatedField.setText(selectedGroup.getCreatedDate());
            for (Toggle toggle : timingToggle.getToggles()) {
                if (((RadioButton) toggle).getText().equalsIgnoreCase(selectedGroup.getTiming())) {
                    timingToggle.selectToggle(toggle);
                    break;
                }
            }
            maxCountField.setText(String.valueOf(selectedGroup.getMaxNumberOfStudents()));
            viewButton.setDisable(false);

            actionButton.setText("Edit");
            actionButton.setOnAction(event -> {


                dateCreatedField.setText(dateCreatedField.getText().trim());
                maxCountField.setText(maxCountField.getText().trim());
                if (dateCreatedField.getText().isEmpty() || !Validator.isToggleSelected(timingToggle) || maxCountField.getText().isEmpty()) {

                    message.setTextFill(Paint.valueOf("red"));
                    message.setText("Please, do not leave important fields empty !");

                } else if (!Validator.isNumeric(maxCountField.getText())) {
                    message.setTextFill(Paint.valueOf("red"));
                    message.setText("Number of students must be positive integer !");
                } else {
                    DbUtil.setDatabase("sms");
                    try {
                        DbUtil.dbExecuteUpdate(String.format("UPDATE groups SET timing = \"%s\", date_created = \"%s\", " +
                                        "max_students = %d WHERE ID = %d;", ((RadioButton) timingToggle.getSelectedToggle()).getText().toLowerCase(),
                                dateCreatedField.getText(), Integer.valueOf(maxCountField.getText()), Integer.valueOf(idField.getText())));
                        SceneChanger.changeScene(event, "admin/groups-view.fxml", "Groups");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


            });
        }


    }


    @FXML
    protected void onViewButton(ActionEvent event) throws Exception{
        StudentsController.setSelectedGroup(selectedGroup);
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }
}

