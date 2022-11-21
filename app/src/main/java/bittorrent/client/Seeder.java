package bittorrent.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

public class Seeder {

    public void seed(Torrent torrent,int server_port, byte[] peer_id) {
        try {
            // When seeding, we are the server, waiting for a client connexion.
            // As such, we need to open a server socket, on the port specified
            System.out.println("Accepting requests on port " + server_port);
            ServerSocket seederSocket = new ServerSocket(server_port);

            // This function is blocking, and will advance only uppon recieving a request
            Socket clientSocket = seederSocket.accept();
            
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);

            DataInputStream data_in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            DataOutputStream data_out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            
            // HANDSHAKE <===
            Handshake handreq = new Handshake(data_in);
            System.out.println("Handshake request : ");
            System.out.println(handreq.toString());

            // HANDSHAKE ===>
            Handshake handresp = new Handshake(new byte[8], torrent.getInfo_hash(), peer_id);
            handresp.sendHandshake(data_out);
            System.out.println("Handshake response : \n" + handresp);

            // BITFIELD <===
            byte[] bitfield = new byte[7];
            data_in.read(bitfield);
            System.out.println(Arrays.toString(bitfield));




        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
