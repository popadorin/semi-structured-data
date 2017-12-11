package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.models.MetaInformation;
import com.dorin.pad.lab2.models.NodeInfo;
import com.dorin.pad.lab2.models.ProxyCommand;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class UnicastProxy {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private final Gson gson = new Gson();
    private byte[] receiveData = new byte[1024];
    private DatagramSocket udpSocket;

    public UnicastProxy(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
    }

    public void sendToNode(ProxyCommand command, InetAddress address, int port) throws IOException {
        LOGGER.info("sentToNode to address: " + address + ", port: " +  port);
        byte[] sendData = command.name().getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);

        udpSocket.send(sendPacket);
    }

    public List<NodeInfo> readFromNodes(int timeout) throws IOException {
        List<NodeInfo> nodeInfos = new ArrayList<>();
        udpSocket.setSoTimeout(timeout);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeout;

        while (System.currentTimeMillis() < endTime) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpSocket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(),
                        receivePacket.getOffset(), receivePacket.getLength());

                // spoate de facut validare aici

                MetaInformation metaInformation = gson.fromJson(receivedMessage, MetaInformation.class);

                nodeInfos.add(new NodeInfo(receivePacket.getAddress(), receivePacket.getPort(), metaInformation));
            } catch (SocketTimeoutException e) {
                LOGGER.error("udp socket timeout");
            }
        }

        LOGGER.info("EXIT unicast handle, time out");

        return nodeInfos;

    }

}
