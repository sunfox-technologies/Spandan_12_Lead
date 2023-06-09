package com.example.final_12_lead;

import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.util.Arrays;

import static java.lang.Math.sqrt;

class LineChartUpdater implements Runnable {
    private XYChart.Series<String, Number> graphDataSeriesV1;
    private XYChart.Series<String, Number> graphDataSeriesV2;
    private XYChart.Series<String, Number> graphDataSeriesV3;
    private XYChart.Series<String, Number> graphDataSeriesV4;
    private XYChart.Series<String, Number> graphDataSeriesV5;
    private XYChart.Series<String, Number> graphDataSeriesV6;
    private XYChart.Series<String, Number> graphDataSeriesLead1;
    private XYChart.Series<String, Number> graphDataSeriesLead2;
    private XYChart.Series<String, Number> graphDataSeriesLead3;
    private XYChart.Series<String, Number> graphDataSeriesAvf;
    private XYChart.Series<String, Number> graphDataSeriesAvl;
    private XYChart.Series<String, Number> graphDataSeriesAvr;
    private String[] arrayList;
    int graphPointIncrementer;

    public LineChartUpdater(XYChart.Series<String, Number> graphDataSeriesV1, XYChart.Series<String, Number> graphDataSeriesV2, XYChart.Series<String, Number> graphDataSeriesV3, XYChart.Series<String, Number> graphDataSeriesV4, XYChart.Series<String, Number> graphDataSeriesV5, XYChart.Series<String, Number> graphDataSeriesV6, XYChart.Series<String, Number> graphDataSeriesLead1, XYChart.Series<String, Number> graphDataSeries8, XYChart.Series<String, Number> graphDataSeriesLead3,XYChart.Series<String, Number> graphDataSeriesAvf,XYChart.Series<String, Number> graphDataSeriesAvl,XYChart.Series<String, Number> graphDataSeriesAvr, String[] arrayList, int graphPointsIncrementer) {
        this.graphDataSeriesV1 = graphDataSeriesV1;
        this.graphDataSeriesV2 = graphDataSeriesV2;
        this.graphDataSeriesV3 = graphDataSeriesV3;
        this.graphDataSeriesV4 = graphDataSeriesV4;
        this.graphDataSeriesV5 = graphDataSeriesV5;
        this.graphDataSeriesV6 = graphDataSeriesV6;
        this.graphDataSeriesLead1 = graphDataSeriesLead1;
        this.graphDataSeriesLead2 = graphDataSeries8;
        this.graphDataSeriesLead3 = graphDataSeriesLead3;
        this.graphDataSeriesAvf=graphDataSeriesAvf;
        this.graphDataSeriesAvl=graphDataSeriesAvl;
        this.graphDataSeriesAvr=graphDataSeriesAvr;
        this.arrayList = arrayList;
        this.graphPointIncrementer = graphPointsIncrementer;
    }

    @Override
    public void run() {
        Arrays.stream(arrayList).toArray();
//        System.out.println(Arrays.toString(Arrays.stream(arrayList).toArray()));
        Platform.runLater(() -> {
//            System.out.println(arrayList[5]);
//            for (int i = 2; i < arrayList.length - 1; ) {
                graphDataSeriesLead1.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[0])));
                graphDataSeriesLead2.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[1])));
                graphDataSeriesLead3.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[2])));
                graphDataSeriesV1.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[3])));
                graphDataSeriesV2.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[4])));
                graphDataSeriesV3.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[5])));
                graphDataSeriesV4.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[6])));
                graphDataSeriesV5.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[7])));
//                graphDataSeriesV6.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), Double.valueOf(arrayList[8])));
                graphDataSeriesAvl.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), (( Double.valueOf(arrayList[1]) - Double.valueOf(arrayList[0])))/sqrt(3)));
                graphDataSeriesAvr.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer), (( Double.valueOf(arrayList[0]) - Double.valueOf(arrayList[1]))*2.0)/sqrt(3)));
                graphDataSeriesAvf.getData().add(new XYChart.Data<>(String.valueOf(graphPointIncrementer),(( Double.valueOf(arrayList[1]) - Double.valueOf(arrayList[0]))*2.0)/sqrt(3)));
//            }
        });

    }
}
