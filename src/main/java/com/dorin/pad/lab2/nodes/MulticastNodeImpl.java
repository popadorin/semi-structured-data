package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.models.ProxyCommand;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastNodeImpl implements MulticastNode {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private MulticastSocket mcSocket;
    private InetAddress mcIPAddress;
    private DatagramPacket packet;

    public MulticastNodeImpl(String mcIPStr, int mcPort) throws IOException {
        mcSocket = new MulticastSocket(mcPort);
        mcIPAddress = InetAddress.getByName(mcIPStr);
        LOGGER.info("Multicast Node running at:" + mcSocket.getLocalSocketAddress());
        mcSocket.joinGroup(mcIPAddress);
    }

    @Override
    public void close() throws IOException {
        mcSocket.leaveGroup(mcIPAddress);
        mcSocket.close();
    }

    @Override
    public ProxyCommand readFromProxy() throws IOException {
        packet = new DatagramPacket(new byte[1024], 1024);
        LOGGER.info("Waiting for a  multicast message...");

        mcSocket.receive(packet);

        String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

        return ProxyCommand.valueOf(message.trim());
    }

    @Override
    public InetAddress getProxyAddress() {
        return packet.getAddress();
    }

    @Override
    public int getProxyPort() {
        return packet.getPort();
    }

}
