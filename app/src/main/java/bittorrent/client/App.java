/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package bittorrent.client;

import java.io.File;
import java.io.IOException;

public class App {

    public static final String PATHNAME = "src/test/resources/torrents/CuteTogepi.jpg.torrent";
    public static final String PATHNAMEICEBERG = "src/test/resources/torrents/iceberg.jpg.torrent";
    public static final String PATHNAMETROLL = "src/test/resources/torrents/troll.jpg.torrent";
    public static final String PATHNAMEBOEUF= "src/test/resources/torrents/boeufalabiere.jpg.torrent";
    public static final String PATHNAME4K= "src/test/resources/torrents/Hutton_in_the_Forest_4K.jpg.torrent";
    public static final String PATHNAMEMP4= "src/test/resources/torrents/yeah.mp4.torrent";

    public String getGreeting() {
        return "Hello World! Test";
    }

    public static void main(String[] args) {

        // TODO : Make a proper CLI program
        // We start by loading the torrent file
        Torrent torrent = new Torrent(new File(PATHNAMEMP4));
        // Then, we get the tracker's informations
        TrackerConnect tc = new TrackerConnect(torrent);
        TrackerInfo info = tc.getTrackerInfo();

        System.out.println(info);

        // TODO : getOtherPeers
        try {
            new Leecher().leech(torrent, tc.getPeer_id(), info.getPeers()[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
