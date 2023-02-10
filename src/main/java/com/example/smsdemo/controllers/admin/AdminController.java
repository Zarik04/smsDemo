package com.example.smsdemo.controllers.admin;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.LoginController;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.Admin;
import com.example.smsdemo.models.LogSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private ImageView logo;

    @FXML
    private Label ID;

    @FXML
    private Label fName;

    @FXML
    private Label sName;

    @FXML
    private Label mName;

    @FXML
    private Label gender;

    @FXML
    private Label birthDate;

    @FXML
    private Label phone;

    @FXML
    private Label email;

    @FXML
    private PieChart confirmedChart;

    @FXML
    private PieChart genderChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setAdminData();
            setChartsData();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void setAdminData() throws Exception{
        Admin loggedUser = (Admin) LogSession.getUser();
        if (loggedUser.getGender().equalsIgnoreCase("male")) logo.setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaM.png")));
        else logo.setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaW.png")));
        ID.setText(ID.getText()+" "+loggedUser.getUserID());
        fName.setText(fName.getText()+" "+loggedUser.getName());
        sName.setText(sName.getText()+" "+loggedUser.getSurname());
        mName.setText(mName.getText()+" "+loggedUser.getMiddleName());
        gender.setText(gender.getText()+" "+loggedUser.getGender());
        birthDate.setText(birthDate.getText()+" "+loggedUser.getBirthDate());
        phone.setText(phone.getText()+" "+loggedUser.getPhone());
        email.setText(email.getText()+" "+loggedUser.getUserName());
    }

    private void setChartsData() throws Exception{
        ObservableList<PieChart.Data> confirmedChartData = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> genderChartData = FXCollections.observableArrayList();

        DbUtil.setDatabase("sms");
        ResultSet rest;

        rest =  DbUtil.dbExecuteQuery("SELECT * FROM student WHERE reg_status = 1;");
        confirmedChartData.add(new PieChart.Data("Confirmed Students", DbUtil.getSize(rest)));
        rest.close();
        DbUtil.dbDisconnect();

        rest = DbUtil.dbExecuteQuery("SELECT * FROM student WHERE reg_status = 0;");
        confirmedChartData.add(new PieChart.Data("Not Confirmed Students", DbUtil.getSize(rest)));
        rest.close();
        DbUtil.dbDisconnect();

        confirmedChart.setData(confirmedChartData);
        confirmedChart.setTitle("Confirmation of Students");
        confirmedChart.setLabelsVisible(false);


        rest = DbUtil.dbExecuteQuery("SELECT * FROM student WHERE gender = 'm';");
        genderChartData.add(new PieChart.Data("Male Students", DbUtil.getSize(rest)));
        rest.close();
        DbUtil.dbDisconnect();

        rest = DbUtil.dbExecuteQuery("SELECT * FROM student WHERE gender = 'f';");
        genderChartData.add(new PieChart.Data("Female Students", DbUtil.getSize(rest)));
        rest.close();
        DbUtil.dbDisconnect();

        genderChart.setData(genderChartData);
        genderChart.setTitle("Students by Gender");
        genderChart.setLabelsVisible(false);

    }

    @FXML
    protected void onSubjectsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "admin/admin-subjects.fxml", "Subjects");
    }

    @FXML
    protected void onStudentButton(ActionEvent event) throws Exception {
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }

    @FXML
    protected void onGroupsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "admin/groups-view.fxml", "Groups");
    }

    @FXML
    protected void onLogoutButton(ActionEvent event) throws Exception{
        try {
            LoginController.logUserOut();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            SceneChanger.changeScene(event, "login-view.fxml", "Login Page");
        }
    }


}
