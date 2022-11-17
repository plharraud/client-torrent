package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Request extends BittorrentMessage {
    int pieceIndex;
    int pieceBeginOffset;
    int pieceLength;

    public Request(int pieceIndex, int pieceBeginOffset, int pieceLength) {
        super(13,6);
        this.pieceIndex = pieceIndex;
        this.pieceBeginOffset = pieceBeginOffset;
        this.pieceLength = pieceLength;
    }
    
    public Request(BittorrentMessage bittorrentMessage) throws IOException {
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
}
