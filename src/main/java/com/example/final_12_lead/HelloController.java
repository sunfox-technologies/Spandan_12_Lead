package com.example.final_12_lead;

import com.example.serialcommunicationmodule.OnReceiveDataListenerFromModuleToUI;
import com.example.serialcommunicationmodule.SpandanUsbCommunication;
import com.fazecast.jSerialComm.SerialPort;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {
    @FXML
    private LineChart<String, Number> l1;

    @FXML
    private LineChart<String, Number> l2;

    @FXML
    private LineChart<String, Number> l3;

    @FXML
    private Label loggingStatus;
    @FXML
    private CategoryAxis xAxisLead1;

    @FXML
    private CategoryAxis xAxisLead2;

    @FXML
    private CategoryAxis xAxisLead3;

    @FXML
    private CategoryAxis xAxisV1;

    @FXML
    private CategoryAxis xAxisV2;

    @FXML
    private CategoryAxis xAxisV3;

    @FXML
    private CategoryAxis xAxisV4;

    @FXML
    private CategoryAxis xAxisV5;

    @FXML
    private CategoryAxis xAxisV6;


    @FXML
    private LineChart<String,Number> v1;

    @FXML
    private LineChart<String,Number> v2;

    @FXML
    private LineChart<String,Number> v3;

    @FXML
    private LineChart<String,Number> v4;

    @FXML
    private LineChart<String,Number> v5;

    @FXML
    private LineChart<String,Number> v6;

    //    ProgressIndicator graphLoadingProgressIndicator;
    XYChart.Series<String, Number> graphDataSeriesV1 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesV2 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesV3 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesV4 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesV5 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesV6 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesLead1 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesLead2 = new XYChart.Series<>();
    XYChart.Series<String, Number> graphDataSeriesLead3 = new XYChart.Series<>();
    int windowSize = 500;

    SerialPort mySerialPort;
    public static BooleanProperty deviceDetectedFromThread = new SimpleBooleanProperty(false);
    public HelloController() {
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
    }
    Boolean isGraphActive = false;
    ExecutorService executorService = Executors.newFixedThreadPool(8);
    ArrayList<String> bufferArray = new ArrayList<>();

    static int graphPointsIncrementer = 0;
    @FXML
    void menuHelpAboutButton() {

        System.out.println("menuHelpAboutButtonClicked");

    }

    @FXML
    void menuItemCloseButton() {

        System.out.println("menuItemCloseButtonClicked");
        System.exit(0);

    }

    @FXML
    void menuItemDeleteButton() {
        System.out.println("menuItemDeleteButtonClicked");
    }

    @FXML
    void menuStartLoggingButton() {
        if (deviceDetectedFromThread.getValue() && !isGraphActive) {
            Thread portConnectionThread = new Thread(() -> {
                try {
                    SpandanUsbCommunication.connectPort(mySerialPort);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            portConnectionThread.start();
//            graphLoadingProgressIndicator = new ProgressIndicator();
//            Platform.runLater(() -> {
//                JFXSnackbar notifyingSnackbar = new JFXSnackbar(paneContainingGraph1);
//                notifyingSnackbar.getStylesheets().add("com/example/demo/style.css");
//                notifyingSnackbar.show("Graph Loading Please Wait...........", 2500);
//                notifyingSnackbar.lookup(".jfx-snackbar-toast").setStyle("-fx-text-fill: black; -fx-font-size: 18;");
//            });
            Thread progressIndicatorThread = new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                graphLoadingProgressIndicator = new ProgressIndicator(0.025);
//                Platform.runLater(() -> {
//                    graphLoadingProgressIndicator.getStylesheets().add("com/example/demo/style.css");
//                    graphLoadingProgressIndicator.setMinWidth(60);
//                    graphLoadingProgressIndicator.setMinHeight(60);
//                    graphLoadingProgressIndicator.setStyle("-fx-progress-color: green;");
//                    paneContainingGraph1.getChildren().add(graphLoadingProgressIndicator);
//                });
//                double graphLoadingProgress = 0.025;
//                while (graphLoadingProgress <= 1) {
//                    double finalGraphLoadingProgress = graphLoadingProgress;
//                    Platform.runLater(() -> graphLoadingProgressIndicator.setProgress(finalGraphLoadingProgress));
//                    graphLoadingProgress += 0.025;
//                    try {
//                        Thread.sleep(40);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                graphLoadingProgressIndicator.setVisible(false);
//                Platform.runLater(() -> {
//                    paneContainingGraph1.getChildren().remove(graphLoadingProgressIndicator);
//                });
                List<String> xAxisCategoriesList = IntStream.range(0, windowSize)
                        .mapToObj(String::valueOf).toList();
                String[] xAxisCategories = xAxisCategoriesList.toArray(new String[0]);
                try {
                    xAxisV1.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisV2.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisV3.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisV4.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisV5.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisV6.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisLead1.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisLead2.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
                    xAxisLead3.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(xAxisCategories)));
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
            v1.getData().add(graphDataSeriesV1);
            v2.getData().add(graphDataSeriesV2);
            v3.getData().add(graphDataSeriesV3);
            v4.getData().add(graphDataSeriesV4);
            v5.getData().add(graphDataSeriesV5);
            v6.getData().add(graphDataSeriesV6);
            l1.getData().add(graphDataSeriesLead1);
            l2.getData().add(graphDataSeriesLead2);
            l3.getData().add(graphDataSeriesLead3);
            Platform.runLater(() -> {
                graphDataSeriesV1.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeriesV2.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeriesV3.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeriesV4.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeriesV5.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeriesV6.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeriesLead1.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
                graphDataSeriesLead2.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;" + "-fx-stroke-width: 1px");
            });
            Thread thread = new Thread(() -> {
                OnReceiveDataListenerFromModuleToUI onReceiveDataListenerFromModuleToUI = new OnReceiveDataListenerFromModuleToUI() {
                    @Override
                    public void usbOnDataReceive(String dataFromSerialPort) throws InterruptedException {
                        if (dataFromSerialPort.contains("error")) {
                            isGraphActive = false;

                            System.out.println(dataFromSerialPort);
//
                        }
                        if (dataFromSerialPort.equals("Port Closed")) {
                            isGraphActive = false;
                        } else if (isGraphActive && deviceDetectedFromThread.getValue()) {
                            String[] arrayList = dataFromSerialPort.split(",");
//                            System.out.println(dataFromSerialPort);
                            bufferArray.add(Arrays.toString(arrayList));
                            Platform.runLater(() -> {
                                if (graphDataSeriesV1.getData().size() >= windowSize) {
                                    graphDataSeriesV1.getData().remove(0);
                                    graphDataSeriesV2.getData().remove(0);
                                    graphDataSeriesV3.getData().remove(0);
                                    graphDataSeriesV4.getData().remove(0);
                                    graphDataSeriesV5.getData().remove(0);
                                    graphDataSeriesV6.getData().remove(0);
                                    graphDataSeriesLead1.getData().remove(0);
                                    graphDataSeriesLead2.getData().remove(0);
                                    graphDataSeriesLead3.getData().remove(0);
                                    xAxisLead1.setAutoRanging(true);
                                    xAxisLead2.setAutoRanging(true);
                                    xAxisLead3.setAutoRanging(true);
                                    xAxisV1.setAutoRanging(true);
                                    xAxisV2.setAutoRanging(true);
                                    xAxisV3.setAutoRanging(true);
                                    xAxisV4.setAutoRanging(true);
                                    xAxisV5.setAutoRanging(true);
                                    xAxisV6.setAutoRanging(true);

                                }
                            });
                            if (bufferArray.size() == 10) {
                                executorService.submit(new LineChartUpdater(graphDataSeriesV1, graphDataSeriesV2, graphDataSeriesV3, graphDataSeriesV4, graphDataSeriesV5, graphDataSeriesV6, graphDataSeriesLead1, graphDataSeriesLead2,graphDataSeriesLead3,arrayList, graphPointsIncrementer));
                                graphPointsIncrementer++;
                                bufferArray.clear();
                            }

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
    void menuStopLoggingButton() {
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
    void clearGraph(){
        graphPointsIncrementer = 0;
        Platform.runLater(() -> {
            v1.getData().remove(graphDataSeriesV1);
            v2.getData().remove(graphDataSeriesV2);
            v3.getData().remove(graphDataSeriesV3);
            v4.getData().remove(graphDataSeriesV4);
            v5.getData().remove(graphDataSeriesV5);
            v6.getData().remove(graphDataSeriesV6);
            l1.getData().remove(graphDataSeriesLead1);
            l2.getData().remove(graphDataSeriesLead2);
            l3.getData().remove(graphDataSeriesLead3);

            graphDataSeriesV1.getData().clear();
            graphDataSeriesV2.getData().clear();
            graphDataSeriesV3.getData().clear();
            graphDataSeriesV4.getData().clear();
            graphDataSeriesV5.getData().clear();
            graphDataSeriesV6.getData().clear();
            graphDataSeriesLead1.getData().clear();
            graphDataSeriesLead2.getData().clear();
            graphDataSeriesLead3.getData().clear();
        });
    }





}
