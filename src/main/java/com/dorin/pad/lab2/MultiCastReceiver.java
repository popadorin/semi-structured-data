package com.dorin.pad.lab2;

import com.dorin.pad.lab2.models.Employee;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class MultiCastReceiver {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private final Gson gson = new Gson();
    private DatagramSocket clientSocket;
    private InetAddress unicastAddress;
    private int unicastPort;
    private Employee employee;

    public MultiCastReceiver(int port, Employee employee) throws SocketException {
        clientSocket = new DatagramSocket(port);
        this.employee = employee;
    }

    public void listen() throws IOException {
        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        MulticastSocket mcSocket = new MulticastSocket(mcPort);
        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);

        LOGGER.info("Multicast Receiver running at:" + mcSocket.getLocalSocketAddress());
        mcSocket.joinGroup(mcIPAddress);


        boolean isStopped = false;
        while (!isStopped) {
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            LOGGER.info("Waiting for a  multicast message...");

            mcSocket.receive(packet);

            unicastAddress = packet.getAddress();
            unicastPort = packet.getPort();

            String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

            LOGGER.info("Received message: " + message);

            if (message.trim().toLowerCase().equals("give")) {
                sendToProxy(employee);
            }

            if (message.trim().toLowerCase().equals("exit")) {
                isStopped = true;
                mcSocket.leaveGroup(mcIPAddress);
                mcSocket.close();
            }

        }
    }

    private void sendToProxy(Employee employee) throws IOException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error("THREAD sleep error");
        }

        String serializedEmployee = gson.toJson(employee);
        byte[] sendData = serializedEmployee.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, unicastAddress, unicastPort);

        clientSocket.send(sendPacket);
        LOGGER.info("message successfully sent!");
    }

}
