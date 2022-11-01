/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package bittorrent.client;

import java.io.File;
import java.io.IOException;

public class App {

    public static final String PATHNAME = "src/test/resources/torrents/CuteTogepi.jpg.torrent";
    public static final String PATHNAMEICEBERG = "src/test/resources/torrents/iceberg.jpg.torrent";

    public String getGreeting() {
        return "Hello World! Test";
    }

    public static void main(String[] args) {

        // TODO : Make a proper CLI program
        // We start by loading the torrent file
        Torrent torrent = new Torrent(new File(PATHNAMEICEBERG));
        // Then, we get the tracker's informations
        TrackerConnect tc = new TrackerConnect(torrent);
        TrackerInfo info = tc.getTrackerInfo();

        System.out.println(info);

        // TODO : getOtherPeers
        Leeching leech = new Leeching();
        try {
            leech.send(torrent.getInfo_hash(), tc.getPeer_id(), info.getPeers()[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
