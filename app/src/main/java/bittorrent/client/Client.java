package bittorrent.client;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.cli.CLIException;
import bittorrent.client.cli.CLIHandler;
import bittorrent.client.seeder.SeederThread;
import bittorrent.client.torrent.Torrent;
import bittorrent.client.tracker.Tracker;
import bittorrent.client.tracker.TrackerInfo;

public class Client {

    public static Logger log = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        try {
            Torrent torrent = CLIHandler.parse(args);

            ServerSocket mainSocket = new ServerSocket(0);
            int port = mainSocket.getLocalPort();

            Peer self = new Peer(port);
            log.info("client peer (self) {}", self);
            Tracker tracker = new Tracker(torrent, self);
            TrackerInfo trackerInfo = tracker.getInfo();

            /*
            for (Peer peer : trackerInfo.getPeers()) {
                if (! peer.equals(self)) {
                    Leecher.leech(torrent, self, peer);
                }
            }*/

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

            log.info("listening on " + mainSocket.getLocalSocketAddress());
            while (true) {
                Socket clientSocket = mainSocket.accept();
                executor.execute(new SeederThread(clientSocket, torrent, self));
            }

        } catch (CLIException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }
}
