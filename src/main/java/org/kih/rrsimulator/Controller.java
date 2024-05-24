package org.kih.rrsimulator;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private StackedBarChart<Integer, String> stackedBarChart;
    @FXML
    private Spinner<Integer> pidSpinner, arrivalTimeSpinner, serviceTimeSpinner, timeQuantumSpinner;
    @FXML
    private Button addButton, simulButton;
    @FXML
    private TableView<Process> processesView;
    @FXML
    private Label avgWaitTime, avgTaTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinners();
        initButtons();
        initTableView();


//        XYChart.Series<Integer, String> series1 = new XYChart.Series<>();
//        XYChart.Series<Integer, String> series2 = new XYChart.Series<>();
//
//        stackedBarChart.getData().add(series1);
//        stackedBarChart.getData().add(series2);
//
//        series1.setName("BT");
//        XYChart.Data<Integer, String> data1 = new XYChart.Data<>(3, "P1");
//        XYChart.Data<Integer, String> data2 = new XYChart.Data<>(4, "P1");
//        XYChart.Data<Integer, String> data3 = new XYChart.Data<>(3, "P1");
//        series1.getData().add(data1);
//        series1.getData().add(data2);
//        series1.getData().add(data3);
//
//        data1.getNode().getStyleClass().add("transparent");
//        data2.getNode().getStyleClass().add("wait");
//        data3.getNode().getStyleClass().add("burst");
//        data2.getNode().setOnMouseClicked(event -> {
//            System.out.println("Button was clicked!");
//        });
//        data2.getNode().setOnMouseEntered(mouseEvent -> data2.getNode().setCursor(Cursor.HAND));
    }

    private void initSpinners() {
        pidSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        arrivalTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        serviceTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        timeQuantumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
    }

    private void initButtons() {
        addButton.setOnMouseClicked(mouseEvent -> processesView.getItems().add(new Process(pidSpinner.getValue(), arrivalTimeSpinner.getValue(), serviceTimeSpinner.getValue())));
        simulButton.setOnMouseClicked(mouseEvent -> {
            List<Process> readyQueue = new LinkedList<>();
            final int timeQuantum = timeQuantumSpinner.getValue();
            int processTime = 0;
            Process runProcess = null;
            int t = 0;
            List<Process> processList = new ArrayList<>(processesView.getItems().size());
            processList.addAll(processesView.getItems());

            for (Process p : processList) {
                stackedBarChart.getData().add(new XYChart.Series<>());
            }

            while (true) {
                // 대기중인 프로세스 웨이팅시간 증가
                for (Process process : readyQueue) {
                    process.incWaitingTime();
                }
                // 프로세스 도착
                for (Process process : processList) {
                    if (process.getArrivalTime() == t) {
                        readyQueue.add(process);
                    }
                }
                // 실행중인 프로세스 존재할경우
                if (runProcess != null) {
                    runProcess.decRemainTime();
                    processTime++;
                    if (runProcess.getRemainTime() == 0) {
                        runProcess.setEndTime(t);
                        runProcess = null;
                        processTime = 0;
                    } else if (processTime == timeQuantum) {
                        readyQueue.add(runProcess);
                        runProcess = null;
                        processTime = 0;
                    }
                }
                // 실행중인 프로세스 존재하지않을경우
                if (runProcess == null) {
                    if (!readyQueue.isEmpty()) {
                        runProcess = readyQueue.remove(0);
                    }
                }
                // 종료조건
                if (runProcess == null && readyQueue.isEmpty() && processList.stream().allMatch(process -> process.getRemainTime() == 0)) {
                    break;
                }
                // 뷰 업데이트
                if (runProcess != null) {
                    XYChart.Data<Integer, String> data = new XYChart.Data<>(1, "P" + runProcess.getPid());
                    stackedBarChart.getData().get(processList.indexOf(runProcess)).getData().add(data);
                    data.getNode().getStyleClass().add("burst");
                }
                for (Process process : processList) {
                    XYChart.Data<Integer, String> data = new XYChart.Data<>(1, "P" + process.getPid());
                    if (readyQueue.contains(process)) {
                        stackedBarChart.getData().get(processList.indexOf(process)).getData().add(data);
                        data.getNode().getStyleClass().add("wait");
                    } else if (process.getArrivalTime() > t) {
                        stackedBarChart.getData().get(processList.indexOf(process)).getData().add(data);
                        data.getNode().getStyleClass().add("transparent");
                    }
                }

                t++;
            }
            avgWaitTime.setText("2");

        });
    }

    private void initTableView() {
        processesView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("pid"));
        processesView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        processesView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("serviceTime"));
    }
}