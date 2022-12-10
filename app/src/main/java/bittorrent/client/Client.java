package bittorrent.client;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.cli.CLIException;
import bittorrent.client.cli.CLIHandler;
import bittorrent.client.torrent.Torrent;
import bittorrent.client.tracker.Tracker;
import bittorrent.client.tracker.TrackerInfo;

public class Client {

    public static final int DEFAULT_PORT = 6685;

    public static Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            Torrent torrent = CLIHandler.parse(args);

            ServerSocket mainSocket = new ServerSocket(0);
            int port = mainSocket.getLocalPort();
            log.info("listening on  " + mainSocket.getLocalSocketAddress());

            Peer self = new Peer(port);

            Tracker tracker = new Tracker(torrent, self);
            TrackerInfo trackerInfo = tracker.getInfo();

            // TrackerConnect tc = new TrackerConnect(task.getTorrent(), port);
            // TrackerInfo info = tc.iHaveTheFullFile();

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

            while (true) {
                Socket clientSocket = mainSocket.accept();
                executor.execute(new SeederThread(clientSocket));
            }

        } catch (CLIException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            log.error(e);
        }
    }
}
