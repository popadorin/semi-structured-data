package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.models.MetaInformation;
import com.dorin.pad.lab2.models.NodeInfo;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnicastProxy {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private final Gson gson = new Gson();
    private byte[] receiveData = new byte[1024];
    private DatagramSocket udpSocket;

    public UnicastProxy(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
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

                // Aici problema ca datele care vin nu-s sigure si nu pot converti in json daca sunt greseli,
                // trebuie validare si inca trebuie de facut sa se transmita datele sigur chiar si prin UDP
                MetaInformation metaInformation = gson.fromJson(receivedMessage, MetaInformation.class);

                LOGGER.info("Data from node(port: "
                        + receivePacket.getPort() + ", address: " + receivePacket.getAddress() + ") :\n "
                        + metaInformation);

                nodeInfos.add(new NodeInfo(receivePacket.getAddress(), receivePacket.getPort(), metaInformation));
            } catch (SocketTimeoutException e) {
                LOGGER.error("udp socket timeout");
            }

        }

        LOGGER.info("EXIT unicast handle, time out");

        return nodeInfos;

    }

}
