package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class NotInterested extends BittorrentMessage {

    public NotInterested() {
        super(1,3);
    }

    public NotInterested(BittorrentMessage bittorrentMessage) {
        super(bittorrentMessage);
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);
        out.flush();
    }

    @Override
    public String toString() {
        return "NotInterested ["+super.toString()+"]";
    }
}
