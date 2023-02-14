package com.example.smsdemo;

import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.controllers.utils.Validator;
import com.example.smsdemo.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private Button logToSignUpButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginMessage;

    @FXML
    private PasswordField pwd;

    @FXML
    private Button resetButton;

    @FXML
    private TextField uName;
    @FXML
    private ToggleGroup loginType;


    @FXML
    protected void onResetButton(ActionEvent event) {
        uName.setText("");
        pwd.setText("");
        loginMessage.setText("");
    }

    @FXML
    protected void onLoginButton(ActionEvent event) {
        uName.setText(uName.getText().trim());
        pwd.setText(pwd.getText().trim());
        DbUtil.setDatabase("sms");
        loginMessage.setText("");
        if (uName.getText().isEmpty() || pwd.getText().isEmpty() || !Validator.isToggleSelected(loginType)) {
            loginMessage.setTextFill(Paint.valueOf("red"));
            loginMessage.setText("Please, do not leave any field empty!");
        } else if (Validator.validateEmail(uName.getText())) {
//            Detecting Login Type

            String tableName;
            RadioButton logType = (RadioButton) loginType.getSelectedToggle();
            if (logType.getText().equalsIgnoreCase("student")) tableName="student";
            else if (logType.getText().equalsIgnoreCase("teacher")) tableName="teacher";
            else tableName="admin";

            try {
                ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM %s WHERE email='%s' AND password='%s';", tableName, uName.getText().toLowerCase(), pwd.getText()));
                if (rest.next()) {
                    rest.close();
                    DbUtil.dbDisconnect();
                    logTheUser(event, uName.getText(), tableName);
                } else {
                    rest.close();
                    DbUtil.dbDisconnect();
                    loginMessage.setTextFill(Paint.valueOf("red"));
                    loginMessage.setText("Wrong Login Details !");
                }

            } catch (Exception ex) {

                loginMessage.setTextFill(Paint.valueOf("red"));
                loginMessage.setText("There is no connection with server !");
            }
        } else {

            String tableName;
            RadioButton logType = (RadioButton) loginType.getSelectedToggle();
            if (logType.getText().equalsIgnoreCase("student")) tableName="student";
            else if (logType.getText().equalsIgnoreCase("teacher")) tableName="teacher";
            else tableName="admin";

            try {
                int userID = Integer.valueOf(uName.getText());
                ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM %s WHERE ID=%d AND password='%s';", tableName, userID, pwd.getText()));
                if (rest.next() && uName.getText().length() == 7) {
                    rest.close();
                    DbUtil.dbDisconnect();
                    logTheUser(event, uName.getText(), tableName);
                } else {
                    rest.close();
                    DbUtil.dbDisconnect();
                    loginMessage.setTextFill(Paint.valueOf("red"));
                    loginMessage.setText("Wrong Login Details !");

                }

            } catch (Exception ex) {
                loginMessage.setTextFill(Paint.valueOf("red"));
                loginMessage.setText("There is no connection with server !");
            }
        }
    }


    @FXML
    protected void onJumpButton(ActionEvent event) throws Exception {
        SceneChanger.changeScene(event, "signup-view.fxml", "Sign Up Page");
    }

    private void logTheUser(ActionEvent event, String uName, String tableName) throws Exception{
        DbUtil.setDatabase("sms");
        if (uName.contains("@")) DbUtil.dbExecuteUpdate(String.format("UPDATE %s SET log_status=1 WHERE email='%s';", tableName, uName));
        else DbUtil.dbExecuteUpdate(String.format("UPDATE %s SET log_status=1 WHERE ID=%d;", tableName, Integer.valueOf(uName)));
        ResultSet rest;
        if (uName.contains("@")) rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM %s WHERE email='%s';", tableName, uName));
        else rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM %s WHERE ID=%d;", tableName, Integer.valueOf(uName)));
        rest.next();

        User loggedUser=null;
        if (rest.getString("user_type").equals("S")) loggedUser=new Student();
        else if (rest.getString("user_type").equals("T")) loggedUser = new Teacher();
        else loggedUser = new Admin();

        loggedUser.setUserID(rest.getString("ID"));
        loggedUser.setName(rest.getString("f_name"));
        loggedUser.setSurname(rest.getString("s_name"));
        loggedUser.setMiddleName(rest.getString("m_name"));
        if (rest.getString("gender").equalsIgnoreCase("m")) loggedUser.setGender("Male");
        else if (rest.getString("gender").equalsIgnoreCase("f")) loggedUser.setGender("Female");
        loggedUser.setBirthDate(rest.getString("birth_date"));
        if (rest.getString("user_type").equals("S")) loggedUser.setUserType("student");
        else if (rest.getString("user_type").equals("T")) loggedUser.setUserType("teacher");
        else loggedUser.setUserType("admin");
        loggedUser.setUserName(rest.getString("email"));
        loggedUser.setPhone("+"+rest.getString("phone"));
        loggedUser.setPassword(rest.getString("password"));
        if (rest.getInt("log_status")==1) loggedUser.setOnline(true);
        else loggedUser.setOnline(false);
        System.out.println(rest.getInt("log_status"));
        loggedUser.setRegistered(rest.getBoolean("reg_status"));
        LogSession.setUser(loggedUser);

        rest.close();
        DbUtil.dbDisconnect();

        if (LogSession.isUserSet()){
            SceneChanger.changeScene(event, "preview.fxml", "");
        }


    }

    public static void logUserOut() throws Exception{
        DbUtil.setDatabase("sms");
        String tableName = LogSession.getUser().getUserType();
        DbUtil.dbExecuteUpdate(String.format("UPDATE %s SET log_status=0 WHERE ID=%d;", tableName, Integer.valueOf(LogSession.getUser().getUserID())));
        LogSession.setUser(null);

        File userFilesFolder = new File("user files");
        String[] userFiles = userFilesFolder.list();

        for (String fileName: userFiles){
            try {
                (new File(userFilesFolder.getAbsolutePath()+"/"+fileName)).delete();
            }catch (Exception e){
                System.out.printf("\n\tCould not delete %s in user files folder !!!\n", fileName);
            }
        }

    }

}
