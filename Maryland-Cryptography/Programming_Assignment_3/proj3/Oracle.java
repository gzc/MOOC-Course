import java.net.Socket;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;


public class Oracle {
    String hostname = "54.165.60.84";
    int port = 80;
    int block_length = 16;

    Socket socket;
    OutputStream out;
    BufferedReader in;

    public int connect() {
        try {
            socket = new Socket(hostname, port);
            out = socket.getOutputStream();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to server successfully.");
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int disconnect() {
        try {
            socket.close();
            System.out.println("Connection closed successfully.");
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Packet Structure: < num_blocks(1) || ciphertext(16*num_blocks) || null-terminator(1) >
    public int send(int ctext[], int num_blocks) {
        byte message[] = new byte[(num_blocks*block_length)+2];
        char recvbit[] = new char[2];
        int i;

        message[0] = (byte) num_blocks;
        for (i=1; i < ((num_blocks*block_length)+1); i++) {
            message[i] = (byte) ctext[i-1];
        }
        message[((num_blocks*block_length)+1)] = (byte) 0;
        try {
            out.write(message);
            out.flush();
            in.read(recvbit,0,2);
            return Integer.valueOf(new String(recvbit).replaceAll("\0", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
