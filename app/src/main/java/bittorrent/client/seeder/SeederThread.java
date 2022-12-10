package bittorrent.client.seeder;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.Peer;
import bittorrent.client.torrent.Torrent;

public class SeederThread implements Runnable {

    private Socket clientSocket;
    private Torrent torrent;
    private Peer self;

    Logger log = LogManager.getLogger();

    public SeederThread(Socket clientSocket, Torrent torrent, Peer self) {
        this.clientSocket = clientSocket;
        this.torrent = torrent;
        this.self = self;
    }

    public void run() {
        log.info("handling client " + clientSocket.getRemoteSocketAddress());
        Seeder.seed(torrent, clientSocket, self);
    }

}
