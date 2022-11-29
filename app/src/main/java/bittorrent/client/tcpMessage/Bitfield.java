package bittorrent.client.tcpMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

public class Bitfield extends BittorrentMessage {
    byte[] bitfield;

    public Bitfield(DataInputStream dataInput) throws IOException{
        super(dataInput);
        this.bitfield = new byte[super.messageLength -1];
        dataInput.read(this.bitfield);
    }

    public Bitfield(byte[] bitfield) {
        super(1 + bitfield.length, 5);
        this.bitfield = bitfield;
    }

    public Bitfield(BittorrentMessage bittorrentMessage) throws IOException {
        super(bittorrentMessage);
        // Calculate the size of the bytes to read
        this.bitfield = new byte[bittorrentMessage.messageLength -1];
        bittorrentMessage.dataInput.read(this.bitfield);
    }

    public boolean hasPiece(int index){
        BitSet data = BitSet.valueOf(getBitfield());
        int offset = index/8; // Se place sur le bon octet
        int indexLittleEndian = (7 + offset*8) - (index % 8) ; // Converti en little endian sur le bon octet
        return data.get(indexLittleEndian);
    }

    @Override
    public void send(DataOutputStream out) throws IOException {
        super.send(out);
        out.write(bitfield);
        out.flush();
    }
    @Override
    public void handle() {
        // TODO : Add a handle method to the Bitfield message
    }

    @Override
    public String toString() {
        return "Bitfield ["+super.toString()+", bitfield=" + Arrays.toString(bitfield) + "]";
    }

    public byte[] getBitfield() {
        return bitfield;
    }
    
}
