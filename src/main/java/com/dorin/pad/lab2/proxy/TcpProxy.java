package com.dorin.pad.lab2.proxy;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpProxy {
    private final static Logger LOGGER = Logger.getLogger(TcpProxy.class);
    private static int port = 7777;
    private static ServerSocket server;
    private static Socket socket;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        socket = server.accept(); // locking, waiting for socket to connect

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        LOGGER.info("Proxy TCP started...");

        boolean isStopped = false;
        while (!isStopped) {
            byte[] toReceive = (byte[]) objectInputStream.readObject();
            String fromProxy = new String(toReceive);
            LOGGER.info("From client: " + fromProxy);

            System.out.println("Type message to client:");
            String userInput = new Scanner(System.in).nextLine();
            byte[] toSend = userInput.getBytes();

            objectOutputStream.writeObject(toSend);

        }

    }
}
