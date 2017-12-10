import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_Client {
    public static void main(String args[]) throws Exception {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        boolean isStopped = false;
        while (!isStopped) {
            // get from user
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            String sentence = inFromUser.readLine();

            if (sentence.equals("exit")) {
                isStopped = true;
                clientSocket.close();
                break;
            }

            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

            // send data to server
            clientSocket.send(sendPacket);

            // get data from server
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);

        }
//        clientSocket.close();
    }
}
