package bittorrent.client;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Leeching {

    public void send(byte[] info_hash, byte[] peer_id, Peer seeder) throws IOException {
        Handshake handreq = new Handshake(19, "BitTorrent protocol", new byte[8], info_hash, peer_id);

        String server = seeder.getIp().toString().substring(1); // Server name or IP address
        int servPort = seeder.getPort();

        // Convert argument String to bytes using the default character encoding
        byte[] data = new byte[68];

        try {
            // Create socket that is connected to server on specified port
            System.out.println("serv+port : " + server + " " + servPort);
            Socket socket = new Socket(server, servPort);

            System.out.println("Connected to server...sending Handshake");

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            DataInputStream data_in = new DataInputStream(new BufferedInputStream(in));

            DataOutputStream real_out = new DataOutputStream(new BufferedOutputStream(out));

            handreq.toByteArray(real_out);

            byte[] name_leng = new byte[1];
            byte[] nameb = new byte[19];
            byte[] extension = new byte[8];
            byte[] hash = new byte[20];
            byte[] peerid = new byte[20];

            data_in.read(name_leng);
            data_in.read(nameb);
            data_in.read(extension);
            data_in.read(hash);
            data_in.read(peerid);

            int name_length = Utils.byteArrayToUnsignedInt(name_leng);
            String name = new String(nameb);

            Handshake handresp = new Handshake(name_length, name, extension, info_hash, peer_id);

            System.out.println("Handshake req : ");
            System.out.println(handresp.toString());
            System.out.println("Handshake resp : ");
            System.out.println(handreq.toString());

            // System.out.println(data_in.readInt());
            /*
             * // Receive the same string back from the server
             * int totalBytesRcvd = 0; // Total bytes received so far
             * int bytesRcvd; // Bytes received in last read
             * while (totalBytesRcvd < 68) {
             * if ((bytesRcvd = in.read(data, totalBytesRcvd,
             * data.length - totalBytesRcvd)) == -1)
             * throw new SocketException("Connection closed prematurely");
             * totalBytesRcvd += bytesRcvd;
             * } // data array is full
             */
            socket.close(); // Close the socket and its streams
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}