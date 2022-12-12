package bittorrent.client.torrent;

import java.io.*;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.Piece;

public class TorrentFile_ {
    final int maxsizepacket = 16384;
    private int length;
    private int piece_length;
    private int piece_parts;
    private int last_piece_parts;
    private int last_block_size;
    private Piece[] torrentFile;
    private ByteArrayOutputStream imagebytes;

    private static Logger log = LogManager.getLogger();

    public TorrentFile_(int length, int piecelength) {
        this.length = length;
        this.piece_length = piecelength;
        this.piece_parts = (int) Math.ceil((float) length / (float) piecelength);
        this.last_piece_parts = fillPieceInfo();
        this.last_block_size = length - piece_length * (piece_parts - 1);
        if (last_piece_parts == 2) {
            this.last_block_size -= maxsizepacket;
        }
        torrentFile = new Piece[piece_parts];
    }

    public void Leeching100(DataInputStream data_in, DataOutputStream data_out) {
        for (int index = 0; index < piece_parts - 1; index++) {
            Piece piece = new Piece(data_in, data_out, index);
            addPiece(piece);
        }
        Piece piecefinal = new Piece(data_in, data_out, piece_parts - 1, last_piece_parts, last_block_size);
        addPiece(piecefinal);
        log.info("✔ Leeching 100% complete");
    }

    public byte[] convertJPGtoBytes(File jpg){
        try {
            byte[] fileContent = Files.readAllBytes(jpg.toPath());
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPiece(Piece piece) {
        torrentFile[piece.getIndex()] = piece;
    }

    public void generateFile(String filePath){
        writeFullBytes();
        bytesToFile(filePath);
    }
    public void writeFullBytes(){
        imagebytes = new ByteArrayOutputStream();
        for (int i = 0; i < piece_parts; i++) {
            torrentFile[i].writePiece(imagebytes);
        }
    }

    public void bytesToFile(String filePath) {
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
            out.write(imagebytes.toByteArray());
            out.close();
            log.info("✔ File generated");
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

    public byte[] getImageBytesAray() {
        return imagebytes.toByteArray();
    }

}
