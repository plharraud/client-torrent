package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Choke extends BittorrentMessage {

    public Choke() {
        super(1,0);
    }

    
    public Choke(BittorrentMessage bittorrentMessage) {
        super(bittorrentMessage);
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
       super.send(out);
       out.flush();
    }


    @Override
    public String toString() {
        return "Choke ["+super.toString()+"]";
    }
    
}
