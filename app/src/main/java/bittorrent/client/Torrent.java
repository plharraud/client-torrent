package bittorrent.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import be.adaxisoft.bencode.BDecoder;
import be.adaxisoft.bencode.BEncodedValue;
import be.adaxisoft.bencode.BEncoder;
import be.adaxisoft.bencode.InvalidBEncodingException;

public class Torrent {

	//This class is just an example to bootstrap but you may change everything in it even the name
	private byte[] info_hash = new byte[20];
  public Torrent() {
  }
	
	public Torrent(File file) {
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		
	}

	public byte[] getInfo_hash() {
		return info_hash;
	}
	
	public String getInfo_hash_hex() {
		return Utils.bytesToHex(info_hash);
	}

}
