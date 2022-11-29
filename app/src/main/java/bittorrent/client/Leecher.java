package bittorrent.client;

import bittorrent.client.tcpMessage.BittorrentMessage;
import bittorrent.client.tcpMessage.*;

import java.io.*;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.tcpMessage.Bitfield;

public class Leecher {

    private static Logger log = LogManager.getLogger();

    public void leech(TorrentTask task, byte[] peer_id, Peer seeder) throws IOException {

        try {
            // TODO : Remove / before IP
            Torrent torrent = task.getTorrent();
            String server = seeder.getIp().toString().substring(1); // Server name or IP address
            int server_port = seeder.getPort();
            // Create socket that is connected to server on specified port
            log.debug("Seeder => IP : " + server + ", Port : " + server_port);
            Socket socket = new Socket(server, server_port);

            DataInputStream data_in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream data_out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // HANDSHAKE ===>
            Handshake handreq = new Handshake(new byte[8], torrent.getInfo_hash(), peer_id);
            handreq.sendHandshake(data_out);

            // HANDSHAKE <===
            Handshake handresp = new Handshake(data_in);

            log.info("Handshake request : ");
            log.info(handreq.toString());
            log.info("Handshake response : ");
            log.info(handresp.toString());

            // BITFIELD <===
            Bitfield  bitfield_received = (Bitfield) new BittorrentMessage(data_in).identify();
            log.info(bitfield_received);

            // BITFIELD ===>
            Bitfield bitfield_sent = new Bitfield(new byte[2]);
            bitfield_sent.send(data_out);

            // INTERESTED ===>
            Interested interested = new Interested();
            interested.send(data_out);

            // UNCHOKE <===
            Unchoke unchoke = (Unchoke) new BittorrentMessage(data_in).identify();
            log.info(unchoke);

            // Collect all pieces and place it in a buffer
            TorrentFile file = new TorrentFile(torrent.getLength(), torrent.getPiece_length());
            file.Leeching100(data_in, data_out);
            file.generateFile(task.getDownloadedFilePath());


            log.debug("Closing the socket");
            socket.close(); // Close the socket and its streams

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}