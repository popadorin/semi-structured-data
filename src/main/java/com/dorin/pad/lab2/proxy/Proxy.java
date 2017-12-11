package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.models.NodeInfo;
import com.dorin.pad.lab2.models.ProxyCommand;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Proxy {
    private final static Logger LOGGER = Logger.getLogger(Proxy.class);

    public static void main(String[] args) {
        try {
            DatagramSocket udpSocket = new DatagramSocket();

            // configurations
            final int timeoutForNodeDiscovery = 5000; // 5 seconds
            final int mcPort = 12345;
            final String mcAddress = "230.1.1.1";

            MulticastProxy multicastProxy = new MulticastProxy(udpSocket, mcAddress, mcPort);
            UnicastProxy unicastProxy = new UnicastProxy(udpSocket);

            LOGGER.info("Started multicast udp server...");

            boolean isStopped = false;
            while (!isStopped) {
                System.out.println("Choose command: (send, exit)");
                String userInput = new Scanner(System.in).nextLine();
                switch (userInput.trim().toLowerCase()) {
                    case "send":
                        runProxyProtocol(multicastProxy, unicastProxy, timeoutForNodeDiscovery);
                        break;
                    case "exit":
                        isStopped = true;
                        udpSocket.close();
                        break;
                    default:
                        LOGGER.error("No such command");
                        break;
                }

            }
        } catch (UnknownHostException e) {
            LOGGER.error("UnknownHostException on: " + e.getMessage());
        } catch (SocketException se) {
            LOGGER.error("SocketException on: " + se.getMessage());
        } catch (IOException ioe) {
            LOGGER.error("IOException on: " + ioe.getMessage());
        }
    }

    private static void runProxyProtocol(MulticastProxy multicastProxy,
                                         UnicastProxy unicastProxy,
                                         int timeoutForNodeDiscovery) throws IOException {

        byte[] message = ProxyCommand.GIVE_META_INFO.name().getBytes();
        multicastProxy.sendToNodes(message);

        List<NodeInfo> nodeInfos = unicastProxy.readFromNodes(timeoutForNodeDiscovery);
        nodeInfos.forEach(System.out::println);

        // find the node with most valuable metaInfo (most numberofconnections);

        // make TCP connection with that node

        // say to that node to GIVE_DATA

        // send that data to client
    }

}
