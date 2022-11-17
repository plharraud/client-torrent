package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Cancel extends BittorrentMessage {
    int pieceIndex;
    int pieceBeginOffset;
    int pieceLength;
    
    public Cancel(BittorrentMessage bittorrentMessage, int pieceIndex, int pieceBeginOffset, int pieceLength) {
        super(13,8);
        this.pieceIndex = pieceIndex;
        this.pieceBeginOffset = pieceBeginOffset;
        this.pieceLength = pieceLength;
    }
    
    public Cancel(BittorrentMessage bittorrentMessage) throws IOException {
        super(bittorrentMessage);
        this.pieceIndex = bittorrentMessage.dataInput.readInt();
        this.pieceBeginOffset = bittorrentMessage.dataInput.readInt();
        this.pieceLength = bittorrentMessage.dataInput.readInt();

        
    }

    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
        out.writeInt(pieceIndex);
        out.writeInt(pieceBeginOffset);
        out.writeInt(pieceLength);
    }

    @Override
    public String toString() {
        return "Cancel ["+super.toString()+", pieceIndex=" + pieceIndex + ", pieceBeginOffset=" + pieceBeginOffset + ", pieceLength="
                + pieceLength + "]";
    }
    
}
