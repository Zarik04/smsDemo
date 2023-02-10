package com.example.smsdemo.controllers.utils;

import com.example.smsdemo.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SceneChanger {


    public static void changeScene(ActionEvent event, String newScene, String title) throws Exception{
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource(newScene)));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void changeScene(MouseEvent event, String newScene, String title) throws Exception{
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource(newScene)));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }



}
