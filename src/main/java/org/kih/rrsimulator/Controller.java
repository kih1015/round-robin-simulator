package org.kih.rrsimulator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private StackedBarChart<Number, String> stackedBarChart;
    @FXML
    private Spinner<Integer> pidSpinner, arrivalTimeSpinner, serviceTimeSpinner, timeQuantumSpinner;
    @FXML
    private Button addButton, delButton, simulButton;
    @FXML
    private TableView<Process> processesView;
    @FXML
    private Label avgWaitTime, avgTaTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinners();
        initButtons();
        initTableView();
        stackedBarChart.getData().add(new XYChart.Series<>());

    }

    private void initSpinners() {
        pidSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        arrivalTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        serviceTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1));
        timeQuantumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1));
    }

    private void initButtons() {
        delButton.setOnMouseClicked(mouseEvent -> {
            ObservableList<Process> selected, all;
            all = processesView.getItems();
            selected = processesView.getSelectionModel().getSelectedItems();
            selected.forEach(all::remove);
        });
        addButton.setOnMouseClicked(mouseEvent -> {
            if (processesView.getItems().stream().allMatch(process -> process.getPid() != pidSpinner.getValue())) {
                processesView.getItems().add(new Process(pidSpinner.getValue(), arrivalTimeSpinner.getValue(), serviceTimeSpinner.getValue()));
            }
        });
        simulButton.setOnMouseClicked(mouseEvent -> {
            stackedBarChart.getData().get(0).getData().clear();
            List<Process> readyQueue = new LinkedList<>();
            final int timeQuantum = timeQuantumSpinner.getValue();
            int processTime = 0;
            Process runProcess = null;
            int t = 0;
            List<Process> processList = new ArrayList<>(processesView.getItems().size());
            processList.addAll(processesView.getItems());

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
                    XYChart.Data<Number, String> data = new XYChart.Data<>(1, "P" + runProcess.getPid());
                    stackedBarChart.getData().get(0).getData().add(data);
                    data.getNode().getStyleClass().add("burst");
                }
                for (Process process : processList) {
                    XYChart.Data<Number, String> data = new XYChart.Data<>(1, "P" + process.getPid());
                    if (readyQueue.contains(process)) {
                        stackedBarChart.getData().get(0).getData().add(data);
                        data.getNode().getStyleClass().add("wait");
                    } else if (process.getArrivalTime() > t) {
                        stackedBarChart.getData().get(0).getData().add(data);
                        data.getNode().getStyleClass().add("transparent");
                    }
                }

                t++;
            }
            double waitSum = 0, taSum = 0;
            for (Process process : processList) {
                waitSum += process.getWaitingTime();
                taSum += process.getEndTime() - process.getArrivalTime();
            }
            avgWaitTime.setText("" + waitSum / processList.size());
            avgTaTime.setText("" + taSum / processList.size());

        });
    }

    private void initTableView() {
        processesView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("pid"));
        processesView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        processesView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("serviceTime"));
    }

    private  void removeAll() {
        ObservableList<XYChart.Series<Number, String>> ol = stackedBarChart.getData();
        ol.get(0).getData().clear();
    }
}