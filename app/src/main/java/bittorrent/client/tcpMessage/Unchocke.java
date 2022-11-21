package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Unchocke extends BittorrentMessage {

    public Unchocke() {
        super(1,1);
    }

    public Unchocke(BittorrentMessage bittorrentMessage) {
        super(bittorrentMessage);
    }

    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
    }

    @Override
    public String toString() {
        return "Unchocke ["+super.toString()+"]";
    }

    
}
