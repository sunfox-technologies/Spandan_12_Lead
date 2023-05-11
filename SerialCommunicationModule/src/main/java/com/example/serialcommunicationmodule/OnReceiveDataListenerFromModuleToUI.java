package com.example.serialcommunicationmodule;

public interface
OnReceiveDataListenerFromModuleToUI {
    void usbOnDataReceive(String data) throws Exception;
    void usbAuthentication(String data);
}
