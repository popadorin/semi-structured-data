package com.dorin.pad.lab2.client;

import com.dorin.pad.lab2.configurations.Configurations;
import com.dorin.pad.lab2.models.ClientCommand;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Scanner;

public class Client {
    private final static Logger LOGGER = Logger.getLogger(Client.class);

    public static void main(String[] args) {
        try {
            TcpClient transport = new TcpClientImpl(Configurations.proxyName, Configurations.clientProxyPort);

            boolean isStopped = false;
            while (!isStopped) {
                System.out.println("Choose command:");
                System.out.println("1. Get employees");
                System.out.println("2. Exit");

                Integer userChoice = Integer.parseInt(new Scanner(System.in).nextLine().trim());
                switch (userChoice) {
                    case 1:
                        treatGetEmployee(transport);
                        break;
                    case 2:
                        treatExit(transport);
                        isStopped = true;
                        break;
                    default:
                        LOGGER.error("No such command");
                        break;
                }

                LOGGER.info("Client stopped");
            }

        } catch (IOException ioe) {
            LOGGER.error("IOException on: " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("ClassNotFoundException on: " + cnfe.getMessage());
        } catch (JsonSyntaxException ex) {
            LOGGER.error("Gson exception, ex: " + ex.getMessage());
        }

    }

    private static void treatExit(TcpClient transport) throws IOException {
        ClientCommand command = ClientCommand.EXIT;
        byte[] finalMessage = command.name().getBytes();
        transport.sendToProxy(finalMessage);
        transport.close();
    }

    private static void treatGetEmployee(TcpClient transport) throws IOException, ClassNotFoundException {
        ClientCommand command = ClientCommand.GET_EMPLOYEE;
        byte[] toSend = command.name().getBytes();
        transport.sendToProxy(toSend);

        // wait to result and print it after it comes
        LOGGER.info("wait for response from proxy...");
        byte[] toReceive = transport.readFromProxy();
        String fromProxy = new String(toReceive);
        LOGGER.info("From proxy: " + fromProxy);
    }

}
