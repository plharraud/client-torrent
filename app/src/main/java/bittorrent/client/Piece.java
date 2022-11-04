package bittorrent.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Piece {
    private byte[] length = new byte[4];
    private byte[] type = new byte[1];
    private byte[] index = new byte[4];
    private byte[] offset = new byte[4];
    private byte[] piece = new byte[16384];

    public Piece(DataInputStream in, int remaininglength, int maxsizepacket) throws IOException {
        in.read(length);
        in.read(type);
        in.read(index);
        in.read(offset);
        if (remaininglength < maxsizepacket) {
            piece = new byte[remaininglength];
            System.out.println("last piece length : " + remaininglength);
        }
        in.read(piece);
    }

    public void writePieceToFile(ByteArrayOutputStream b) throws IOException {
        b.write(piece);
    }

    public byte[] getLength() {
        return this.length;
    }

    public void setLength(byte[] length) {
        this.length = length;
    }

    public byte[] getType() {
        return this.type;
    }

    public void setType(byte[] type) {
        this.type = type;
    }

    public byte[] getIndex() {
        return this.index;
    }

    public void setIndex(byte[] index) {
        this.index = index;
    }

    public byte[] getOffset() {
        return this.offset;
    }

    public void setOffset(byte[] offset) {
        this.offset = offset;
    }

    public byte[] getPiece() {
        return this.piece;
    }

    public void setPiece(byte[] piece) {
        this.piece = piece;
    }

}
