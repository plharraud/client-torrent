package bittorrent.client;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bittorrent.client.torrent.Torrent;
import bittorrent.client.torrent.TorrentFile;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class TorrentFileTest {

	@Test
	public void test0() {
        Configurator.setAllLevels("", Level.ALL);
        File f = new File("../dl/torrents/iceberg.jpg.torrent");
        try {
            TorrentFile t = new TorrentFile(f);
        } catch (Exception e) {
            // TODO: handle exception
        }
	}

}
