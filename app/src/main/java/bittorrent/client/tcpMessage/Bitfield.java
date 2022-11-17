package bittorrent.client.tcpMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.BitSet;


public class Bitfield extends BittorrentMessage {
    BitSet bitfield;

    Bitfield(DataInputStream dataInput) throws IOException{
        super(dataInput);
        this.bitfield =  BitSet.valueOf(dataInput.readAllBytes());
    }

    Bitfield(BitSet bitfield) {
        super(1 + bitfield.toByteArray().length, 5);
        this.bitfield = bitfield;
    }

    Bitfield(BittorrentMessage bittorrentMessage) throws IOException {
        super(bittorrentMessage);
        this.bitfield =  BitSet.valueOf(bittorrentMessage.dataInput.readAllBytes());
    }

    public Boolean isPieceAvailable(int index) {
        return bitfield.get(index);
    }

    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
        out.write(bitfield.toByteArray());
    }
    @Override
    public void handle() {
        // TODO : Add a handle method to the Bitfield message
    }
}
