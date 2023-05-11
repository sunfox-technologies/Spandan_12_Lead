package com.example.serialcommunicationmodule;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;

public class SerialCommunicationThread extends Thread {
    private final String spandanInputCommand;
    private final SerialPort mySerialPort;
    private OnDataReceiveListenerFromThread<String> onDataReceiveListenerFromThread;

    public SerialCommunicationThread(SerialPort mySerialPort, String spandanInputCommand) {
        this.mySerialPort = mySerialPort;
        this.spandanInputCommand = spandanInputCommand;
    }

    synchronized public void run() {
        try {
            if (!mySerialPort.isOpen()) {
                onDataReceiveListenerFromThread.onDataReceived("error1");
                return;
            }
            byte[] WriteByte = spandanInputCommand.getBytes();
            mySerialPort.writeBytes(WriteByte, 1);
            if (spandanInputCommand.equals("ESC")) {
//                System.out.println("Sent 0");
                mySerialPort.flushIOBuffers();
                mySerialPort.closePort();
//                Thread.sleep(1000);
                if (!mySerialPort.isOpen())
                    System.out.println("Port Closed");
                onDataReceiveListenerFromThread.onDataReceived("Port Closed");
                return;
            }
        } catch (Exception e) {
            System.out.println(
                    "Exception in thread"
            );
            return;
        }
        if (spandanInputCommand.equals("c") ) {

//            System.out.println("Starting writing");
            byte[] readBuffer = new byte[64];

//            System.out.println(mySerialPort.getLastErrorLocation());
            try {
                while (mySerialPort.readBytes(readBuffer, readBuffer.length) != 0) {
                    if (mySerialPort.isOpen()) {
                        String s = new String(readBuffer, StandardCharsets.UTF_8);
//                        System.out.println(s);
                        //                            System.out.println(mySerialPort.getLastErrorLocation());
                        if (mySerialPort.getLastErrorLocation() == 1024 || mySerialPort.getLastErrorLocation() == 1029) {
                            mySerialPort.closePort();
                            onDataReceiveListenerFromThread.onDataReceived("error in loop");
                            break;
//                            System.out.println("Error occurred");
//                            return;
                        }
                        System.out.print(s);

                        onDataReceiveListenerFromThread.onDataReceived(s);
//                        Thread.sleep(1000);
//
                    } else return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Transmission finished writing" + spandanInputCommand);
        }
    }

    public void setOnDataReceiveListener(OnDataReceiveListenerFromThread<String> onDataReceiveListenerFromThread) {
        this.onDataReceiveListenerFromThread = onDataReceiveListenerFromThread;

//        }

    }
}