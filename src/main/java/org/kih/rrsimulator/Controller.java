package org.kih.rrsimulator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private StackedBarChart<Integer, String> stackedBarChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        XYChart.Series<Integer, String> series1 = new XYChart.Series<>();
        XYChart.Series<Integer, String> series2 = new XYChart.Series<>();

        stackedBarChart.getData().add(series1);
        stackedBarChart.getData().add(series2);

        series1.setName("BT");
        XYChart.Data<Integer, String> data1 = new XYChart.Data<>(3, "P1");
        XYChart.Data<Integer, String> data2 = new XYChart.Data<>(4, "P1");
        XYChart.Data<Integer, String> data3 = new XYChart.Data<>(3, "P1");
        series1.getData().add(data1);
        series1.getData().add(data2);
        series1.getData().add(data3);

        data1.getNode().getStyleClass().add("transparent");
        data2.getNode().getStyleClass().add("wait");
        data3.getNode().getStyleClass().add("burst");
        data2.getNode().setOnMouseClicked(event -> {
            System.out.println("Button was clicked!");
        });
        data2.getNode().setOnMouseEntered(mouseEvent -> data2.getNode().setCursor(Cursor.HAND));
    }
}