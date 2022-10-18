package bittorrent.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Random;
import java.time.temporal.ChronoUnit;
public class TrackerConnect {

    private byte[] info_hash;
    private String announce;
    private byte[] peer_id;
    private int port ;
    private String name; 
    // private int dowloaded;
    // private int uploaded;
    // private int left;
    private TrackerInfo trackerInfo;

    public TrackerConnect(Torrent torrent){
        info_hash = torrent.getInfo_hash();
        announce  = torrent.getAnnounce();
        name = torrent.getName();

        // TODO : Calculate the port with availability
        port = 6881;

        // Initialize a random peer_id
        peer_id = new byte[20];
        new Random().nextBytes(peer_id);

    }

    public HttpResponse<String> trackerRequest(HttpClient httpClient){
        try {
            // Build the http rquest
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(createMinimalUriString()))
            .timeout(Duration.of(10,ChronoUnit.SECONDS))
            .GET()
            .build();
            
            //Once the request is forged, we need to use the httpClient ot send it
            HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());
            return response;

        } catch (URISyntaxException | IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public String createMinimalUriString() {
        return
            announce + '?' +
            "info_hash=" + Utils.byteArrayToURLString(info_hash) + '&' +
            "peer_id=" + Utils.byteArrayToURLString(peer_id) + '&' + 
            "port=" + Integer.valueOf(port).toString();
    }

    // Used for testing purposes, so that  we can generate repeatable client IDS
    public void generateSeededPeerId() {
        peer_id = new byte[20];
        new Random(name.hashCode()).nextBytes(peer_id);
    }

    public TrackerInfo getTrackerInfo() {
        return trackerInfo;
    }
}