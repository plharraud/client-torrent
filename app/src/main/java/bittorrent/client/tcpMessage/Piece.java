package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
        this.pieceBlock = new byte[super.messageLength -9];
        dataInput.read(this.pieceBlock);
    }

    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
        out.writeInt(pieceIndex);
        out.writeInt(pieceBeginOffset);
        out.write(pieceBlock);
    }

    @Override
    public String toString() {
        return "Piece ["+super.toString()+", pieceIndex=" + pieceIndex + ", pieceBeginOffset=" + pieceBeginOffset + ", pieceBlockLength="
                + pieceBlock.length + "]";
    }

    
}
