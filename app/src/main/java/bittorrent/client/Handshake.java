package bittorrent.client;

import java.io.*;

public class Handshake {

    // 1/19/8/20/20 = 68
    private int name_length;
    private String name;
    private byte[] extension;
    private byte[] info_hash;
    private byte[] peer_id;

    public Handshake(byte[] extension, byte[] info_hash, byte[] peer_id) {
        this.name_length = 19;
        this.name = "BitTorrent protocol";
        this.extension = extension;
        this.info_hash = info_hash;
        this.peer_id = peer_id;
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

            setName_length(Utils.byteArrayToUnsignedInt(name_leng));
            setName(new String(nameb));
            setExtension(extension);
            setInfo_hash(hash);
            setPeer_id(peerid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHandshake(DataOutputStream out) throws IOException {
        try {
            out.writeByte(name_length);
            out.writeBytes(name);
            out.write(extension);
            out.write(info_hash);
            out.write(peer_id);
            if (out.size() != 68) {
                System.out
                        .println("TAILLE BUFFER INCOHERENTE HANDSHAKE : " + out.size());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int verifyHandshakeIntegrity(byte[] actual_info_hash){
        if(getName_length() != 19 || !getName().equals("BitTorrent protocol")){
            System.out.println("1 : Is not a valid BitTorrent protocol");
            return 1;
        }
        if(getInfo_hash() != actual_info_hash){
            System.out.println("2 : Wrong info hash");
            return 2;
        }
        System.out.println("0 : Valid Handshake");
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

    public byte[] getInfo_hash() {
        return this.info_hash;
    }

    public void setInfo_hash(byte[] info_hash) {
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
                ", info_hash='" + Utils.bytesToHex(getInfo_hash()) + "'" +
                ", peer_id='" + Utils.bytesToHex(getPeer_id()) + "'" +
                "}";
    }

}
