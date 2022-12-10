package bittorrent.client.tracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.adaxisoft.bencode.BDecoder;
import be.adaxisoft.bencode.BEncodedValue;
import be.adaxisoft.bencode.InvalidBEncodingException;
import bittorrent.client.Peer;
import bittorrent.client.Utils;

public class TrackerInfo {

    Logger log = LogManager.getLogger();

    // interval in seconds between regular requests to the tracker
    private int interval;

    // number of seeders
    private int complete;

    // number of leechers
    private int incomplete;

    private List<Peer> peers = new ArrayList<Peer>();

    public TrackerInfo(InputStream bencodeResponse) throws IOException {
        try {
            // Parse the bencode response
            BDecoder reader = new BDecoder(bencodeResponse);
            Map<String, BEncodedValue> document = reader.decodeMap().getMap();
            // Load the values
            //TODO verifier qu'elles sont la avant de les get, ou traiter les erreurs
            interval = document.get("interval").getInt();
            complete = document.get("complete").getInt();
            incomplete = document.get("incomplete").getInt();
            log.info("interval={}, complete (seeders)={}, incomplete (leechers)={}", interval, complete, incomplete);

            // Load the peers
            // We Create a matrix of peer vector, each vector is PEER_SIZE bytes long  (6) and represents a peer
            byte[] rawPeers = document.get("peers").getBytes();
            byte[][] peersInBytes = Utils.deeperByteArray(rawPeers, Peer.PEER_SIZE);
            for (int i = 0; i < peersInBytes.length; i++){
                Peer peer = new Peer(peersInBytes[i]);
                log.debug("peer {}, {}", i, peer);
                peers.add(peer);
            }

        } catch (InvalidBEncodingException e) {
            log.error(e.getMessage());
        }
    }

    public int getInterval() {
        return interval;
    }

    public int getComplete() {
        return complete;
    }

    public int getIncomplete() {
        return incomplete;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    @Override
    public String toString() {
        return "TrackerInfo [interval=" + interval + ", complete=" + complete + ", incomplete=" + incomplete + ", peers=" + peers + "]";
    }
}
