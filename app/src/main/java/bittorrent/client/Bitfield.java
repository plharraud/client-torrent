package bittorrent.client;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Bitfield {

    // Message Length: 3 00 00 00 03
    // Message Type: Bitfield (5) 05 1o
    // Bitfield data: 0000 0000 2o
    private int length;
    private byte[] typeByte;
    private int type;
    private byte[] data;
    
    public Bitfield(DataInputStream bitfield_received) throws IOException{ // Bitfield from another client
        try{
            this.length = bitfield_received.readInt();
            this.typeByte = new byte[1];
            bitfield_received.read(this.typeByte); // = 1 octet qui vaut x05
            this.type = Utils.byteArrayToUnsignedInt(this.typeByte);
            this.data = new byte[this.length - 1];
            bitfield_received.read(this.data);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public Bitfield(){ //Bitfield from our client
        //Version leeching 0%
        this.length = 3;
        this.type = 5;
        this.typeByte = Utils.intToBytes(this.type);
        this.data = new byte[2];
    }

    public void sendBitfield(DataOutputStream out) throws IOException {
        try {
            int sizeinit = out.size();
            out.writeInt(length);
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

    public void extractDataToArray(){

    }

    public void verifyBitfieldIntegrity (){
       if(type != 5) System.out.println("Bad BitField");
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getTypeByte() {
        return this.typeByte;
    }

    public void setTypeByte(byte[] typeByte) {
        this.typeByte = typeByte;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
            " length='" + getLength() + "'" +
            ", typeByte='" + Utils.bytesToHex(getTypeByte()) + "'" +
            ", type='" + getType() + "'" +
            ", data='" + Utils.bytesToHex(getData()) + "'" +
            "}";
    }


}
