package com.example.smsdemo;

import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.LogSession;
import com.example.smsdemo.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class previewController implements Initializable {

    @FXML
    private Button actionBtn;

    @FXML
    private Label message;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User loggedUser = LogSession.getUser();
        if (loggedUser.isRegistered()){

            if (loggedUser.getUserName().equalsIgnoreCase(loggedUser.getPassword())){
                message.setText("Your password is set by default !\nPlease change your password!");
                message.setTextFill(Paint.valueOf("red"));
                actionBtn.setText("Change Password");
                actionBtn.setOnAction(event -> {
                    try {
                        SceneChanger.changeScene(event, "change-password.fxml", "Change Password");

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                });
            }else {
                message.setText("You are confirmed !");
                message.setTextFill(Paint.valueOf("green"));
                actionBtn.setText("Main Page");
                actionBtn.setOnAction(event -> {

                    try {
                        if (LogSession.getUser().getUserType().equalsIgnoreCase("student")) SceneChanger.changeScene(event, "student/main-view.fxml", "Main Page");
                        else if (LogSession.getUser().getUserType().equalsIgnoreCase("teacher")) SceneChanger.changeScene(event, "teacher/main-teacher.fxml", "Main Page");
                        else if (LogSession.getUser().getUserType().equalsIgnoreCase("admin")) SceneChanger.changeScene(event, "admin/main-admin.fxml", "Admin Page");
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                });
            }


        } else{
            message.setText("You are not confirmed !");
            message.setTextFill(Paint.valueOf("red"));
            actionBtn.setText("Login Page");
            actionBtn.setOnAction(event -> {

                try {
                    LoginController.logUserOut();
                    SceneChanger.changeScene(event, "login-view.fxml", "Login Page");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });

        }
    }
}
