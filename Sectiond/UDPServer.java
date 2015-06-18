import java.io.IOException;
import java.net.*;

/**
 * The UDP server.
 */
public class UDPServer {
    public final static int PORT = 1201, BUFFER_SIZE = 1024, TIME_SLOTS = 5000;
    public final static int SLOT_TIME = 8;

    public static void main(String[] args) {
        runServer(20);
        runServer(18);
        runServer(16);
        runServer(14);
        runServer(12);
        runServer(10);
        runServer(8);
        runServer(6);
        runServer(4);
    }

    public static void runServer(int lambda) {
        byte[] toSendBytes;

        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            serverSocket.setReuseAddress(true);
            long previousEndTime = 0;
            int runs = 0;
            int collisions = 0;
            long totalDelay = 0;
            boolean firstRun = true;

            // stop the server after we get all of the client messages
            System.out.println("Lambda: " + lambda);
            System.out.println("Traffic Load: " + lambda / 2);

            // start two threaded clients
            (new Thread(new UDPClient("Client A", lambda))).start();
            (new Thread(new UDPClient("Client B", lambda))).start();

            // run for TIME_SLOTS
            while(runs < TIME_SLOTS) {
                byte[] received = new byte[BUFFER_SIZE];

                DatagramPacket receivedPacket = new DatagramPacket(received, received.length);

                serverSocket.receive(receivedPacket);

                String clientMessage = new String(receivedPacket.getData());
                InetAddress clientAddress = receivedPacket.getAddress();

                int port = receivedPacket.getPort();

                long endTime = System.nanoTime();

                long slotTimeDifference = (endTime - previousEndTime) / 100000;
                if(!firstRun) {
                    totalDelay += slotTimeDifference;
                }

                // adjust previous end time to current end time
                previousEndTime = endTime;

                // add up times
                totalDelay += (endTime - previousEndTime) / 100000;

                // setup reply
                String toSend = "Success";
                if(slotTimeDifference <= SLOT_TIME) {
                    toSend = "Collision";
                    collisions++;
                }
                toSendBytes = toSend.getBytes();

                DatagramPacket sentPacket = new DatagramPacket(toSendBytes, toSendBytes.length, clientAddress, port);
                serverSocket.send(sentPacket);

                firstRun = false;
                runs++;
            }

            serverSocket.close();

            int successfulTransmissions = (TIME_SLOTS - collisions);
            double throughput = (successfulTransmissions + 0.0) / TIME_SLOTS;
            double averageDelay = (totalDelay + 0.0) / TIME_SLOTS; // divide by 10 to get ms

            System.out.println("Collisions: " + collisions);
            System.out.println("Successful Transmissions: " + successfulTransmissions);
            System.out.println("Throughput: " + throughput);
            System.out.println("Average Delay: " + averageDelay);
            System.out.println();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
