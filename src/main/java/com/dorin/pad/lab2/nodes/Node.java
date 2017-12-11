package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.models.Employee;
import com.dorin.pad.lab2.models.MetaInformation;
import com.dorin.pad.lab2.models.ProxyCommand;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class Node {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private final Gson gson = new Gson();
    private Employee employee;
    private UnicastNode unicastNode;
    private MulticastNode multicastNode;
    private MetaInformation metaInformation;
    private int tcpPort;

    public Node(int unicastPort, MetaInformation metaInformation, Employee employee) throws IOException {
        unicastNode = new UnicastNode(unicastPort);

        // configuration
        tcpPort = 9999;

        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        multicastNode = new MulticastNodeImpl(mcIPStr, mcPort);

        this.employee = employee;
        this.metaInformation = metaInformation;
    }

    public void run() throws IOException, ClassNotFoundException {
        boolean proxyAlreadyMulticasted = false;
        boolean isStopped = false;
        while (!isStopped) {
            LOGGER.info("waiting command from proxy...");
            ProxyCommand commandFromProxy;
            if (!proxyAlreadyMulticasted) {
                commandFromProxy = multicastNode.readFromProxy();
                proxyAlreadyMulticasted = true;
            } else {
                commandFromProxy = unicastNode.readFromProxy();
            }

            LOGGER.info("command from proxy: " + commandFromProxy);
            setUnicastData(multicastNode.getProxyAddress(), multicastNode.getProxyPort());

            switch (commandFromProxy) {
                case GIVE_META_INFO:
                    unicastNode.sendToProxy(metaInformation);
                    break;
                case YOU_ARE_MAVEN:
                    // open TCP connection
                    LOGGER.info("Open TCP connection from node");
                    treatTcpConnection();
                    break;
                case EXIT:
                    isStopped = true;
                    multicastNode.close();
                    break;
                default:
                    LOGGER.error("No such command from proxy");
                    break;
            }

        }
    }

    private void setUnicastData(InetAddress address, int port) {
        unicastNode.setAddress(address);
        unicastNode.setPort(port);
    }

    private void treatTcpConnection() throws IOException, ClassNotFoundException {
        TcpNode tcpNode = new TcpNodeImpl(tcpPort);
        LOGGER.info("Server TCP node created and running...");
        ProxyCommand command = ProxyCommand.valueOf(new String(tcpNode.readFromProxy()));

        switch (command) {
            case GIVE_DATA:
                tcpNode.sendToProxy(gson.toJson(employee).getBytes());
                return;
            case EXIT:
                tcpNode.close();
                return;
            default:
                LOGGER.info("No such command");

        }
    }


}
