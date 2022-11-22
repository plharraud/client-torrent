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
    public void send(DataOutputStream out) throws IOException {
        super.send(out);
        out.writeChar(listenPort);
        out.flush();
    }



    @Override
    public String toString() {
        return "Port ["+super.toString()+", listenPort=" + listenPort + "]";
    }
    
}
