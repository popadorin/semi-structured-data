package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.models.ProxyCommand;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Proxy {
    private final static Logger LOGGER = Logger.getLogger(Proxy.class);

    public static void main(String[] args) {
        try {
            DatagramSocket udpSocket = new DatagramSocket();

            // configurations
            final int mcPort = 12345;
            final String mcAddress = "230.1.1.1";
            final ProxyCommand COMMAND = ProxyCommand.GIVE_META_INFO;

            MulticastProxy multicastProxy = new MulticastProxy(udpSocket, mcAddress, mcPort);
            UnicastProxy unicastProxy = new UnicastProxy(udpSocket);

            LOGGER.info("Started multicast udp server...");

            boolean isStopped = false;
            while (!isStopped) {
                System.out.println("Choose command: (send, exit)");
                String userInput = new Scanner(System.in).nextLine();
                switch (userInput.trim().toLowerCase()) {
                    case "send":
                        byte[] message = COMMAND.name().getBytes();
                        multicastProxy.sendToNodes(message);

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
        } catch (UnknownHostException e) {
            LOGGER.error("UnknownHostException on: " + e.getMessage());
        } catch (SocketException se) {
            LOGGER.error("SocketException on: " + se.getMessage());
        } catch (IOException ioe) {
            LOGGER.error("IOException on: " + ioe.getMessage());
        }
    }
}
