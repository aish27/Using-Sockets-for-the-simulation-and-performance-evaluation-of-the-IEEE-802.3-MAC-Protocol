import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The UDP client.
 */
public class UDPClient implements Runnable {
    public final static int PORT = 1201, BUFFER_SIZE = 1024;
    public final static String ADDRESS = "127.0.0.1";
    public static final int SLOT_TIME = 8;
    private Random random;
    private String clientName;
    private int lambda;

    /**
     * Creates a UDPClient with a clientName.
     * @param clientName the client name
     */
    public UDPClient(String clientName, int lambda) {
        this.clientName = clientName;
        this.lambda = lambda;

        random = new Random();
    }

    /**
     * The run method for threading purposes.
     */
    public void run() {
        // setup string to send to server
        String send = clientName;
        for(int i = 0; i < BUFFER_SIZE - clientName.length(); i++) {
            send += ".";
        }
        byte[] sendBytes = send.getBytes();

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName(ADDRESS);
            int n = 0;

            while(true) {
                // sleep for a bit before sending a message
                TimeUnit.NANOSECONDS.sleep(getSlotTime(n));

                DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, inetAddress, PORT);
                clientSocket.send(sendPacket);

                byte[] received = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(received, received.length);
                clientSocket.receive(receivePacket);

                // check out what type of message we received
                String receiveMessage = new String(receivePacket.getData());
                if(receiveMessage.startsWith("Collision")) {
                    n++;
                }
                else {
                    n /= 2; // halve it on success
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the slot time.
     * @return the slot time
     */
    private int getSlotTime(int n) {
        int exponentialBinaryBackoffAmount = 0;
        if(n > 0) {
            exponentialBinaryBackoffAmount = random.nextInt((int) (Math.pow(2, n) - 1));
        }

        return SLOT_TIME * lambda * (exponentialBinaryBackoffAmount + 1);
    }
}
