package com.example.final_12_lead;

import com.example.serialcommunicationmodule.OnReceiveDataListenerFromModuleToUI;
import com.example.serialcommunicationmodule.SpandanUsbCommunication;
import com.fazecast.jSerialComm.SerialPort;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class SampleController implements Initializable {
    @FXML
    private Label loggingStatus;

    private static final int NUM_CHARTS = 12;
    private static final int WINDOW_SIZE = 500;
    private static final int UPDATE_INTERVAL_MS = 10;
    private Random random = new Random();
    private List<XYChart.Series<Number, Number>> seriesList = new ArrayList<>();
    private List<LineChart<Number, Number>> chartList = new ArrayList<>();
    private int dataCount = 0;
    AnimationTimer timer = null;

    @FXML
    private VBox vbox;
    SerialPort mySerialPort;
    public static BooleanProperty deviceDetectedFromThread = new SimpleBooleanProperty(false);

    public SampleController() {
        Thread deviceDetectingThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                try {
                    SerialPort[] availablePorts = SerialPort.getCommPorts();
                    mySerialPort = availablePorts[0];
                    deviceDetectedFromThread.set(true);
                    Thread.sleep(500);
                } catch (Exception e) {
                    deviceDetectedFromThread.set(false);
                }
            }
        });
        deviceDetectingThread.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deviceDetectedFromThread.addListener((observableValue, deviceDisconnectedListener, deviceConnectedListener) -> {
            if (deviceConnectedListener) {
                Platform.runLater(() -> {
                    loggingStatus.setStyle("-fx-background-color : green");
                    loggingStatus.setText("Device Connected");
                });
            } else {
                Platform.runLater(() -> {
                    loggingStatus.setStyle("-fx-background-color : red");
                    loggingStatus.setText("Device Disconnected");
//                    isGraphActive=false;
//                    clearGraph();
                });
            }
        });
        for (int i = 0; i < NUM_CHARTS; i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            seriesList.add(series);

            NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 10);
            NumberAxis yAxis = new NumberAxis();
            xAxis.setTickLabelsVisible(false);
            xAxis.setMinorTickVisible(false);
            xAxis.setTickMarkVisible(false);
            yAxis.setTickLabelsVisible(false);
            yAxis.setMinorTickVisible(false);
            yAxis.setTickMarkVisible(false);

            LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

            switch (i) {
                case 0 -> {
                    chart.setTitle("Lead1");
                    series.setName("lead1Series");
                    chart.getData().add(series);
                }
                case 1 -> {
                    chart.setTitle("Lead2");
                    series.setName("lead2Series");
                    chart.getData().add(series);
                }
                case 2 -> {
                    chart.setTitle("Lead3");
                    series.setName("lead3Series");
                    chart.getData().add(series);
                }
                case 3 -> {
                    chart.setTitle("V1");
                    series.setName("V1Series");
                    chart.getData().add(series);
                }
                case 4 -> {
                    chart.setTitle("V2");
                    series.setName("V2Series");
                    chart.getData().add(series);
                }
                case 5 -> {
                    chart.setTitle("V3");
                    series.setName("V3Series");
                    chart.getData().add(series);
                }
                case 6 -> {
                    chart.setTitle("V4");
                    series.setName("V4Series");
                    chart.getData().add(series);
                }
                case 7 -> {
                    chart.setTitle("V5");
                    series.setName("V5Series");
                    chart.getData().add(series);
                }
                case 8 -> {
                    chart.setTitle("V6");
                    series.setName("V6Series");
                    chart.getData().add(series);
                }
                case 9 -> {
                    chart.setTitle("Avr");
                    series.setName("avrSeries");
                    chart.getData().add(series);
                }
                case 10 -> {
                    chart.setTitle("Avl");
                    series.setName("avlSeries");
                    chart.getData().add(series);
                }
                case 11 -> {
                    chart.setTitle("Avf");
                    series.setName("avfSeries");
                    chart.getData().add(series);
                }
            }
            chart.setAnimated(false);
            chart.setLegendVisible(false);
            chart.setCreateSymbols(false);
            chartList.add(chart);
            series.getNode().setStyle("-fx-stroke: black;-fx-stroke-width: 1px;");
            vbox.getChildren().add(chart);
        }

        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= UPDATE_INTERVAL_MS * 1_000_000) {
//                    addDataPoint();
//                    removeOldDataPoints();
                    lastUpdate = now;
                }
            }
        };
    }

    int j = 0;
    OnReceiveDataListenerFromModuleToUI onReceiveDataListenerFromModuleToUI = new OnReceiveDataListenerFromModuleToUI() {
        @Override
        public void usbOnDataReceive(String data) throws Exception {

//            System.out.println(data);
            String[] array = data.split(",");
            j = 0;


            for (int i = 0; i < 9; i++) {
                XYChart.Series<Number, Number> seriesTemp = seriesList.get(i);
                switch (seriesTemp.getName()) {
                    case "lead1Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[0])));
                    case "lead2Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[1])));
                    case "lead3Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[2])));
                    case "V1Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[3])));
                    case "V2Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[4])));
                    case "V3Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[5])));
                    case "V4Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[6])));
                    case "V5Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[7])));
                    case "V6Series" ->
                            seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.valueOf(array[8])));
                }
                if (seriesTemp.getName().equals("avlSeries"))
                    seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.parseDouble(array[0]) - Double.parseDouble(array[1])));
                else if (seriesTemp.getName().equals("avfSeries"))
                    seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.parseDouble(array[0]) - Double.parseDouble(array[1])));
                else if (seriesTemp.getName().equals("avrSeries"))
                    seriesTemp.getData().add(new XYChart.Data<>(dataCount, Double.parseDouble(array[0]) - Double.parseDouble(array[1])));

            }
            dataCount++;
//            if (dataCount > WINDOW_SIZE) {
//                for (XYChart.Series<Number, Number> series : seriesList) {
//                    ObservableList<XYChart.Data<Number, Number>> dataSeries = series.getData();
//                    if (dataSeries.size() > WINDOW_SIZE) {
//                        dataSeries.remove(0, dataSeries.size() - WINDOW_SIZE);
//                    }
//                }
//            }
//
//            if (dataCount > WINDOW_SIZE) {
//                double lowerBound = dataCount - WINDOW_SIZE;
//                double upperBound = dataCount - 1;
//
//                for (LineChart<Number, Number> chart : chartList) {
//                    chart.getXAxis().setAutoRanging(false);
//                    NumberAxis axis = (NumberAxis) chart.getXAxis();
//                    axis.setUpperBound(upperBound);
//                    axis.setLowerBound(lowerBound);
//                }
//            }
        }


        @Override
        public void usbAuthentication(String data) {

        }
    };

    private void addDataPoint() {

        double randomValue = random.nextDouble();

        for (XYChart.Series<Number, Number> series : seriesList) {
            series.getData().add(new XYChart.Data<>(dataCount, randomValue));
        }

        dataCount++;

        if (dataCount > WINDOW_SIZE) {
            double lowerBound = dataCount - WINDOW_SIZE;
            double upperBound = dataCount - 1;

            for (LineChart<Number, Number> chart : chartList) {
                chart.getXAxis().setAutoRanging(false);
                NumberAxis axis = (NumberAxis) chart.getXAxis();
                axis.setUpperBound(upperBound);
                axis.setLowerBound(lowerBound);
            }
        }
    }

    private void removeOldDataPoints() {
        if (dataCount > WINDOW_SIZE) {
            for (XYChart.Series<Number, Number> series : seriesList) {
                ObservableList<XYChart.Data<Number, Number>> data = series.getData();
                if (data.size() > WINDOW_SIZE) {
                    data.remove(0, data.size() - WINDOW_SIZE);
                }
            }
        }
    }

    public void menuItemCloseButton(ActionEvent actionEvent) {
    }

    public void menuItemDeleteButton(ActionEvent actionEvent) {
    }

    public void menuHelpAboutButton(ActionEvent actionEvent) {
    }

    public void menuStartLoggingButton(ActionEvent actionEvent) {

        timer.start();
        Thread portConnectionThread = new Thread(() -> {
            try {
                SpandanUsbCommunication.connectPort(mySerialPort);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        portConnectionThread.start();
        try {
            SpandanUsbCommunication.onReceiveDataListenerFromModuleToUI = onReceiveDataListenerFromModuleToUI;
            SpandanUsbCommunication.sendCommand("start");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuStopLoggingButton(ActionEvent actionEvent) {
        timer.stop();
    }
}
