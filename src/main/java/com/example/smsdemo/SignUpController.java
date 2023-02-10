package com.example.smsdemo;

import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.controllers.utils.Validator;
import com.example.smsdemo.models.Student;
import com.example.smsdemo.models.Teacher;
import com.example.smsdemo.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Paint;

import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;

public class SignUpController {

    @FXML
    private TextField fName;
    @FXML
    private TextField sName;
    @FXML
    private TextField mName;

    @FXML
    private ToggleGroup genderRadio;

    @FXML
    private DatePicker birthDate;

    @FXML
    private ToggleGroup userType;

    @FXML
    private TextField email;
    @FXML
    private TextField phone;
    @FXML
    private PasswordField pwd;

    @FXML
    private Button resetButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label signUpMessage;

    @FXML
    private Button signUpToLogBut;


    @FXML
    protected void onResetButton(ActionEvent event) {
        fName.setText("");
        sName.setText("");
        mName.setText("");

        if (Validator.isToggleSelected(genderRadio)) {
            genderRadio.getSelectedToggle().setSelected(false);
        }
        if (Validator.isToggleSelected(userType)){
            userType.getSelectedToggle().setSelected(false);
        }

        birthDate.getEditor().clear();
        email.setText("");
        phone.setText("");
        pwd.setText("");
        signUpMessage.setText("");

    }

    @FXML
    protected void onSignUpButton(ActionEvent event) {
        signUpMessage.setText("");
        trimAll();
        String dateOfBirth;
        String pattern = "dd/MM/yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        try {
            dateOfBirth = dateFormatter.format(birthDate.getValue());
        } catch (Exception e) {
            dateOfBirth = null;
        }


        if (fName.getText().isEmpty() || sName.getText().isEmpty() || mName.getText().isEmpty() || !Validator.isToggleSelected(genderRadio) || birthDate.getEditor().getText().isEmpty() || !Validator.isToggleSelected(genderRadio) || email.getText().isEmpty() || phone.getText().isEmpty() || pwd.getText().isEmpty()) {
            signUpMessage.setTextFill(Paint.valueOf("red"));
            signUpMessage.setText("Please, do not leave any field empty!");
        } else if (!Validator.validateName(fName.getText()) || !Validator.validateName(sName.getText()) || !Validator.validateName(mName.getText())) {
            signUpMessage.setTextFill(Paint.valueOf("red"));
            signUpMessage.setText("Please, write your name in a correct format!");
        } else if (dateOfBirth == null) {
            signUpMessage.setTextFill(Paint.valueOf("red"));
            signUpMessage.setText("Please, write your birth date in a correct format!");
        } else if (!Validator.validateEmail(email.getText())) {
            signUpMessage.setTextFill(Paint.valueOf("red"));
            signUpMessage.setText("Please, write your email address in a correct format!");
        } else if (!Validator.validatePhone(phone.getText())) {
            signUpMessage.setTextFill(Paint.valueOf("red"));
            signUpMessage.setText("Please, write your phone number in a correct format!");
        } else if (!Validator.validatePwd(pwd.getText())) {
            signUpMessage.setTextFill(Paint.valueOf("red"));
            signUpMessage.setText("Password must contain at least one digit [0-9].\n" +
                    "Password must contain at least one lowercase Latin character [a-z].\n" +
                    "Password must contain at least one uppercase Latin character [A-Z].\n" +
                    "Password must contain at least one special character like ! @ # & ( ).\n" +
                    "Password must contain a length of at least 8 characters and a maximum of 20 characters.");

        } else {
            DbUtil.setDatabase("sms");
            User newUser;
            try {

                if (((RadioButton) userType.getSelectedToggle()).getText().equalsIgnoreCase("student")) newUser = new Student();
                else newUser = new Teacher();

                newUser.setName(fName.getText());
                newUser.setSurname(sName.getText());
                newUser.setMiddleName(mName.getText());
                newUser.setGender(((RadioButton) genderRadio.getSelectedToggle()).getText());
                newUser.setBirthDate(dateOfBirth);
                newUser.setUserType(((RadioButton) userType.getSelectedToggle()).getText().toLowerCase());
                newUser.setUserName(email.getText());
                newUser.setPhone(phone.getText());
                newUser.setPassword(pwd.getText());

                DbUtil.dbExecuteUpdate(String.format("INSERT INTO %s " +
                        "(f_name, s_name, m_name, gender, birth_date, user_type, email, phone, password, reg_status, log_status) VALUES " +
                        "(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\",\"%s\", %d, %d)" +
                        "", newUser.getUserType(), newUser.getName(), newUser.getSurname(), newUser.getMiddleName(),
                        String.valueOf(newUser.getGender().toLowerCase().charAt(0)), newUser.getBirthDate(),
                        String.valueOf(newUser.getUserType().toUpperCase().charAt(0)), newUser.getUserName(),
                        newUser.getPhone().substring(1), newUser.getPassword(), 0, 0));

                ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM %s WHERE email=\"%s\";",
                                                                    newUser.getUserType(), newUser.getUserName()));
                rest.next();
                newUser.setUserID(String.valueOf(rest.getInt("ID")));
                rest.close();
                DbUtil.dbDisconnect();

                if (newUser.getUserType().equalsIgnoreCase("student")){
                    DbUtil.setDatabase("sms_courses");
                    DbUtil.dbExecuteUpdate(String.format("CREATE TABLE s%s (" +
                            "ID INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
                            "course_name VARCHAR(150), " +
                            "course_id VARCHAR(8), " +
                            "course_logo VARCHAR(150), " +
                            "start_date VARCHAR(50), " +
                            "enrolled_date VARCHAR(50), " +
                            "end_date VARCHAR(50), " +
                            "status TINYINT(1), " +
                            "teacher VARCHAR(11)" +
                            ");", newUser.getUserID()));
                }

                signUpMessage.setTextFill(Paint.valueOf("green"));
                signUpMessage.setText("Successfully signed up !");


            } catch (Exception ex) {
                signUpMessage.setTextFill(Paint.valueOf("red"));
                signUpMessage.setText("Something went wrong!\nThere is no connection with server!\nPlease, try later!");
                ex.printStackTrace();
            }
        }

    }

    @FXML
    protected void onJumpButton(ActionEvent event) throws Exception {
        SceneChanger.changeScene(event, "login-view.fxml", "Login Page");
    }

    private void trimAll(){
        fName.setText(fName.getText().trim());
        sName.setText(sName.getText().trim());
        mName.setText(mName.getText().trim());
        email.setText(email.getText().trim());
        phone.setText(phone.getText().trim());
        pwd.setText(pwd.getText().trim());
    }


}
