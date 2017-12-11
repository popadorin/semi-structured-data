package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.models.Employee;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UnicastProxy {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private final int duration = 10000;
    private final Gson gson = new Gson();
    private byte[] receiveData = new byte[1024];
    private DatagramSocket udpSocket;

    public UnicastProxy(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
    }

    public void listen() throws IOException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;

        while (System.currentTimeMillis() < endTime) {
            // se blocheaza aici dupa ce nu mai are nimeni de trimis si trebuie de rezolvat,
            // ca degeaba mai sta conditia la while daca el nu trece de aici
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpSocket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData(),
                    receivePacket.getOffset(), receivePacket.getLength());


            // Aici problema ca datele care vin nu-s sigure si nu pot converti in json daca sunt greseli,
            // trebuie validare si inca trebuie de facut sa se transmita datele sigur chiar si prin UDP
            Employee employee = gson.fromJson(receivedMessage, Employee.class);

            LOGGER.info("Data from node(port: "
                    + receivePacket.getPort() + ", address: " + receivePacket.getAddress() + ") :\n "
                    + employee);

        }

        LOGGER.info("EXIT unicast handle, time out");

    }
}
