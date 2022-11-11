package bittorrent.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PieceBlock {

    byte[] length = new byte[4];
    byte[] type = new byte[1];
    byte[] index = new byte[4];
    byte[] offset = new byte[4];
    byte[] piece_bytes;
    int piece_index;
    int block_index;
    int block_size;
    Request req;

    public PieceBlock(DataInputStream data_in, DataOutputStream data_out, int piece_index, int block_index,
            int block_size) throws IOException {
        this.piece_index = piece_index;
        this.block_index = block_index;
        this.block_size = block_size;
        piece_bytes = new byte[block_size];
        makeRequest(data_out);
        receiveRequest(data_in);
    }

    public void makeRequest(DataOutputStream data_out) throws IOException {
        try {

            req = new Request(piece_index, block_index, block_size);
            req.sendReq(data_out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveRequest(DataInputStream data_in) throws IOException {
        try {
            data_in.read(length);
            data_in.read(type);
            data_in.read(index);
            data_in.read(offset);
            data_in.read(piece_bytes);
            // System.out.println(Utils.bytesToHex(piece_bytes).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBlocks(ByteArrayOutputStream imagebytes) throws IOException {
        try {
            imagebytes.write(piece_bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBlocBytes() {
        return piece_bytes;
    }

}
