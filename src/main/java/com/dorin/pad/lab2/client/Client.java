package com.dorin.pad.lab2.client;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Scanner;

public class Client {
    private final static Logger LOGGER = Logger.getLogger(Client.class);

    public static void main(String[] args) {
        try {
            String proxyName = "localhost";
            int proxyPort = 7777;
            TcpClient transport = new TcpClientImpl(proxyName, proxyPort);

            boolean isStopped = false;
            while (!isStopped) {
                System.out.println("Type message to proxy:");
                String userInput = new Scanner(System.in).nextLine();
                byte[] toSend = userInput.getBytes();

                transport.sendToProxy(toSend);

                byte[] toReceive = transport.readFromProxy();
                String fromProxy = new String(toReceive);
                LOGGER.info("From proxy: " + fromProxy);

                if (userInput.trim().toLowerCase().equals("exit")) {
                    isStopped = true;
                    transport.close();
                }
            }

        } catch (IOException ioe) {
            LOGGER.error("IOExceptiono on: " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("ClassNotFoundException on: " + cnfe.getMessage());
        }

    }
}
