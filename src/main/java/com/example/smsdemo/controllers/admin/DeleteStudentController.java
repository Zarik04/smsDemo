package com.example.smsdemo.controllers.admin;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.Edit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteStudentController implements Initializable {

    @FXML
    private Label deleteMessage;
    private String deleteQuery = "DELETE FROM student WHERE ID = %d;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteMessage.setText(deleteMessage.getText()+" "+ Edit.getID()+" ?");
    }

    @FXML
    protected void onDeleteCancelBtn(ActionEvent event) throws Exception{
        Edit.setAllNull();
        SceneChanger.changeScene(event, "admin/student-view.fxml","Students");
    }

    @FXML
    protected void onDeleteConfirmBtn(ActionEvent event) throws Exception{
        DbUtil.setDatabase("sms");
        DbUtil.dbExecuteUpdate(String.format(deleteQuery, Integer.valueOf(Edit.getID())));
        DbUtil.setDatabase("sms_courses");
        DbUtil.dbExecuteUpdate(String.format("DROP TABLE s%s;", Edit.getID()));
        Edit.setAllNull();
        SceneChanger.changeScene(event, "admin/student-view.fxml","Students");
    }


}
