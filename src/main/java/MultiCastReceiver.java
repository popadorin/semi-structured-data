import java.io.IOException;
import java.net.*;

public class MultiCastReceiver {
    private DatagramSocket clientSocket = new DatagramSocket(6789);
    private InetAddress unicastAddress;
    private int unicastPort;

    public MultiCastReceiver() throws SocketException {
    }

    public void listen() throws IOException {
        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        MulticastSocket mcSocket = new MulticastSocket(mcPort);
        InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);

        System.out.println("Multicast Receiver running at:" + mcSocket.getLocalSocketAddress());
        mcSocket.joinGroup(mcIPAddress);


        boolean isStopped = false;
        while (!isStopped) {
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            System.out.println("Waiting for a  multicast message...");

            mcSocket.receive(packet);

            unicastAddress = packet.getAddress();
            unicastPort = packet.getPort();
            printPacketData(packet);

            String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

            System.out.println("Received message: " + message);

            if (message.trim().toLowerCase().equals("give")) {
                System.out.println("GIVE message came");
                String obj = "Jora";
                sendToProxy(obj);
            }

            if (message.trim().toLowerCase().equals("exit")) {
                isStopped = true;
                mcSocket.leaveGroup(mcIPAddress);
                mcSocket.close();
            }

        }
    }

    private void sendToProxy(String obj) throws IOException {
        byte[] sendData = obj.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, unicastAddress, unicastPort);

        clientSocket.send(sendPacket);
        System.out.println("message successfully sent!");
    }

    private void printPacketData(DatagramPacket packet) {
        System.out.println("Packet:");
        System.out.println("Address: " + packet.getAddress());
        System.out.println("Port: " + packet.getPort());
        System.out.println("Socket address: " + packet.getSocketAddress());
        System.out.println("Data: " + packet.getData());
    }
}
