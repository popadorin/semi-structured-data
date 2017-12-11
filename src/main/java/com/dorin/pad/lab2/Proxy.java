package com.dorin.pad.lab2;

import com.dorin.pad.lab2.models.Employee;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Proxy {
    private final static Logger LOGGER = Logger.getLogger(Proxy.class);
    private static int duration = 10000;
    private final static Gson gson = new Gson();
    private static DatagramSocket udpSocket;
    private static byte[] receiveData = new byte[1024];

    public static void main(String[] args) throws Exception {
        udpSocket = new DatagramSocket();
        ////////////////
        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
        String commandToGetData = "give";

        LOGGER.info("Started multicast udp server...");

        boolean isStopped = false;
        while (!isStopped) {
            System.out.println("Choose command: (send, exit)");
            String userInput = new Scanner(System.in).nextLine();
            switch (userInput.trim().toLowerCase()) {
                case "send":
                    byte[] message = commandToGetData.getBytes();
                    DatagramPacket packet = new DatagramPacket(message, message.length, mcIPAddress, mcPort);
                    udpSocket.send(packet);

                    unicastUdpHandle();

                    break;
                case "exit":
                    isStopped = true;
                    udpSocket.close();
                    break;
                default:
                    LOGGER.error("No such command");
                    break;
            }

            System.out.println(888);

        }

    }

    private static void unicastUdpHandle() throws IOException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;

        while (System.currentTimeMillis() < endTime) {
            // se blocheaza aici dupa ce nu mai are nimeni de trimis si trebuie de rezolvat,
            // ca degeaba mai sta conditia la while daca el nu trece de aici
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpSocket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData());


            // Aici problema ca datele care vin nu-s sigure si nu pot converti in json daca sunt greseli,
            // trebuie validare si inca trebuie de facut sa se transmita datele sigur chiar si prin UDP


//                    Employee employee = gson.fromJson(receivedMessage, Employee.class);
//                    LOGGER.info("Data from node: " + employee);
            LOGGER.info("Data from node(port: "
                    + receivePacket.getPort() + ", address: " + receivePacket.getAddress() + ") :\n "
                    + receivedMessage);

        }

        LOGGER.info("EXIT unicast handle, time out");

    }
}
