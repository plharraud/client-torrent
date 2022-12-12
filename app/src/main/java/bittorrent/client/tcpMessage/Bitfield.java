package bittorrent.client.tcpMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.Utils;

public class Bitfield extends BittorrentMessage {

    Logger log = LogManager.getLogger();

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
        this.bitfield = new byte[bittorrentMessage.messageLength - 1];
        bittorrentMessage.dataInput.read(this.bitfield);
    }

    public boolean hasPiece(int index) {
        return (bitfield[index/8] & 1 << (7-index % 8)) != 0;
    }

    public void havePiece(int index) {
        // log.trace("index={}, bitfield[{}] |= {} (bit {})", index, index/8, 1 << (index %8), index % 8);
        bitfield[index/8] |= 1 << (7-index % 8);
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
        return "Bitfield ["+ super.toString() +", bitfield=" + Utils.byteArraytoBin(bitfield) + "]";
    }

    public byte[] getBitfield() {
        return bitfield;
    }
}
