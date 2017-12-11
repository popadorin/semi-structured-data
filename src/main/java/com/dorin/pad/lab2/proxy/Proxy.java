package com.dorin.pad.lab2.proxy;

import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Proxy {
    private final static Logger LOGGER = Logger.getLogger(Proxy.class);

    public static void main(String[] args) throws Exception {
        DatagramSocket udpSocket = new DatagramSocket();
        UnicastProxy unicastProxy = new UnicastProxy(udpSocket);
        ////////////////
        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
        String commandToGetData = "give";

        LOGGER.info("Started multicast udp server...");

        boolean isStopped = false;
        while (!isStopped) {
            System.out.println("Choose command: (send, exit)");
            String userInput = new Scanner(System.in).nextLine();
            switch (userInput.trim().toLowerCase()) {
                case "send":
                    byte[] message = commandToGetData.getBytes();
                    DatagramPacket packet = new DatagramPacket(message, message.length, mcIPAddress, mcPort);
                    udpSocket.send(packet);

                    unicastProxy.listen();
                    break;
                case "exit":
                    isStopped = true;
                    udpSocket.close();
                    break;
                default:
                    LOGGER.error("No such command");
                    break;
            }

        }
    }
}
