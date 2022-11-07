package bittorrent.client;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Interested {
    byte[] fill = new byte[3];
    int length = 1;
    int type = 2;

    public void sendSeq(DataOutputStream out) throws IOException {
        try {
            int sizeinit = out.size();
            out.write(fill);
            out.writeByte(length);
            out.writeByte(type);
            if (out.size() - sizeinit != 5) {
                System.out
                        .println("error Interested");
            }
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
