package com.example.smsdemo.controllers.admin;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.controllers.utils.Validator;
import com.example.smsdemo.models.Edit;
import com.example.smsdemo.models.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private TextField ID;

    @FXML
    private TextField fName;

    @FXML
    private TextField sName;

    @FXML
    private TextField mName;

    @FXML
    private ToggleGroup gender;

    @FXML
    private DatePicker bDate;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;

    @FXML
    private Label message;

    @FXML
    private CheckBox isConfirmed;

    private DateTimeFormatter formatter;

    @FXML
    private Button updateBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        RadioButton toggleGender;

        if (Edit.getID()!=null){
            title.setText("Edit Details");
            ID.setText(Edit.getID());
            fName.setText(Edit.getFirstName());
            sName.setText(Edit.getSecondName());
            mName.setText(Edit.getMiddleName());

            for (Toggle toggle: gender.getToggles()){
                toggleGender = (RadioButton) toggle;

                if (toggleGender.getText().equalsIgnoreCase(Edit.getGender())) toggle.setSelected(true);

            }

            try {
                LocalDate birthDate = LocalDate.parse(Edit.getBirthDate(), formatter);
                bDate.setValue(birthDate);
            } catch (Exception e) {
                e.printStackTrace();
            }



            phone.setText(Edit.getPhone());
            email.setText(Edit.getEmail());
            isConfirmed.setSelected(Edit.isConfirmed());


            updateBtn.setText("Edit");

            updateBtn.setOnAction(event -> {

                fName.setText(fName.getText().trim());
                sName.setText(sName.getText().trim());
                mName.setText(mName.getText().trim());
                phone.setText(phone.getText().trim());
                email.setText(email.getText().trim());

                if (fName.getText().isEmpty()||sName.getText().isEmpty()||mName.getText().isEmpty()|| !Validator.isToggleSelected(gender)||bDate.getEditor().getText().isEmpty()||phone.getText().isEmpty()||email.getText().isEmpty()){
                    message.setTextFill(Paint.valueOf("red"));
                    message.setText("Do not leave important fields empty !");
                }else {
                    String birthDate="";
                    RadioButton genderToggle = (RadioButton) gender.getSelectedToggle();



                    if (!Validator.validateName(fName.getText())||!Validator.validateName(sName.getText())||!Validator.validateName(mName.getText())){
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("Write name in a correct format!");
                    } else if (!Validator.validatePhone(phone.getText())) {
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("Write phone number in a correct format!");
                    } else if (!Validator.validateEmail(email.getText())) {
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("Write email address in a correct format!");

                    } else{
                        ArrayList<String> dbFields = new ArrayList<>();
                        String dbQuery = "UPDATE student SET ";
                        try{
                                birthDate = formatter.format(bDate.getValue());
                        }catch (Exception ex){
                            message.setTextFill(Paint.valueOf("red"));
                            message.setText("Write birth date in a correct format!");
                        }

                        if (!fName.getText().equals(Edit.getFirstName())){
                            dbFields.add("f_name");
                            Edit.setFirstName(fName.getText());
                        }
                        if (!sName.getText().equals(Edit.getSecondName())){
                            dbFields.add("s_name");
                            Edit.setSecondName(sName.getText());
                        }
                        if (!mName.getText().equals(Edit.getMiddleName())){
                            dbFields.add("m_name");
                            Edit.setMiddleName(mName.getText());
                        }
                        if (!genderToggle.getText().equals(Edit.getGender())){
                            dbFields.add("gender");
                            Edit.setGender(genderToggle.getText());
                        }
                        if (!birthDate.equals(Edit.getBirthDate())){
                            dbFields.add("birth_date");
                            Edit.setBirthDate(birthDate);
                        }
                        if (!phone.getText().equals(Edit.getPhone())){
                            dbFields.add("phone");
                            Edit.setPhone(phone.getText());
                        }
                        if (!email.getText().equals(Edit.getEmail())){
                            dbFields.add("email");
                            Edit.setEmail(email.getText());
                        }
                        if (isConfirmed.isSelected()!=Edit.isConfirmed()){
                            dbFields.add("reg_status");
                            Edit.setConfirmed(isConfirmed.isSelected());
                        }

                        if (dbFields.isEmpty()){
                            message.setTextFill(Paint.valueOf("red"));
                            message.setText("There is no change made !");
                        }else {
                            for (String dbField:dbFields){
                                switch (dbField){
                                    case "f_name":
                                        dbQuery = dbQuery + dbField + "=" + String.format("'%s'", fName.getText());
                                        break;

                                    case "s_name":
                                        dbQuery = dbQuery + dbField + "=" + String.format("'%s'", sName.getText());
                                        break;

                                    case "m_name":
                                        dbQuery = dbQuery + dbField + "=" + String.format("'%s'", mName.getText());
                                        break;

                                    case "gender":
                                        dbQuery = dbQuery + dbField + "=" + String.format("'%ch'", genderToggle.getText().toLowerCase().charAt(0));
                                        break;

                                    case "birth_date":
                                        dbQuery = dbQuery + dbField + "=" + String.format("'%s'", birthDate);
                                        break;

                                    case "phone":
                                        dbQuery = dbQuery + dbField + "=" + String.format("'%s'", phone.getText().substring(1));
                                        break;

                                    case "email":
                                        dbQuery = dbQuery + dbField + "=" + String.format("'%s'", email.getText());
                                        break;

                                    case "reg_status":

                                        int regStatus;

                                        if (isConfirmed.isSelected()) regStatus = 1;
                                        else regStatus = 0;

                                        dbQuery = dbQuery + dbField + "=" + String.format("%d", regStatus);
                                        break;

                                }

                                if (dbFields.get(dbFields.size()-1).equals(dbField)) {
                                    dbQuery = dbQuery + String.format(" WHERE ID=%d;", Integer.valueOf(ID.getText()));
                                    break;
                                }
                                else {
                                    dbQuery = dbQuery + ", ";
                                }

                            }

//                    Updating Student Details

                            try {
                                DbUtil.setDatabase("sms");
                                DbUtil.dbExecuteUpdate(dbQuery);

                            }catch (Exception ex){
                                ex.printStackTrace();
                            }

                            message.setTextFill(Paint.valueOf("green"));
                            message.setText("Successfully Updated !");



                        }



                    }
                }

            });




        }else {
                title.setText("Add Student");
                updateBtn.setText("Add");
                updateBtn.setOnAction(event -> {

                    fName.setText(fName.getText().trim());
                    sName.setText(sName.getText().trim());
                    mName.setText(mName.getText().trim());
                    phone.setText(phone.getText().trim());
                    email.setText(email.getText().trim());

                    if (fName.getText().isEmpty()||sName.getText().isEmpty()||mName.getText().isEmpty()|| !Validator.isToggleSelected(gender)||bDate.getEditor().getText().isEmpty()||phone.getText().isEmpty()||email.getText().isEmpty()){
                        message.setTextFill(Paint.valueOf("red"));
                        message.setText("Do not leave important fields empty !");

                    }else {

                        if (!Validator.validateName(fName.getText())||!Validator.validateName(sName.getText())||!Validator.validateName(mName.getText())){
                            message.setTextFill(Paint.valueOf("red"));
                            message.setText("Write name in a correct format!");
                        } else if (!Validator.validatePhone(phone.getText())) {
                            message.setTextFill(Paint.valueOf("red"));
                            message.setText("Write phone number in a correct format!");
                        } else if (!Validator.validateEmail(email.getText())) {
                            message.setTextFill(Paint.valueOf("red"));
                            message.setText("Write email address in a correct format!");
                        }else {
                            try {
                                DbUtil.setDatabase("sms");
                                ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM student WHERE email = \"%s\";", email.getText()));

                                if (rest.next()) {
                                    rest.close();
                                    DbUtil.dbDisconnect();
                                    message.setTextFill(Paint.valueOf("red"));
                                    message.setText("Student with this email address already exists!");

                                }else {
                                    int confInt;
                                    Student newStudent = new Student();
                                    newStudent.setName(fName.getText());
                                    newStudent.setSurname(sName.getText());
                                    newStudent.setMiddleName(mName.getText());
                                    newStudent.setGender(((RadioButton) gender.getSelectedToggle()).getText());
                                    newStudent.setBirthDate(formatter.format(bDate.getValue()));
                                    newStudent.setUserType("student");
                                    newStudent.setUserName(email.getText());
                                    newStudent.setPhone(phone.getText());
                                    newStudent.setRegistered(isConfirmed.isSelected());

                                    if (newStudent.isRegistered()) confInt = 1;
                                    else confInt = 0;

                                    DbUtil.dbExecuteUpdate(String.format("INSERT INTO student " +
                                            "(f_name, s_name, m_name, gender, birth_date, user_type,email, phone, password, reg_status, log_status) VALUES " +
                                            "(\"%s\", \"%s\", \"%s\", '%s', \"%s\", '%s', \"%s\", \"%s\", \"%s\", %d, %d);" +
                                            "", newStudent.getName(), newStudent.getSurname(), newStudent.getMiddleName(),
                                            String.valueOf(newStudent.getGender().toLowerCase().charAt(0)), newStudent.getBirthDate(),
                                            String.valueOf(newStudent.getUserType().toUpperCase().charAt(0)), newStudent.getUserName(),
                                            newStudent.getPhone().substring(1), newStudent.getUserName(), confInt,0));


                                    rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM %s WHERE email=\"%s\";",
                                            newStudent.getUserType(), newStudent.getUserName()));
                                    rest.next();
                                    newStudent.setUserID(String.valueOf(rest.getInt("ID")));
                                    rest.close();
                                    DbUtil.dbDisconnect();


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
                                            ");", newStudent.getUserID()));

                                    message.setTextFill(Paint.valueOf("green"));
                                    message.setText("Successfully signed up !");


                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }



                });


        }

    }

    @FXML
    protected void onCancelButton(ActionEvent event) throws Exception{
        Edit.setAllNull();
        SceneChanger.changeScene(event, "admin/student-view.fxml", "Students");
    }








}
