package bittorrent.client;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Request {
    /*
     * Message Length: 13
     * Message Type: Request (6)
     * Piece index: 0x00000006
     * Begin offset of piece: 0x00000000
     * Piece Length: 0x00004000
     */
    int length = 13;
    byte[] lengthfill = new byte[3];
    int type = 6;
    byte[] index = new byte[4];

    byte[] offset = new byte[4];

    byte[] lengthpiece = new byte[4];

    public Request(int indexi, int part, int lengthpiecei) {
        this.index = Utils.fromUInt32(indexi);
        if (part == 1) {
            this.offset = Utils.fromUInt32(lengthpiecei);
        }
        this.lengthpiece = Utils.fromUInt32(lengthpiecei);
    }

    public void sendReq(DataOutputStream out) throws IOException {
        try {
            int sizeinit = out.size();
            out.write(lengthfill);
            out.writeByte(length);
            out.writeByte(type);
            out.write(index);
            out.write(offset);
            out.write(lengthpiece);
            if (out.size() - sizeinit != 17) {
                System.out
                        .println("error Request");
            }
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
