package com.example.smsdemo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class tesyControl implements Initializable {

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Male Actors", 80),
                new PieChart.Data("Female Actors", 120)
        );

        pieChart.setData(pieData);
        pieChart.setMinSize(200, 200);
        pieChart.setLabelsVisible(false);
//        pieChart.setClockwise(false);
//        pieChart.set
    }
}
