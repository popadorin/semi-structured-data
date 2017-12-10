import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_Server {
    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        int port = 0;

        boolean isStopped = false;
        while(!isStopped) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // get data from client
            serverSocket.receive(receivePacket);

            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);

            InetAddress IPAddress = receivePacket.getAddress();
            port = receivePacket.getPort();

            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            // send data to client
            serverSocket.send(sendPacket);
        }
    }
}
