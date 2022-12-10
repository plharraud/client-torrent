package bittorrent.client.torrent;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import be.adaxisoft.bencode.BDecoder;
import be.adaxisoft.bencode.BEncodedValue;
import be.adaxisoft.bencode.BEncoder;
import be.adaxisoft.bencode.InvalidBEncodingException;
import bittorrent.client.Utils;

public class Torrent {

	public Torrent(File torrent, File targetDir) {
		TorrentFile torrentFile = new TorrentFile(torrent);
		TargetFile   targetFile = new TargetFile(FilenameUtils.concat(targetDir.getPath(), torrentFile.getName()))

		FileInputStream fileInputStream = new FileInputStream(file);

		BDecoder reader = new BDecoder(fileInputStream);
		Map<String, BEncodedValue> document = reader.decodeMap().getMap();

		announce = document.get("announce").getString();

		infoMap = document.get("info").getMap();
		parseInfoMap(infoMap);
		computeInfoHash(infoMap);
		this.pieces_number = (int) Math.ceil((float) length / (float) piece_length);
	}

	private void parseInfoMap(Map<String, BEncodedValue> infoMap) {
		try {
			name = infoMap.get("name").getString();
			piece_length = infoMap.get("piece length").getInt();
			length = infoMap.get("length").getInt();
			pieces = infoMap.get("pieces").getString();
		} catch (InvalidBEncodingException e) {
			
		}
	}

	// Compute info hash
	// reencodes the map as a byte array to feed in the digest algorithm
	private void computeInfoHash(Map<String, BEncodedValue> infoMap) {
		try {
			ByteBuffer infoBytesBuffer = BEncoder.encode(infoMap);
			byte[] infoBytes = new byte[infoBytesBuffer.remaining()];
			infoBytesBuffer.get(infoBytes);
	
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1"); 
			sha1.update(infoBytes, 0, infoBytes.length);
			info_hash = sha1.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getInfo_hash() {
		return info_hash;
	}
	
	public String getInfo_hash_hex() {
		return Utils.bytesToHex(info_hash);
	}

	public String getAnnounce() {
		return announce;
	}

	public String getName() {
		return name;
	}

	public Integer getLength() {
		return length;
	}

	public Integer getPiece_length() {
		return piece_length;
	}

	public String getPieces() {
		return pieces;
	}

	public Integer getNumberOfPieces(){
		return pieces_number;
	}
}
