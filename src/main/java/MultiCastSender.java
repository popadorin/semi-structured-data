import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MultiCastSender {
    public static void main(String[] args) throws Exception {
        byte[] receiveData = new byte[1024];

        ////////////////
        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        DatagramSocket udpSocket = new DatagramSocket();
        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);

        System.out.println("Started multicast udp server...");

        boolean firstLoopPassed = false;
        boolean isStopped = false;
        while (!isStopped) {
            String userInput = new Scanner(System.in).nextLine();

            byte[] message = userInput.getBytes();
            DatagramPacket packet = new DatagramPacket(message, message.length, mcIPAddress, mcPort);
            udpSocket.send(packet);
            if (!firstLoopPassed) {
                firstLoopPassed = true;
                continue;
            }

            // receive data from node careva
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpSocket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData());
            System.out.println("Data from node: " + receivedMessage);

            if (userInput.trim().toLowerCase().equals("exit")) {
                isStopped = true;
                udpSocket.close();
            }
        }
    }
}
