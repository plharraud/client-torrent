package bittorrent.client;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Interested {
    private int length = 1;
    private int type = 2;

    public void sendSeq(DataOutputStream out) throws IOException {
        try {
            int sizeinit = out.size();
            out.writeInt(length);
            out.writeByte(type);
            if (out.size() - sizeinit != 5) {
                System.out
                        .println("error Interested");
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
