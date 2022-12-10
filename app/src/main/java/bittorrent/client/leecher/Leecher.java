package bittorrent.client.leecher;

import bittorrent.client.tcpMessage.BittorrentMessage;
import bittorrent.client.torrent.Torrent;
import bittorrent.client.torrent.TorrentFile_;
import bittorrent.client.Handshake;
import bittorrent.client.Peer;
import bittorrent.client.tcpMessage.*;

import java.io.*;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.tcpMessage.Bitfield;

public class Leecher {

    private static Logger log = LogManager.getLogger();

    public static void leech(Torrent torrent, Peer self, Peer seeder) throws IOException {
        try {
            // TODO : Remove / before IP
            String server = seeder.getIp().toString().substring(1); // Server name or IP address
            int server_port = seeder.getPort();
            // Create socket that is connected to server on specified port
            log.info("Seeder => IP : " + server + ", Port : " + server_port);
            Socket socket = new Socket(server, server_port);

            DataInputStream data_in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream data_out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // HANDSHAKE ===>
            Handshake handreq = new Handshake(new byte[8], torrent.getMetaInfo().getInfoHash(), self.getId());
            handreq.sendHandshake(data_out);

            // HANDSHAKE <===
            Handshake handresp = new Handshake(data_in);

            log.debug("Handshake request : ");
            log.debug(handreq.toString());
            log.debug("Handshake response : ");
            log.debug(handresp.toString());

            // BITFIELD <===
            Bitfield  bitfield_received = (Bitfield) new BittorrentMessage(data_in).identify();
            log.debug(bitfield_received);

            // BITFIELD ===>
            Bitfield bitfield_sent = new Bitfield(new byte[2]);
            bitfield_sent.send(data_out);

            // INTERESTED ===>
            Interested interested = new Interested();
            interested.send(data_out);

            // UNCHOKE <===
            Unchoke unchoke = (Unchoke) new BittorrentMessage(data_in).identify();
            log.debug(unchoke);

            // Collect all pieces and place it in a buffer
            TorrentFile_ file = new TorrentFile_(torrent.getMetaInfo().getLength(), torrent.getMetaInfo().getPieceLength());
            file.Leeching100(data_in, data_out);
            file.generateFile(torrent.getTarget().getFile().getPath());


            log.info("Closing the socket");
            socket.close(); // Close the socket and its streams

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
