package bittorrent.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TorrentFile {
    final int maxsizepacket = 16384;
    int length;
    int piece_length;
    int piece_parts;
    int last_piece_parts;
    int last_block_size;
    Piece[] torrentFile;

    public TorrentFile(int length, int piecelength) {
        this.length = length;
        this.piece_length = piecelength;
        this.piece_parts = (int) Math.ceil((float) length / (float) piecelength);
        this.last_piece_parts = fillPieceInfo();
        this.last_block_size = length - piece_length * (piece_parts - 1);
        if (last_piece_parts == 2) {
            this.last_block_size -= maxsizepacket;
        }
        System.out.println("last block : " + last_block_size);
        torrentFile = new Piece[piece_parts];
    }

    public void Leeching100(DataInputStream data_in, DataOutputStream data_out) {
        for (int index = 0; index < piece_parts - 1; index++) {
            Piece piece = new Piece(data_in, data_out, index);
            addPiece(piece);
        }
        Piece piecefinal = new Piece(data_in, data_out, piece_parts - 1, last_piece_parts, last_block_size);
        addPiece(piecefinal);
    }

    public void addPiece(Piece piece) {
        torrentFile[piece.getIndex()] = piece;
    }

    public void writeBytesBlocks() {
        try {
            ByteArrayOutputStream imagebytes = new ByteArrayOutputStream();
            for (int i = 0; i < piece_parts; i++) {
                torrentFile[i].writePiece(imagebytes);
            }
            generateJpg(imagebytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateJpg(ByteArrayOutputStream imagebytes) {
        try (OutputStream out = new BufferedOutputStream(
                new FileOutputStream("src/test/results/test.jpg"))) {
            out.write(imagebytes.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int fillPieceInfo() {
        if ((length - (piece_length - 1) * piece_parts) > maxsizepacket) {
            return 2;
        } else {
            return 1;
        }
    }
}
