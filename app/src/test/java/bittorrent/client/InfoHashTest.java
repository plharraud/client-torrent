package bittorrent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class InfoHashTest {
	
  @Test
  public void test0() {
    Torrent t = new Torrent();
    assertEquals("0000000000000000000000000000000000000000", t.getInfo_hash_hex());
  }
	
	@Test
	public void testFile1()  {
		Torrent t = new Torrent(new File("src/main/resources/torrents/hello_world.txt.torrent"));
		assertEquals("285DCBB0DC5AE2ECB78F363AD1295A321C8EBFAF", t.getInfo_hash_hex());
	}
	
	@Test
	public void testFile2()  {
		Torrent t = new Torrent(new File("src/main/resources/torrents/iceberg.jpg.torrent"));
		assertEquals("067133ACE5DD0C5027B99DE5D4BA512828208D5B", t.getInfo_hash_hex());
	}
	
	@Test
	public void testFile3()  {
		Torrent t = new Torrent(new File("src/main/resources/torrents/test.torrent"));
		assertEquals("e5f669898038384d240a0f6356582b35ca3181a6".toUpperCase(), t.getInfo_hash_hex());
	}

}
