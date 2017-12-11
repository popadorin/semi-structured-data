package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.configurations.Configurations;
import com.dorin.pad.lab2.models.ClientCommand;
import com.dorin.pad.lab2.models.Employee;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class Proxy {
    private final static Logger LOGGER = Logger.getLogger(Proxy.class);
    private final static Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            LOGGER.info("Proxy started...");
            // client connection

            TcpProxyClient proxyClient = new TcpProxyClientImpl(Configurations.clientProxyPort);

            boolean proxyIsStopped = false;
            while (!proxyIsStopped) {
                ClientCommand clientCommand = ClientCommand.valueOf(new String(proxyClient.readFromClient()));
                switch (clientCommand) {
                    case GET_EMPLOYEE:
                        treatGetEmployee(proxyClient);
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
            se.printStackTrace();
        } catch (IOException ioe) {
            LOGGER.error("IOException on: " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("ClassNotFoundException on: " + cnfe.getMessage());
        }
    }

    private static void treatGetEmployee(TcpProxyClient proxyClient) throws IOException, ClassNotFoundException {
        DatagramSocket udpSocket = new DatagramSocket();
        MulticastProxy multicastProxy = new MulticastProxy(udpSocket,
                Configurations.multicastAddress, Configurations.multicastPort);

        UnicastProxy unicastProxy = new UnicastProxy(udpSocket);
        ProxyProtocol protocol = new ProxyProtocol();
        Employee employee = protocol.getEmployee(multicastProxy, unicastProxy);

        proxyClient.sendToClient(gson.toJson(employee).getBytes());
    }

}
