package com.dorin.pad.lab2.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastProxy {
    private InetAddress mcIPAddress;
    private int mcPort;
    private DatagramSocket udpSocket;

    public MulticastProxy(DatagramSocket udpSocket, String mcAddress, int mcPort) throws UnknownHostException {
         mcIPAddress = InetAddress.getByName(mcAddress);
         this.mcPort = mcPort;
         this.udpSocket =  udpSocket;
    }

    public void sendToNodes(byte[] message) throws IOException {
        DatagramPacket packet = new DatagramPacket(message, message.length, mcIPAddress, mcPort);
        udpSocket.send(packet);
    }
}
