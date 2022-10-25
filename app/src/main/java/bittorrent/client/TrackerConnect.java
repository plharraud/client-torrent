package bittorrent.client;

import java.io.IOException;
import java.io.InputStream;
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
    private int port;
    private String name;
    // private int dowloaded;
    // private int uploaded;
    // private int left;
    private TrackerInfo trackerInfo;

    public TrackerConnect(Torrent torrent) {
        info_hash = torrent.getInfo_hash();
        announce = torrent.getAnnounce();
        name = torrent.getName();
        // Initialize a random peer_id
        peer_id = new byte[20];
        new Random().nextBytes(peer_id);

    }

    public HttpResponse<InputStream> trackerRequest() {
        try {

            // Build the http rquest
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(createMinimalUriString()))
                    .timeout(Duration.of(10, ChronoUnit.SECONDS))
                    .GET()
                    .build();

            // Once the request is forged, we need to use the httpClient ot send it
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<InputStream> response = httpClient.send(httpRequest, BodyHandlers.ofInputStream());
            return response;

        } catch (URISyntaxException | IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    // Minimal uri used to make the request to the tracker
    public String createMinimalUriString() {
        return announce + '?' +
                "info_hash=" + Utils.byteArrayToURLString(info_hash) + '&' +
                "peer_id=" + Utils.byteArrayToURLString(peer_id) + '&' +
                "port=" + Integer.valueOf(port).toString();
    }

    // Used for testing purposes, so that we can generate repeatable client IDS
    // We use the filename as the seed of the random number generator
    public void generateSeededPeerId() {
        peer_id = new byte[20];
        new Random(name.hashCode()).nextBytes(peer_id);
    }

    public TrackerInfo getTrackerInfo() {
        // If we have no tracker info, we request it from the tracker
        if (trackerInfo == null) {
            trackerInfo = new TrackerInfo(trackerRequest().body());
        }
        return trackerInfo;
    }

    public byte[] getPeer_id() {
        return this.peer_id;
    }
}