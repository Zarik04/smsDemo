package com.example.smsdemo.controllers.student;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.LoginController;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.LogSession;
import com.example.smsdemo.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ImageView ava;

    @FXML
    private Label fName;

    @FXML
    private Label sName;

    @FXML
    private Label mName;

    @FXML
    private Label gender;

    @FXML
    private Label studentID;

    @FXML
    private Label logStatus;

    @FXML
    private Label phone;

    @FXML
    private Label birthDate;

    @FXML
    private LineChart<?, ?> attendanceChart;


    @FXML
    private LineChart<?, ?> performanceChart;

    @FXML
    private Button logOutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUserData();
        attendanceChart.setTitle("Attendance");
        performanceChart.setTitle("Performance");
    }

    private void setUserData(){
        try {
            User loggedUser = LogSession.getUser();
            if (loggedUser.getGender().equalsIgnoreCase("male")) ava.setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaM.png")));
            else ava.setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaW.png")));
            fName.setText("First Name: "+loggedUser.getName());
            sName.setText("Second Name: "+loggedUser.getSurname());
            mName.setText("Middle Name: "+loggedUser.getMiddleName());
            gender.setText("Gender: "+loggedUser.getGender());
            birthDate.setText("Date of Birth: "+loggedUser.getBirthDate());
            studentID.setText("Student ID: "+loggedUser.getUserID());
            if (loggedUser.isOnline()) logStatus.setText("Status: Online");
            else logStatus.setText("Status: Offline");
            phone.setText("Phone Number: "+loggedUser.getPhone());
        }catch (Exception ex){
            System.out.println();
            ex.printStackTrace();
        }
    }


    @FXML
    protected void onLogOutButton(ActionEvent event) throws Exception {
        try {
            LoginController.logUserOut();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            SceneChanger.changeScene(event, "login-view.fxml", "Login Page");
        }

    }

    @FXML
    protected void onClassroomButton(ActionEvent event) throws Exception {
        SceneChanger.changeScene(event, "student/lms.fxml", "Classroom");
    }
}
