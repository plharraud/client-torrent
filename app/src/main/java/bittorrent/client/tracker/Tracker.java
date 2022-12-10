package bittorrent.client.tracker;

import bittorrent.client.Peer;
import bittorrent.client.torrent.Torrent;

public class Tracker {

    private Torrent torrent;
    private Peer self;
    private TrackerInfo trackerInfo;

    public Tracker(Torrent torrent, Peer self) {
        this.torrent = torrent;
        this.self = self;
    }

    private TrackerInfo fetchTrackerInfo() throws Exception {
        TrackerRequest req = new TrackerRequest(torrent, self);
        return req.send();
    }

    public TrackerInfo getInfo() throws Exception {
        if (trackerInfo == null) {
            trackerInfo = fetchTrackerInfo();
        }
        return trackerInfo;
    }
}
