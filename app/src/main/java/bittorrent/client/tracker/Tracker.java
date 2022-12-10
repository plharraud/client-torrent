package bittorrent.client.tracker;

import bittorrent.client.Peer;
import bittorrent.client.torrent.Torrent;

public class Tracker {

    private Torrent torrent;

    private TrackerInfo trackerInfo;

    private Peer self;

    public Tracker(Torrent torrent, Peer self) {
        this.torrent = torrent;
        this.self = self;
    }

    private TrackerInfo fetchTrackerInfo() {
        TrackerRequest req = new TrackerRequest(torrent.getAnnounce(), torrent.getInfo_hash(), self, torrent.getLeft());
        return req.send();
    }

    public TrackerInfo getInfo() {
        if (trackerInfo == null) {
            trackerInfo = fetchTrackerInfo();
        }
        return trackerInfo;
    }
}
