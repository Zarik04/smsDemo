package com.example.smsdemo;

import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.controllers.utils.Validator;
import com.example.smsdemo.models.LogSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public class ChangePasswordController {

    @FXML
    private TextField newPassword;

    @FXML
    private TextField oldPassword;

    @FXML
    private Label message;


    @FXML
    protected void onCancelBtn(ActionEvent event){
        try {
            LoginController.logUserOut();
            SceneChanger.changeScene(event, "login-view.fxml", "Login Page");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onChangeBtn(ActionEvent event){
        oldPassword.setText(oldPassword.getText().trim());
        newPassword.setText(newPassword.getText().trim());

        if (oldPassword.getText().isEmpty()||newPassword.getText().isEmpty()){
            message.setTextFill(Paint.valueOf("red"));
            message.setText("Please, do not leave any field empty !");

        }else if (!Validator.validatePwd(newPassword.getText())) {
            message.setTextFill(Paint.valueOf("red"));
            message.setText("Password must contain at least one digit [0-9].\n" +
                    "Password must contain at least one lowercase Latin character [a-z].\n" +
                    "Password must contain at least one uppercase Latin character [A-Z].\n" +
                    "Password must contain at least one special character like ! @ # & ( ).\n" +
                    "Password must contain a length of at least 8 characters and a maximum of 20 characters.");
        }else if (!LogSession.getUser().getPassword().equals(oldPassword.getText())){
            message.setTextFill(Paint.valueOf("red"));
            message.setText("Wrong old password !");
        }else {
            LogSession.getUser().setPassword(newPassword.getText());
            try {
                DbUtil.setDatabase("sms");
                DbUtil.dbExecuteUpdate(String.format("UPDATE student SET password = \"%s\" WHERE ID = %d;", LogSession.getUser().getPassword(), Integer.valueOf(LogSession.getUser().getUserID())));
            }catch (Exception ex){
                ex.printStackTrace();
            }

            message.setTextFill(Paint.valueOf("green"));
            message.setText("Successfully Updated !");
            oldPassword.setText("");
            newPassword.setText("");
        }
    }

}
