package bittorrent.client;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Bitfield {

    // Message Length: 3 00 00 00 03
    // Message Type: Bitfield (5) 05
    // Bitfield data: 0000 00 00
    int length = 3;
    byte[] lengthfill = new byte[3];
    int type = 5;
    byte[] data = new byte[2];

    public void sendBitfield(DataOutputStream out) throws IOException {
        try {
            int sizeinit = out.size();
            out.write(lengthfill);
            out.writeByte(length);
            out.writeByte(type);
            out.write(data);
            if (out.size() - sizeinit != 7) {
                System.out
                        .println("error Bitfield");
            }
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
