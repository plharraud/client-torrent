package bittorrent.client;
import org.junit.jupiter.api.Test;

import bittorrent.client.torrent.Torrent;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class BittorentTrackerTest {

    @Test
    public void testUri1() {
        String expectedUri = "http://127.0.0.1:6969/announce?info_hash=%28%5D%CB%B0%DCZ%E2%EC%B7%8F6%3A%D1%29Z2%1C%8E%BF%AF&peer_id=%3A%0D%E4%D0%CFXu%C5%2Fz%EE%B7b%ED%DF%28%83%A8%24%2F&port=6881";
        TrackerConnect trackerConnect = new TrackerConnect(new Torrent(new File("src/test/resources/torrents/hello_world.txt.torrent")));
        trackerConnect.generateSeededPeerId();
        assertEquals(expectedUri, trackerConnect.createMinimalUriString());
	}
    @Test
    public void testUri2() {
        String expectedUri = "http://127.0.0.1:6969/announce?info_hash=%06q3%AC%E5%DD%0CP%27%B9%9D%E5%D4%BAQ%28%28%20%8D%5B&peer_id=q%1DG%C8%3DN%F7%19%FE3%81Qu%60vZ%D1%3E2%D4&port=6881";
        TrackerConnect trackerConnect = new TrackerConnect(new Torrent(new File("src/test/resources/torrents/iceberg.jpg.torrent")));
        assertNotEquals(expectedUri, trackerConnect.createMinimalUriString());
        System.out.println(trackerConnect.createMinimalUriString());;
	}
    @Test
    public void testUri3() {
        String expectedUri = "http://127.0.0.1:6969/announce?info_hash=%E5%F6i%89%8088M%24%0A%0FcVX%2B5%CA1%81%A6&peer_id=%9C%B6%9A%82%D8%F1%D5.%A4%EA%B6%9F%9A%EF%BC%B8%0B5%FCR&port=6881";
        TrackerConnect trackerConnect = new TrackerConnect(new Torrent(new File("src/test/resources/torrents/test.torrent")));
        trackerConnect.generateSeededPeerId();
        assertEquals(expectedUri, trackerConnect.createMinimalUriString());
	}
    // @Test
    // public void contactTracker() {
    //     String expectedTrackerResponse = "";
    //     HttpClient httpClient = HttpClient.newHttpClient();
    //     TrackerConnect trackerConnect = new TrackerConnect(new Torrent(new File("src/test/resources/torrents/CuteTogepi.jpg.torrent")));
    //     String trackerResponse = trackerConnect.trackerRequest(httpClient).body();
    //     System.out.println(trackerResponse);
    // }
}
