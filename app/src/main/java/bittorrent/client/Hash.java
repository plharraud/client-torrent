package bittorrent.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hash {

    Logger log = LogManager.getLogger();

    private byte[] hash;

    public Hash() {}

    public Hash(byte[] message) {
        hash = sha1(message);
    }

    public Hash(String message) {
        hash = sha1(message.getBytes());
    }

    public byte[] sha1(byte[] message) {
        return sha1len(message, message.length);
    }

    public byte[] sha1len(byte[] message, int length) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1"); 
            sha1.update(message, 0, length);
            return sha1.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("sha1 not supported");
        }
        return null;
    }

    public static Hash fromBytes(byte[] hashBytes) {
        Hash h = new Hash();
        h.setHash(hashBytes);
        return h;
    }

    private void setHash(byte[] hashBytes) {
        if (hashBytes.length == 20) {
            this.hash = hashBytes;
        } else {
            log.error("hash too long ({} bytes, should be 20)", hashBytes.length);
        }
    }

    public byte[] asBytes() {
        return hash;
    }

    public String asHex() {
        return Utils.bytesToHex(hash);
    }

    @Override
    public String toString() {
        return asHex();
    }

    public Boolean equals(Hash h) {
        return Arrays.equals(this.hash, h.asBytes());
    }
}
