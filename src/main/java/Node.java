import java.io.IOException;

public class Node {
    public static void main(String[] args) {
        try {
            MultiCastReceiver multiCastReceiver = new MultiCastReceiver();
            multiCastReceiver.listen();
        } catch (IOException e) {
            System.out.println("IO Exception on multicast receiver");
        }



//        long startTime = System.currentTimeMillis();
//        int duration = 10000; // 10 seconds
//        long endTime = startTime + duration;
//        while (System.currentTimeMillis() < endTime) {
//            System.out.println("jora");
//            Thread.sleep(1000);
//        }


    }
}
