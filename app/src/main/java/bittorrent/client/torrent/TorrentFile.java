package bittorrent.client.torrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.adaxisoft.bencode.BDecoder;
import be.adaxisoft.bencode.BEncodedValue;
import be.adaxisoft.bencode.BEncoder;
import bittorrent.client.Hash;
import bittorrent.client.Utils;

public class TorrentFile {

    public static Logger log = LogManager.getLogger();

    private Hash infoHash;

    private String announce;
    private String comment;

    private String name;
    private int length;
    private int pieceLength;
    private int piecesAmount;
    private List<Hash> hashes = new ArrayList<Hash>();

    public TorrentFile(File file) throws IOException, BadTorrentFileException, NoSuchAlgorithmException {
        log.info("parsing {}", file.getName());
        FileInputStream in = new FileInputStream(file);

        BDecoder reader = new BDecoder(in);
        Map<String, BEncodedValue> document = reader.decodeMap().getMap();

        announce = document.get("announce").getString();
        comment = document.get("comment").getString();
        log.info("announce: {}, comment: {}", announce, comment);

        Map<String, BEncodedValue> info = document.get("info").getMap();
        computeInfoHash(info);
        log.info("info hash is {}", infoHash);

        name = info.get("name").getString();
        length = info.get("length").getInt();
        pieceLength = info.get("piece length").getInt();
        piecesAmount = (int) Math.ceil(length / (float) pieceLength);
        log.info("name={}, length={}, piece length={}, pieces={}", name, length, pieceLength, piecesAmount);

        byte[] pieces = info.get("pieces").getBytes();
        for (int i = 0; i < pieces.length; i += 20) {
            byte[] hashBytes = Arrays.copyOfRange(pieces, i, i+20);
            log.debug("piece {}, hash={}", i/20, Utils.bytesToHex(hashBytes));
            hashes.add(Hash.fromBytes(hashBytes));
        }

        if (piecesAmount != hashes.size()) {
            log.error("pieces amount differ ! len/piecelen ({}) != number of hashes in list ({}=", piecesAmount, hashes.size());
            throw new BadTorrentFileException("calculated piece count and hashes count don't match");
        }
    }

	private void computeInfoHash(Map<String, BEncodedValue> info) throws IOException, NoSuchAlgorithmException {
        ByteBuffer infoBytesBuffer = BEncoder.encode(info);
        byte[] infoBytes = new byte[infoBytesBuffer.remaining()];
        infoBytesBuffer.get(infoBytes);
        infoHash = new Hash(infoBytes);
	}

    public Hash getInfoHash() {
        return infoHash;
    }

    public String getAnnounce() {
        return this.announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPieceLength() {
        return this.pieceLength;
    }

    public void setPieceLength(int pieceLength) {
        this.pieceLength = pieceLength;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPiecesAmount() {
        return piecesAmount;
    }

    public List<Hash> getHashes() {
        return this.hashes;
    }

    public void setHashes(List<Hash> hashes) {
        this.hashes = hashes;
    }
}
