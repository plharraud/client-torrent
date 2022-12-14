package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class KeepAlive extends BittorrentMessage {

    public KeepAlive() {
        super(0,-1);
    }

    public KeepAlive(BittorrentMessage bittorrentMessage) {
        super(bittorrentMessage);
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        out.writeInt(0);
        out.flush();
    }

    @Override
    public String toString() {
        return "KeepAlive ["+super.toString()+"]";
    }
}
