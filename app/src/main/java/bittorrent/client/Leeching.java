import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Leeching {

    public void Send(byte[] info_hash, byte[] peer_id, Peer seeder)throws IOException
    {
        Handshake handshake = new Handshake(info_hash, peer_id)

        String server = seeder.getIp(); // Server name or IP address
        int servPort = seeder.getPort();

        // Convert argument String to bytes using the default character encoding
        //byte[] data = args[1].getBytes();

        // Create socket that is connected to server on specified port
        Socket socket = new Socket(server, servPort); 

        System.out.println("Connected to server...sending Handshake");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        DataOutputStream real_out = new DataOutputStream(new BufferedOutputStream(out));

        handshake.toByteArray(real_out);

        System.out.println("Handshake sent");

        /*
         * out.write(data); // Send the encoded string to the server
         * 
         * // Receive the same string back from the server
         * int totalBytesRcvd = 0; // Total bytes received so far
         * int bytesRcvd; // Bytes received in last read
         * while (totalBytesRcvd < data.length) {
         * if ((bytesRcvd = in.read(data, totalBytesRcvd,
         * data.length - totalBytesRcvd)) == -1)
         * throw new SocketException("Connection closed prematurely");
         * totalBytesRcvd += bytesRcvd;
         * } // data array is full
         * 
         * System.out.println("Received: " + new String(data));
         */

        socket.close(); // Close the socket and its streams
    }
}