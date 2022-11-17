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
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
    }
}
