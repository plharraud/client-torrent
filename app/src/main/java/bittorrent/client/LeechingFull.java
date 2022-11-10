package bittorrent.client;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LeechingFull {

    public void leech(Torrent torrent, byte[] peer_id, Peer seeder) throws IOException {

        final int maxsizepacket = 16384;

        Handshake handreq = new Handshake(19, "BitTorrent protocol", new byte[8], torrent.getInfo_hash(), peer_id);

        // TODO : Remove / before IP
        String server = seeder.getIp().toString().substring(1); // Server name or IP address

        int servPort = seeder.getPort();

        try {
            // Create socket that is connected to server on specified port
            System.out.println("Seeder => IP : " + server + ", Port : " + servPort);
            Socket socket = new Socket(server, servPort);

            DataInputStream data_in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            DataOutputStream data_out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // HANDSHAKE ===>
            handreq.sendHandshake(data_out);

            // HANDSHAKE <===
            Handshake handresp = new Handshake(data_in);

            System.out.println("Handshake request : ");
            System.out.println(handreq.toString());
            System.out.println("Handshake response : ");
            System.out.println(handresp.toString());

            // BITFIELD ===>
            byte[] bitfield = new byte[7];
            data_in.read(bitfield);

            // BITFIELD <===
            Bitfield Bitf = new Bitfield();
            Bitf.sendBitfield(data_out);

            // INTERESTED ===>
            Interested Inti = new Interested();
            Inti.sendSeq(data_out);

            // UNCHOKE <===
            byte[] unchoketest = new byte[5];
            data_in.read(unchoketest);

            int torrentlength = torrent.getLength();
            int torrentpiecelength = torrent.getPiece_length();

            int piececur = 0;
            int piecemax = (int) Math.ceil((float) torrentlength / (float) torrentpiecelength);
            int remaininglength = torrentpiecelength;
            int lenreq;
            int piecepart = 0;

            System.out.println("torrent length : " + torrentlength);
            System.out.println("piece length : " + torrentpiecelength);
            System.out.println("nb of pieces : " + piecemax);
            System.out.println("download rate : " + maxsizepacket);

            ByteArrayOutputStream imagebytes = new ByteArrayOutputStream();

            // TODO : Implement DataStructure for pieces + Assemble pieces part
            // Piece[][] pieces = new Piece[piecemax][];

            // Collect all pieces and place it in a buffer
            TorrentFile file = new TorrentFile(torrentlength, torrentpiecelength);

            file.Leeching100(data_in, data_out);

            file.writeBytesBlocks();

            System.out.println("DONE");

            socket.close(); // Close the socket and its streams

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}