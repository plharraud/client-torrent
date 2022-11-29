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
        this.pieceBlock = new byte[super.messageLength -9];
        dataInput.read(this.pieceBlock);
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);
        out.writeInt(pieceIndex);
        out.writeInt(pieceBeginOffset);
        out.write(pieceBlock);
        out.flush();
    }

    @Override
    public String toString() {
        return "Piece ["+super.toString()+", pieceIndex=" + pieceIndex + ", pieceBeginOffset=" + pieceBeginOffset + ", pieceBlockLength="
                + pieceBlock.length + "]";
    }

    
}
