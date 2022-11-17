package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class Piece extends BittorrentMessage {
    int pieceIndex;
    int pieceBeginOffset;
    byte[] pieceBlock;

    public Piece(int pieceIndex, int pieceBeginOffset, byte[] pieceBlock) {
        super(9 + pieceBlock.length,7);
        this.pieceIndex = pieceIndex;
        this.pieceBeginOffset = pieceBeginOffset;
        this.pieceBlock = pieceBlock;
    }

    public Piece(BittorrentMessage bittorrentMessage) throws IOException {
        super(bittorrentMessage);
        this.pieceIndex = bittorrentMessage.dataInput.readInt();
        this.pieceBeginOffset = bittorrentMessage.dataInput.readInt();
        this.pieceBlock = bittorrentMessage.dataInput.readAllBytes();
    }

    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
        out.writeInt(pieceIndex);
        out.writeInt(pieceBeginOffset);
        out.write(pieceBlock);
    }
}
