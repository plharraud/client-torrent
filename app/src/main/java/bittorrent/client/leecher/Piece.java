package bittorrent.client.leecher;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bittorrent.client.Hash;

public class Piece {

    final int maxsizepacket = 16384;
    PieceBlock[] piece;
    byte[] piecesConcat;
    int parts;
    int index;
    Hash hash;

    public Piece(DataInputStream data_in, DataOutputStream data_out, int index) {
        try {
            this.index = index;
            this.parts = 2;
            piece = new PieceBlock[this.parts];
            for (int i = 0; i < 2; i++) {
                piece[i] = new PieceBlock(data_in, data_out, index, i, maxsizepacket);
            }
            // addPiecesBytes();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Piece(DataInputStream data_in, DataOutputStream data_out, int index, int parts, int last_block_size) {
        try {
            this.index = index;
            this.parts = parts;
            if (parts == 2) {
                piece = new PieceBlock[2];
                piece[0] = new PieceBlock(data_in, data_out, index, 0, maxsizepacket);
                piece[1] = new PieceBlock(data_in, data_out, index, 1, last_block_size);
            } else {
                piece = new PieceBlock[1];
                piece[0] = new PieceBlock(data_in, data_out, index, 0, last_block_size);
            }
            // addPiecesBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * public void addPiecesBytes() {
     * try {
     * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     * for (int i = 0; i < parts; i++) {
     * outputStream.write(piece[i].getBlocBytes());
     * }
     * piecesConcat = outputStream.toByteArray();
     * System.out.println("ah" + Utils.bytesToHex(piecesConcat).toString());
     * } catch (IOException e) {
     * e.getStackTrace();
     * }
     * }
     */

    public void writePiece(ByteArrayOutputStream imagebytes) {
        try {
            for (int i = 0; i < parts; i++) {
                piece[i].writeBlocks(imagebytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes() {
        return piecesConcat;
    }

    public int getIndex() {
        return index;
    }

    public byte[] getFullBytes() {
        return piecesConcat;
    }

}
