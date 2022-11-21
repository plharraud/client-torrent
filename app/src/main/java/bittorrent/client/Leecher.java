package bittorrent.client;

import java.io.*;
import java.net.Socket;

public class Leecher {

    public void leech(Torrent torrent, byte[] peer_id, Peer seeder) throws IOException {

        try {
            // TODO : Remove / before IP
            String server = seeder.getIp().toString().substring(1); // Server name or IP address
            int server_port = seeder.getPort();
            // Create socket that is connected to server on specified port
            System.out.println("Seeder => IP : " + server + ", Port : " + server_port);
            Socket socket = new Socket(server, server_port);

            DataInputStream data_in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream data_out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // HANDSHAKE ===>
            Handshake handreq = new Handshake(new byte[8], torrent.getInfo_hash(), peer_id);
            handreq.sendHandshake(data_out);

            // HANDSHAKE <===
            Handshake handresp = new Handshake(data_in);

            System.out.println("Handshake request : ");
            System.out.println(handreq.toString());
            System.out.println("Handshake response : ");
            System.out.println(handresp.toString());

            // BITFIELD <===
            Bitfield  bitfield = new Bitfield(data_in);
            System.out.println("Bitfield received : ");
            System.out.println(bitfield.toString());

            // BITFIELD ===>
            Bitfield Bitf = new Bitfield();
            Bitf.sendBitfield(data_out);

            // INTERESTED ===>
            Interested Inti = new Interested();
            Inti.sendSeq(data_out);

            // UNCHOKE <===
            Unchoke unchoketest = new Unchoke(data_in);
            System.out.println("Unchoke received : ");
            System.out.println(unchoketest.toString());

            // Collect all pieces and place it in a buffer
            TorrentFile file = new TorrentFile(torrent.getLength(), torrent.getPiece_length());
            file.Leeching100(data_in, data_out);
            file.generateFile(torrent.getName());


            System.out.println("Closing the socket");
            socket.close(); // Close the socket and its streams

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}