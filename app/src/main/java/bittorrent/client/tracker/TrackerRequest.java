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
import bittorrent.client.Utils;
import bittorrent.client.torrent.Torrent;

import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrackerRequest {

    private static final long REQUEST_TIMEOUT_SECONDS = 5;

    private String announceURL;
    private Hash infoHash;
    private Peer self;
    private int left; // bytes left to download until 100%

    Logger log = LogManager.getLogger();

    public TrackerRequest(Torrent torrent, Peer self) {
        this.announceURL = torrent.getMetaInfo().getAnnounce();
        this.infoHash = torrent.getMetaInfo().getInfoHash();
        this.self = self;
        this.left = torrent.getLeft();
    }

    public TrackerInfo send() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(getAnnounceURI())
            .timeout(Duration.of(REQUEST_TIMEOUT_SECONDS, ChronoUnit.SECONDS))
            .GET()
            .build();

        log.debug("announcing at {}", getAnnounceURI());
        HttpResponse<InputStream> response = httpClient.send(httpRequest, BodyHandlers.ofInputStream());
        return new TrackerInfo(response.body());
    }

    private URI getAnnounceURI() throws URISyntaxException {
        return new URI(createCompleteUriString());
    }

    // Complete uri used to make the request to the tracker
    public String createCompleteUriString() {
        return announceURL
            + "?info_hash=" + Utils.byteArrayToURLString(infoHash.asBytes())
            + "&peer_id=" + Utils.byteArrayToURLString(self.getId())
            + "&port=" + self.getPort()
            + "&left=" + left
            + "&compact=1";
    }
}
