package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Unchoke extends BittorrentMessage {

    public Unchoke() {
        super(1,1);
    }

    public Unchoke(BittorrentMessage bittorrentMessage) {
        super(bittorrentMessage);
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);
        out.flush();
    }

    @Override
    public String toString() {
        return "Unchocke ["+super.toString()+"]";
    }

    
}
