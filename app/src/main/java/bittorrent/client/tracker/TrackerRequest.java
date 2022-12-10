package bittorrent.client.tracker;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import bittorrent.client.Hash;
import bittorrent.client.Peer;

import java.time.temporal.ChronoUnit;

public class TrackerRequest {

    private static final long REQUEST_TIMEOUT_SECONDS = 5;

    private String announceURL;
    private Hash infoHash;
    private Peer self;

    // bytes left to download until 100%
    private int left;

    public TrackerRequest(String announceURL, Hash infoHash, Peer self, int left) {
        this.announceURL = announceURL;
        this.infoHash = infoHash;
        this.self = self;
        this.left = left;
    }

    public TrackerInfo send() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(getAnnounceURI())
            .timeout(Duration.of(REQUEST_TIMEOUT_SECONDS, ChronoUnit.SECONDS))
            .GET()
            .build();

        HttpResponse<InputStream> response = httpClient.send(httpRequest, BodyHandlers.ofInputStream());
        return new TrackerInfo(response.body());
    }

    private URI getAnnounceURI() throws URISyntaxException {
        return new URI(createCompleteUriString());
    }

    // Complete uri used to make the request to the tracker
    public String createCompleteUriString() {
        return announceURL
            + "?info_hash=" + infoHash
            + "&peer_id=" + self.getId()
            + "&port=" + self.getPort()
            + "&left=" + left
            + "compact=1";
    }

}
