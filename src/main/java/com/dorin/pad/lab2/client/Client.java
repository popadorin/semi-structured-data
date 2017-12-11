package com.dorin.pad.lab2.client;

import com.dorin.pad.lab2.models.ClientCommand;
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
                System.out.println("Choose command:");
                System.out.println("1. Get employees");
                System.out.println("2. Exit");

                ClientCommand command;
                Integer userChoice = Integer.parseInt(new Scanner(System.in).nextLine().trim());
                switch (userChoice) {
                    case 1:
                        command = ClientCommand.GIVE_EMPLOYEE;
                        byte[] toSend = command.name().getBytes();
                        transport.sendToProxy(toSend);

                        // wait to result and print it after it comes
                        byte[] toReceive = transport.readFromProxy();
                        String fromProxy = new String(toReceive);
                        LOGGER.info("From proxy: " + fromProxy);
                        break;
                    case 2:
                        command = ClientCommand.EXIT;
                        byte[] finalMessage = command.name().getBytes();
                        transport.sendToProxy(finalMessage);

                        isStopped = true;
                        transport.close();
                        break;
                    default:
                        LOGGER.error("No such command");
                        break;
                }

                LOGGER.info("Client stopped");
            }

        } catch (IOException ioe) {
            LOGGER.error("IOExceptiono on: " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("ClassNotFoundException on: " + cnfe.getMessage());
        }

    }
}
