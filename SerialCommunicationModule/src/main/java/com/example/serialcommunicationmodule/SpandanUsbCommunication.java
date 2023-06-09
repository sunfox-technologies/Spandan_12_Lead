package com.example.serialcommunicationmodule;


import com.fazecast.jSerialComm.SerialPort;

public class SpandanUsbCommunication {
    private static String bufferData = "";

    public static OnReceiveDataListenerFromModuleToUI onReceiveDataListenerFromModuleToUI;

    public SpandanUsbCommunication(OnReceiveDataListenerFromModuleToUI onReceiveDataListenerFromModuleToUI) {
        SpandanUsbCommunication.onReceiveDataListenerFromModuleToUI = onReceiveDataListenerFromModuleToUI;
    }
    static OnDataReceiveListenerFromThread<String> onDataReceiveListenerFromThread = new OnDataReceiveListenerFromThread<String>() {
        @Override
        public void onDataReceived(String data) throws Exception {
            performOperationOnBytes(data);
        }
        @Override
        public void usbAuthentication(String data) {
            onReceiveDataListenerFromModuleToUI.usbAuthentication(data);
        }
    };

    public static void performOperationOnBytes(String data) throws Exception {
        if (data.equals("error")) {
            onReceiveDataListenerFromModuleToUI.usbOnDataReceive(data);
            bufferData = "";
            return;
        }
        if (data.equals("Port Closed")) {
            onReceiveDataListenerFromModuleToUI.usbOnDataReceive(data);
            bufferData = "";
            return;
        }
        bufferData += data;
        String tempData = "";
        try {
            while (bufferData.toUpperCase().contains("\n")) {
//                System.out.println(bufferData);


                tempData = bufferData.substring(0, bufferData.toUpperCase().indexOf("\n"));

                String validData = tempData;
                validData = validData.replace("\n", "");
                validData = validData.replace("\r", "");

//                System.out.print(validData);
                onReceiveDataListenerFromModuleToUI.usbOnDataReceive(validData);
//                System.out.println(validData);

                bufferData = bufferData.substring(bufferData.toUpperCase().indexOf("\n") + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static SerialCommunication serialCom = new SerialCommunication(onDataReceiveListenerFromThread);

    private static void startTransmission() {
        serialCom.startTransmission("d");
        bufferData = "";
    }

    private static void stopTransmission() {
        serialCom.stopTransmission("ESC");
    }

    public static void sendCommand(String command) throws InterruptedException {
        {
            switch (command) {
                case "start" -> {
                    startTransmission();
                }
                case "stop" -> stopTransmission();
            }
        }
    }

    public static void connectPort(SerialPort mySerialPort) throws InterruptedException {
        serialCom.connectPort(mySerialPort);
    }
}