package com.example.final_12_lead;

import com.example.serialcommunicationmodule.OnReceiveDataListenerFromModuleToUI;
import com.example.serialcommunicationmodule.SpandanUsbCommunication;
import com.fazecast.jSerialComm.SerialPort;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class MainController3 implements Initializable {
    @FXML
    private LineChart<String, Number> chart1;
    @FXML
    private LineChart<String, Number> chart2;
    @FXML
    private LineChart<String, Number> chart3;
    @FXML
    private LineChart<String, Number> chart4;
    @FXML
    private LineChart<String, Number> chart5;
    @FXML
    private LineChart<String, Number> chart6;
    @FXML
    private LineChart<String, Number> chart7;
    @FXML
    private LineChart<String, Number> chart8;
    @FXML
    private Label deviceDetectionLabel;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private CategoryAxis xAxis2;
    @FXML
    private CategoryAxis xAxis3;
    @FXML
    private CategoryAxis xAxis4;
    @FXML
    private CategoryAxis xAxis5;
    @FXML
    private CategoryAxis xAxis6;
    @FXML
    private CategoryAxis xAxis7;
    @FXML
    private CategoryAxis xAxis8;
    SerialPort mySerialPort;
    static int graphPointsIncrementer = 0;
    ProgressIndicator graphLoadingProgressIndicator;
    XYChart.Series<String, Number> graphDataSeries1 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries2 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries3 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries4 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries5 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries6 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries7 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries8 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeries9 = new XYChart.Series<>();
    int windowSize = 300;
    public static BooleanProperty deviceDetectedFromThread = new SimpleBooleanProperty(false);
    @FXML
    StackPane paneContainingGraph1 = new StackPane();

    public MainController3() {
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
                    deviceDetectionLabel.setStyle("-fx-background-color : green");
                    deviceDetectionLabel.setText("Device Connected");
                });
            } else {
                Platform.runLater(() -> {
                    deviceDetectionLabel.setStyle("-fx-background-color : red");
                    deviceDetectionLabel.setText("Device Disconnected");
                    isGraphActive = false;
                    clearGraph();
                });
            }
        });
    }
    static  int i = 0;

    Boolean isGraphActive = false;
    ExecutorService executorService = Executors.newFixedThreadPool(8);
    ArrayList<String> bufferArray = new ArrayList<>();

    @FXML
    void startGraphAction() {
        if (deviceDetectedFromThread.getValue() && !isGraphActive) {
            Thread portConnectionThread = new Thread(() -> {
                try {
                    SpandanUsbCommunication.connectPort(mySerialPort);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            portConnectionThread.start();
            graphLoadingProgressIndicator = new ProgressIndicator();
            Platform.runLater(() -> {
                JFXSnackbar notifyingSnackbar = new JFXSnackbar(paneContainingGraph1);
                notifyingSnackbar.getStylesheets().add("com/example/demo/style.css");
                notifyingSnackbar.show("Graph Loading Please Wait...........", 2500);
                notifyingSnackbar.lookup(".jfx-snackbar-toast").setStyle("-fx-text-fill: black; -fx-font-size: 18;");
            });
            Thread progressIndicatorThread = new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                graphLoadingProgressIndicator = new ProgressIndicator(0.025);
                Platform.runLater(() -> {
                    graphLoadingProgressIndicator.getStylesheets().add("com/example/demo/style.css");
                    graphLoadingProgressIndicator.setMinWidth(60);
                    graphLoadingProgressIndicator.setMinHeight(60);
                    graphLoadingProgressIndicator.setStyle("-fx-progress-color: green;");
                    paneContainingGraph1.getChildren().add(graphLoadingProgressIndicator);
                });
                double graphLoadingProgress = 0.025;
                while (graphLoadingProgress <= 1) {
                    double finalGraphLoadingProgress = graphLoadingProgress;
                    Platform.runLater(() -> graphLoadingProgressIndicator.setProgress(finalGraphLoadingProgress));
                    graphLoadingProgress += 0.025;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                graphLoadingProgressIndicator.setVisible(false);
                Platform.runLater(() -> {
                    paneContainingGraph1.getChildren().remove(graphLoadingProgressIndicator);
                });
                List<String> xAxisCategoriesList = IntStream.range(0, windowSize)
                        .mapToObj(String::valueOf).toList();
                String[] xAxisCategories = xAxisCategoriesList.toArray(new String[0]);
                try {
                    xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxis2.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxis3.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxis4.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxis5.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxis6.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxis7.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxis8.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread thread = new Thread(() -> {
                    try {
                        SpandanUsbCommunication.sendCommand("start");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                thread.start();
            });
            progressIndicatorThread.start();
            isGraphActive = true;
            Platform.runLater(() -> {
                chart1.getData().add(graphDataSeries1);
                chart2.getData().add(graphDataSeries2);
                chart3.getData().add(graphDataSeries3);
                chart4.getData().add(graphDataSeries4);
                chart5.getData().add(graphDataSeries5);
                chart6.getData().add(graphDataSeries6);
                chart7.getData().add(graphDataSeries7);
                chart8.getData().add(graphDataSeries8);
                graphDataSeries1.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeries2.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeries3.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeries4.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeries5.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeries6.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeries7.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeries8.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
            });
            Thread thread = new Thread(() -> {
                OnReceiveDataListenerFromModuleToUI onReceiveDataListenerFromModuleToUI = new OnReceiveDataListenerFromModuleToUI() {
                    @Override
                    public void usbOnDataReceive(String dataFromSerialPort) throws InterruptedException {
                        if (dataFromSerialPort.contains("error")) {
                            isGraphActive = false;
//                            Platform.runLater(() -> {
//                            ecgGraphLineChart.setVisible(false);
//                                paneContainingGraph.setVisible(false);
//                            System.out.println(dataFromSerialPort);
//                            });
//                            Platform.runLater(() -> {
//                                JFXSnackbar snackbar = new JFXSnackbar(paneContainingGraph);
//                                snackbar.getStylesheets().add("com/example/demo/style.css");
//                                snackbar.show("Something Went Wrong Please Try Again", 1000);
//                                snackbar.lookup(".jfx-snackbar-toast").setStyle("-fx-text-fill: red;");
//                            });
                        }
                        if (dataFromSerialPort.equals("Port Closed")) {
                            isGraphActive = false;
                        } else if (isGraphActive && deviceDetectedFromThread.getValue()) {

                            String[] arrayList = dataFromSerialPort.split(",");
                            graphDataSeries1.getData().add(new XYChart.Data<>(String.valueOf(i++), Double.valueOf(arrayList[2])));

//                            System.out.println(arrayList[2]);
//                            bufferArray.add(Arrays.toString(arrayList));
//                            Platform.runLater(() -> {
//                                if (graphDataSeries1.getData().size() >= windowSize) {
//                                    graphDataSeries1.getData().remove(0);
//                                    graphDataSeries2.getData().remove(0);
//                                    graphDataSeries3.getData().remove(0);
//                                    graphDataSeries4.getData().remove(0);
//                                    graphDataSeries5.getData().remove(0);
//                                    graphDataSeries6.getData().remove(0);
//                                    graphDataSeries7.getData().remove(0);
//                                    graphDataSeries8.getData().remove(0);
//                                    xAxis.setAutoRanging(true);
//                                    xAxis2.setAutoRanging(true);
//                                    xAxis3.setAutoRanging(true);
//                                    xAxis4.setAutoRanging(true);
//                                    xAxis5.setAutoRanging(true);
//                                    xAxis6.setAutoRanging(true);
//                                    xAxis7.setAutoRanging(true);
//                                    xAxis8.setAutoRanging(true);
//                                }
//                            });
//                            if (bufferArray.size() == 15) {
//                                executorService.submit(new LineChartUpdater(graphDataSeries1, graphDataSeries2, graphDataSeries3, graphDataSeries4, graphDataSeries5, graphDataSeries6, graphDataSeries7, graphDataSeries8, graphDataSeries9, arrayList, graphPointsIncrementer));
//                                graphPointsIncrementer++;
//                                bufferArray.clear();
//                            }

                        } else {
                            SpandanUsbCommunication.sendCommand("stop");
                            isGraphActive = false;
                        }
                    }

                    @Override
                    public void usbAuthentication(String data) {

                    }
                };
                try {
                    SpandanUsbCommunication.onReceiveDataListenerFromModuleToUI = onReceiveDataListenerFromModuleToUI;
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();


        } else if (!isGraphActive) Platform.runLater(() -> {
            JFXSnackbar snackbar = new JFXSnackbar();
//            snackbar.getStylesheets().add("com/example/demo/style.css");
            snackbar.show("Device Not FOUND", 750);
        });
    }

    @FXML
    void stopGraphAction() {
        isGraphActive = false;

        graphPointsIncrementer = 0;
        Thread thread = new Thread(() -> {
            try {
                SpandanUsbCommunication.sendCommand("stop");
                executorService.isTerminated();
                clearGraph();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();


    }

    void clearGraph() {
        graphPointsIncrementer = 0;
        Platform.runLater(() -> {
            chart1.getData().remove(graphDataSeries1);
            chart2.getData().remove(graphDataSeries2);
            chart3.getData().remove(graphDataSeries3);
            chart4.getData().remove(graphDataSeries4);
            chart5.getData().remove(graphDataSeries5);
            chart6.getData().remove(graphDataSeries6);
            chart7.getData().remove(graphDataSeries7);
            chart8.getData().remove(graphDataSeries8);

            graphDataSeries1.getData().clear();
            graphDataSeries2.getData().clear();
            graphDataSeries3.getData().clear();
            graphDataSeries4.getData().clear();
            graphDataSeries5.getData().clear();
            graphDataSeries6.getData().clear();
            graphDataSeries7.getData().clear();
            graphDataSeries8.getData().clear();
        });
    }

    @FXML
    void closeWindow() {
        Stage stage = (Stage) deviceDetectionLabel.getScene().getWindow();
        // do what you have to do
        stage.close();

    }

}

