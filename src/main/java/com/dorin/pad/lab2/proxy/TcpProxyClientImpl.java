package com.dorin.pad.lab2.proxy;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpProxyClientImpl implements TcpProxyClient {
    private final static Logger LOGGER = Logger.getLogger(TcpProxyClientImpl.class);
    private ServerSocket server;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public TcpProxyClientImpl(int port) throws IOException {
        server = new ServerSocket(port);
        socket = server.accept(); // locking, waiting for socket to connect

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        LOGGER.info("Proxy TCP started...");

    }

    @Override
    public byte[] readFromClient() throws IOException, ClassNotFoundException {
        return (byte[]) objectInputStream.readObject();
    }

    @Override
    public void sendToClient(byte[] object) throws IOException {
        objectOutputStream.writeObject(object);
    }

    @Override
    public void close() {

    }
}
