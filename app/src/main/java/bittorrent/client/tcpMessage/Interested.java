package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Interested extends BittorrentMessage {
    

    public Interested() {
        super(1,2);
    }


    public Interested(BittorrentMessage bittorrentMessage) {
        super(bittorrentMessage);
    }



    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
    }


    @Override
    public String toString() {
        return "Interested ["+super.toString()+"]";
    }
    
}
