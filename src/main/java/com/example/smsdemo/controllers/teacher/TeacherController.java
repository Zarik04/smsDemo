package com.example.smsdemo.controllers.teacher;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.LoginController;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.LogSession;
import com.example.smsdemo.models.Teacher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setTeacherData();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @FXML
    protected void onSubjectsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "teacher/teacher-subjects.fxml", "Subjects");
    }

    private void setTeacherData() throws Exception{
        Teacher loggedUser = (Teacher) LogSession.getUser();
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

    @FXML
    protected void onLogoutButton(ActionEvent event) throws Exception {
        try {
            LoginController.logUserOut();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            SceneChanger.changeScene(event, "login-view.fxml", "Login Page");
        }
    }



}
