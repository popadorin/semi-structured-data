package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.models.ClientCommand;
import com.dorin.pad.lab2.models.Employee;
import com.dorin.pad.lab2.models.NodeInfo;
import com.dorin.pad.lab2.models.ProxyCommand;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Scanner;

public class Proxy {
    private final static Logger LOGGER = Logger.getLogger(Proxy.class);
    private final static Gson gson = new Gson();
    // configurations
    private final static int clientPort = 7777;

    private final static int timeoutForNodeDiscovery = 5000; // 5 seconds
    private final static int multicastPort = 12345;
    private final static String multicastAddress = "230.1.1.1";


    public static void main(String[] args) {
        try {
            LOGGER.info("Proxy started...");
            // client connection

            TcpProxyClient proxyClient = new TcpProxyClientImpl(clientPort);

            boolean proxyIsStopped = false;
            while (!proxyIsStopped) {
                ClientCommand clientCommand = ClientCommand.valueOf(new String(proxyClient.readFromClient()));
                switch (clientCommand) {
                    case GIVE_EMPLOYEE:
                        Employee employee = getEmployee();
                        proxyClient.sendToClient(gson.toJson(employee).getBytes());
                        break;
                    case EXIT:
                        proxyIsStopped = true;
                        break;
                    default:
                        LOGGER.error("No such command");
                }
            }

            LOGGER.info("Proxy stopped");

        } catch (UnknownHostException e) {
            LOGGER.error("UnknownHostException on: " + e.getMessage());
        } catch (SocketException se) {
            LOGGER.error("SocketException on: " + se.getMessage());
        } catch (IOException ioe) {
            LOGGER.error("IOException on: " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("ClassNotFoundException on: " + cnfe.getMessage());
        }
    }

    private static Employee getEmployee() throws IOException {
        // multicast and unicast logic
        DatagramSocket udpSocket = new DatagramSocket();
        MulticastProxy multicastProxy = new MulticastProxy(udpSocket, multicastAddress, multicastPort);
        UnicastProxy unicastProxy = new UnicastProxy(udpSocket);

        runDISProtocol(multicastProxy, unicastProxy, timeoutForNodeDiscovery);
        // return employee
        return null;
    }

    private static void runDISProtocol(MulticastProxy multicastProxy,
                                         UnicastProxy unicastProxy,
                                         int timeoutForNodeDiscovery) throws IOException {

        byte[] message = ProxyCommand.GIVE_META_INFO.name().getBytes();
        multicastProxy.sendToNodes(message);

        List<NodeInfo> nodeInfos = unicastProxy.readFromNodes(timeoutForNodeDiscovery);
        nodeInfos.forEach(System.out::println);

        // find the node with most valuable metaInfo (most numberofconnections);
        NodeInfo maven = getMostValuableNode(nodeInfos);

        // make TCP connection with that node

        TcpProxyNode proxyNode = new TcpProxyNodeImpl(maven.getAddress().getHostName(), maven.getPort());
        // say to that node to GIVE_DATA

        // send that data to client
    }

    private static NodeInfo getMostValuableNode(List<NodeInfo> nodeInfos) {
        NodeInfo mostValuableNode = nodeInfos.get(0);

        for (NodeInfo node : nodeInfos) {
            if (node.getMetaInformation().getNumberOfConnections() >
                    mostValuableNode.getMetaInformation().getNumberOfConnections()) {

                mostValuableNode = node;
            }
        }

        return mostValuableNode;
    }


}
