package bittorrent.client.tcpMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Bitfield extends BittorrentMessage {
    byte[] bitfield;

    Bitfield(DataInputStream dataInput) throws IOException{
        super(dataInput);
        this.bitfield =  dataInput.readAllBytes();
    }

    Bitfield(byte[] bitfield) {
        super(1 + bitfield.length, 5);
        this.bitfield = bitfield;
    }

    Bitfield(BittorrentMessage bittorrentMessage) throws IOException {
        super(bittorrentMessage);
        this.bitfield =  bittorrentMessage.dataInput.readAllBytes();
    }

    @Override
    public void build(DataOutputStream out) throws IOException {
        super.build(out);
        out.write(bitfield);
    }
    @Override
    public void handle() {
        // TODO : Add a handle method to the Bitfield message
    }

    @Override
    public String toString() {
        return "Bitfield ["+super.toString()+", bitfield=" + Arrays.toString(bitfield) + "]";
    }
    
}
