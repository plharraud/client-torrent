package bittorrent.client;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Handshake {

    // 1/19/8/20/20 = 68
    private String name_length;
    private String name;
    private byte[] extension = new byte[8];
    private byte[] info_hash;
    private byte[] peer_id;

    public Handshake(String name_length, String name, byte[] extension, byte[] info_hash, byte[] peer_id) {
        this.name_length = name_length;
        this.name = name;
        this.extension = extension;
        this.info_hash = info_hash;
        this.peer_id = peer_id;
    }

    public void toByteArray(DataOutputStream out) {
        try {
            out.writeBytes(name_length);
            out.writeBytes(name);
            out.write(extension);
            out.write(info_hash);
            out.write(peer_id);
            if (out.size() != 68) {
                System.out.println("TAILLE BUFFER INCOHERENTE HANDSHAKE : " + out.size());
            }
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
