package bittorrent.client;

import java.util.Random;

public class BittorrentTracker {

    private String info_hash;
    private String announce;
    private byte[] peer_id;
    private int port ;
    private int dowloaded;
    private int uploaded;
    private int left;

    BittorrentTracker(Torrent torrent){
        info_hash = torrent.getInfo_hash_hex();
        announce  = torrent.getAnnounce();

        // TODO : Calculate the port with availability
        port = 6881;

        // Initialize a random peer_id
        peer_id = new byte[20];
        new Random().nextBytes(peer_id);
    }
}