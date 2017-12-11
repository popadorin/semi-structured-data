package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.models.Employee;
import com.dorin.pad.lab2.models.MetaInformation;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UnicastNode {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private final Gson gson = new Gson();
    private DatagramSocket clientSocket;
    private InetAddress address;
    private int port;

    public UnicastNode(int port) throws SocketException {
        clientSocket = new DatagramSocket(port);
    }

    public void sendToProxy(MetaInformation metaInformation) throws IOException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error("THREAD sleep error");
        }

        String serializedEmployee = gson.toJson(metaInformation);
        byte[] sendData = serializedEmployee.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);

        clientSocket.send(sendPacket);
        LOGGER.info("message successfully sent!");
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
