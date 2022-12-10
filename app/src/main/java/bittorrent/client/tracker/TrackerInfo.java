package bittorrent.client.tracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import be.adaxisoft.bencode.BDecoder;
import be.adaxisoft.bencode.BEncodedValue;
import bittorrent.client.Peer;
import bittorrent.client.Utils;


public class TrackerInfo {

    // interval in seconds between regular requests to the tracker
    private int interval;

    // number of seeders
    private int complete;

    // number of leechers
    private int incomplete;

    private ArrayList<Peer> peers = new ArrayList<Peer>();

    public TrackerInfo(InputStream bencodeResponse) {
        try {
            // Parse the bencode response
            BDecoder reader = new BDecoder(bencodeResponse);
            Map<String, BEncodedValue> document = reader.decodeMap().getMap();
            // Load the values
            //TODO verifier qu'elles sont la avant de les get, ou traiter les erreurs
            interval = document.get("interval").getInt();
            complete = document.get("complete").getInt();
            incomplete = document.get("incomplete").getInt();

            // Load the peers
            // We Create a matrix of peer vector, each vector is PEER_SIZE bytes long  (6) and represents a peer
            byte[] rawPeers = document.get("peers").getBytes();
            byte[][] peersInBytes = Utils.deepenByteArray(rawPeers, Peer.PEER_SIZE);
            for (int i = 0; i < peersInBytes.length; i++){
                Peer peer = new Peer(peersInBytes[i]);
                peers.add(peer);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public ArrayList<Peer> getPeers() {
        return peers;
    }

    @Override
    public String toString() {
        return "TrackerInfo [interval=" + interval + ", complete=" + complete + ", incomplete=" + incomplete + ", peers=" + peers + "]";
    }
}
