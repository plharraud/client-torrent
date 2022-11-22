package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Have extends BittorrentMessage{
    int pieceIndex;

    public Have(int pieceIndex) {
        super(5,4);
        this.pieceIndex = pieceIndex;
    }
    
    public Have(BittorrentMessage bittorrentMessage) throws IOException {
        super(bittorrentMessage);
        this.pieceIndex = bittorrentMessage.dataInput.readInt();
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);
        out.writeInt(pieceIndex);
        out.flush();
    }

    @Override
    public String toString() {
        return "Have ["+super.toString()+", pieceIndex=" + pieceIndex + "]";
    }
    
}
