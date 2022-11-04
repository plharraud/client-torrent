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
        Handshake handreq = new Handshake(19, "BitTorrent protocol", new byte[8], torrent.getInfo_hash(), peer_id);

        // TODO : Remove / before IP
        String server = seeder.getIp().toString().substring(1); // Server name or IP address
        int servPort = seeder.getPort();

        // Convert argument String to bytes using the default character encoding
        byte[] data = new byte[68];

        final int maxsizepacket = 16384;

        try {
            // Create socket that is connected to server on specified port
            System.out.println("Seeder => IP : " + server + ", Port : " + servPort);
            Socket socket = new Socket(server, servPort);

            System.out.println("Connected to server...sending Handshake");

            DataInputStream data_in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            DataOutputStream real_out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

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

            Handshake handresp = new Handshake(name_length, name, extension, hash, peer_id);

            System.out.println("Handshake req : ");
            System.out.println(handreq.toString());
            System.out.println("Handshake resp : ");
            System.out.println(handresp.toString());

            // BITFIELD ===>
            byte[] bitfield = new byte[7];
            data_in.read(bitfield);

            // BITFIELD <===
            Bitfield Bitf = new Bitfield();
            Bitf.sendBitfield(real_out);

            // INTERESTED ===>
            Interested Inti = new Interested();
            Inti.sendSeq(real_out);

            // UNCHOKE <===
            byte[] unchoketest = new byte[4];
            data_in.read(unchoketest);

            int torrentlength = torrent.getLength();
            int torrentpiecelength = torrent.getPiece_length();
            System.out.println("torrent length : " + torrentlength);
            System.out.println("piece length : " + torrentpiecelength);

            byte[] pspec = new byte[4];
            data_in.read(pspec);

            int piececur = 0;
            // int piecemax = (int) Math.ceil(torrentlength / torrentpiecelength);
            int piecemax = torrentlength / torrentpiecelength;
            int remaininglength = torrentpiecelength;
            int lenreq;
            int piecepart = 0;

            byte[] p1 = new byte[4];
            byte[] p2 = new byte[1];
            byte[] p3 = new byte[4];
            byte[] p4 = new byte[4];
            byte[] p5 = new byte[16384];

            ByteArrayOutputStream imagebytes = new ByteArrayOutputStream();

            // collect pieces and place it in a buffer

            for (piececur = 0; piececur < piecemax + 1; piececur++) {

                if (piececur == piecemax) {
                    remaininglength = torrentlength - torrentpiecelength * piececur;
                } else {
                    remaininglength = torrentpiecelength;
                }

                piecepart = 0;

                while (remaininglength > 0) {
                    if (remaininglength < maxsizepacket) {
                        lenreq = remaininglength;
                    } else {
                        lenreq = maxsizepacket;
                    }
                    Request req = new Request(piececur, piecepart, lenreq);
                    // REQUEST ===>
                    req.sendReq(real_out);
                    data_in.read(p1);
                    data_in.read(p2);
                    data_in.read(p3);
                    data_in.read(p4);
                    if (remaininglength >= maxsizepacket) {
                        data_in.read(p5);
                        imagebytes.write(p5);
                    } else {
                        byte[] p6 = new byte[remaininglength];
                        data_in.read(p6);
                        imagebytes.write(p6);
                        System.out.println("last piece length : " + remaininglength);
                    }
                    remaininglength -= maxsizepacket;
                    piecepart++;
                }

            }

            // byte array to jpg
            try (OutputStream out = new BufferedOutputStream(
                    new FileOutputStream("src/test/resources/torrents/test.jpg"))) {
                out.write(imagebytes.toByteArray());
            }

            socket.close(); // Close the socket and its streams

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}