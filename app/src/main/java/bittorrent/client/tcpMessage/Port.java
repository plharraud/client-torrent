package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Port extends BittorrentMessage {
    char listenPort;
    
    public Port(char listenPort) {
        super(3,9);
        this.listenPort = listenPort;
    }

    

    public Port(BittorrentMessage bittorrentMessage) throws IOException {
        super(bittorrentMessage);
        this.listenPort = bittorrentMessage.dataInput.readChar();
    }



    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
        out.writeChar(listenPort);
    }
}
