package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.models.Employee;
import com.dorin.pad.lab2.models.MetaInformation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class MulticastNode {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private Employee employee;
    private UnicastNode unicastNode;
    private MetaInformation metaInformation;

    public MulticastNode(int port, MetaInformation metaInformation, Employee employee) throws SocketException {
        unicastNode = new UnicastNode(port);
        this.employee = employee;
        this.metaInformation = metaInformation;
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

            unicastNode.setAddress(packet.getAddress());
            unicastNode.setPort(packet.getPort());

            String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

            LOGGER.info("Received message: " + message);

            if (message.trim().toLowerCase().equals("give")) {
                unicastNode.sendToProxy(metaInformation);
            }

            if (message.trim().toLowerCase().equals("exit")) {
                isStopped = true;
                mcSocket.leaveGroup(mcIPAddress);
                mcSocket.close();
            }

        }
    }



}
