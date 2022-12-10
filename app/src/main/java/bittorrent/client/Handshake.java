package bittorrent.client;

import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handshake {

    // 1/19/8/20/20 = 68
    private int name_length;
    private String name;
    private byte[] extension;
    private Hash info_hash;
    private byte[] peer_id;

    private static Logger log = LogManager.getLogger();

    public Handshake(byte[] extension, Hash info_hash, byte[] peer_id) {
        this.name_length = 19;
        this.name = "BitTorrent protocol";
        this.extension = extension;
        this.info_hash = info_hash;
        this.peer_id = peer_id;
        //verifyHandshakeIntegrity(info_hash);
    }

    public Handshake(DataInputStream in) throws IOException {
        try {
            byte[] name_leng = new byte[1];
            byte[] nameb = new byte[19];
            byte[] extension = new byte[8];
            byte[] hash = new byte[20];
            byte[] peerid = new byte[20];

            in.read(name_leng);
            in.read(nameb);
            in.read(extension);
            in.read(hash);
            in.read(peerid);

            this.name_length = Utils.byteArrayToUnsignedInt(name_leng);
            this.name = new String(nameb);
            this.extension = extension;
            this.info_hash = Hash.fromBytes(hash);
            this.peer_id = peerid;
            //verifyHandshakeIntegrity(); // check le hash avec lui meme ?
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHandshake(DataOutputStream out) throws IOException {
        try {
            out.writeByte(getName_length());
            out.writeBytes(getName());
            out.write(getExtension());
            out.write(getInfo_hash().asBytes());
            out.write(getPeer_id());
            if (out.size() != 68) {
                log.error("TAILLE BUFFER INCOHERENTE HANDSHAKE : " + out.size());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int verifyHandshakeIntegrity(Hash hash){
        if(getName_length() != 19 || !getName().equals("BitTorrent protocol")){
            log.error("1 : Is not a valid BitTorrent protocol");
            return 1;
        }
        if(! this.info_hash.equals(hash)){
            log.error("2 : Wrong info hash");
            return 2;
        }
        log.debug("0 : Valid Handshake");
        return 0;
    }

    public int getName_length() {
        return this.name_length;
    }

    public void setName_length(int name_length) {
        this.name_length = name_length;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getExtension() {
        return this.extension;
    }

    public void setExtension(byte[] extension) {
        this.extension = extension;
    }

    public Hash getInfo_hash() {
        return this.info_hash;
    }

    public void setInfo_hash(Hash info_hash) {
        this.info_hash = info_hash;
    }

    public byte[] getPeer_id() {
        return this.peer_id;
    }

    public void setPeer_id(byte[] peer_id) {
        this.peer_id = peer_id;
    }

    @Override
    public String toString() {
        return "{" +
                " name_length='" + getName_length() + "'" +
                ", name='" + getName() + "'" +
                ", extension='" + Utils.bytesToHex(getExtension()) + "'" +
                ", info_hash='" + getInfo_hash().asHex() + "'" +
                ", peer_id='" + Utils.bytesToHex(getPeer_id()) + "'" +
                "}";
    }

}
