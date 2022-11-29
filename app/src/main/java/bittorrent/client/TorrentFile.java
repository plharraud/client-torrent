package bittorrent.client;

import java.io.*;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TorrentFile {
    final int maxsizepacket = 16384;
    private int length;
    private int piece_length;
    private int piece_parts;
    private int last_piece_parts;
    private int last_block_size;
    private Piece[] torrentFile;
    private ByteArrayOutputStream imagebytes;

    private static Logger log = LogManager.getLogger();

    public TorrentFile(int length, int piecelength) {
        this.length = length;
        this.piece_length = piecelength;
        this.piece_parts = (int) Math.ceil((float) length / (float) piecelength);
        this.last_piece_parts = fillPieceInfo();
        this.last_block_size = length - piece_length * (piece_parts - 1);
        if (last_piece_parts == 2) {
            this.last_block_size -= maxsizepacket;
        }
        displayTorrentInfos();
        torrentFile = new Piece[piece_parts];
    }

    public void displayTorrentInfos() {
        log.debug("torrent length : " + length);
        log.debug("nb of pieces : " + piece_parts);
        log.debug("piece length : " + piece_length);
        log.debug("last block : " + last_block_size);
        log.debug("download rate : " + maxsizepacket);
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
        try (OutputStream out = new BufferedOutputStream(
                new FileOutputStream(filePath))) {
            out.write(imagebytes.toByteArray());
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
