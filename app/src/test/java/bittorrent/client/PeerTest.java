package bittorrent.client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeerTest {
    @Test
    public void loadPeer(){
        byte[] byteArrayCorrect = {0b00000001,0b00000010,0b00000100,0b00001000,0b00010000,0b00000110};
        Peer peer = new Peer(byteArrayCorrect);
        System.out.println(peer.getIp());
        assertEquals("1.2.4.8", peer.getIp());
        System.out.println(peer.getPort());
        assertEquals("4102", peer.getPort());
    }

    @Test
    public void loadInvalidPeer(){
        byte[] byteArrayIncorrect = {10,20,30,40,50,60,70};
        assertThrows(Throwable.class, () -> {
            new Peer(byteArrayIncorrect);
        });
    }
}
