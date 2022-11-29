package bittorrent.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import be.adaxisoft.bencode.BDecoder;
import be.adaxisoft.bencode.BEncodedValue;


public class TrackerInfo {
    // tracker response with a single peer
    // private String correctResponse = "d8:completei0e10:downloadedi0e10:incompletei1e8:intervali1976e12:min intervali988e5:peers6:??e";

    private int interval;
    private int complete;
    private int incomplete;
    private Peer[] peers;
    public ArrayList<Peer> peersList = new ArrayList<Peer>();

    TrackerInfo(InputStream bencodeResponse) {
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
            peers = new Peer[peersInBytes.length];
            for (int i = 0; i < peersInBytes.length; i++){
                Peer peer = new Peer(peersInBytes[i]);
                peers[i] = peer;
                peersList.add(peer);
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

    public Peer[] getPeers() {
        return peers;
    }

    @Override
    public String toString() {
        return "TrackerInfo [interval=" + interval + ", complete=" + complete + ", incomplete=" + incomplete + ", peers=" + Arrays.toString(peers) + "]";
    }


    
}
