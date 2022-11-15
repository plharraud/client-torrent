package bittorrent.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Unchoke {

    private int length;
    private int type;
    private byte[] typeByte;

    public Unchoke(){ //Generate unchoke from our client
        this.length = 1;
        this.type = 1;
        this.typeByte = Utils.intToBytes(type);
    }

    public Unchoke(DataInputStream unchoke_received) throws IOException{ //Unchoke received from another client
        try{
            this.length = unchoke_received.readInt();
            this.typeByte = new byte[1];
            unchoke_received.read(this.typeByte); // = 1 octet qui vaut x01
            this.type = Utils.byteArrayToUnsignedInt(typeByte);
        } 
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendUnchoke(DataOutputStream out) throws IOException {
        try {
            out.writeInt(length);
            out.writeByte(type);
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void verifyUnchokeIntegrity (){
        if(type != 1) System.out.println("Bad Unchoke");
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getTypeByte() {
        return this.typeByte;
    }

    public void setTypeByte(byte[] typeByte) {
        this.typeByte = typeByte;
    }

    @Override
    public String toString() {
        return "{" +
            " length='" + getLength() + "'" +
            ", type='" + getType() + "'" +
            ", typeByte='" + Utils.bytesToHex(getTypeByte()) + "'" +
            "}";
    }

}
