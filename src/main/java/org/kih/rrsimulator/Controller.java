package org.kih.rrsimulator;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private StackedBarChart<Integer, String> stackedBarChart;
    @FXML
    private Spinner<Integer> pidSpinner, arrivalTimeSpinner, serviceTimeSpinner, timeQuantumSpinner;
    @FXML
    private Button addButton;
    @FXML
    private TableView<Process> processesView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinners();
        initButtons();
        initTableView();





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

    private void initSpinners() {
        pidSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        arrivalTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        serviceTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        timeQuantumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
    }

    private void initButtons() {
        addButton.setOnMouseClicked(mouseEvent -> {
            processesView.getItems().add(new Process(pidSpinner.getValue(), arrivalTimeSpinner.getValue(), serviceTimeSpinner.getValue()));
        });
    }

    private void initTableView() {
        processesView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("pid"));
        processesView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        processesView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("serviceTime"));
    }
}