package bittorrent.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

public class Peer {

    public static final int PEER_SIZE = 6;
    private InetAddress ip;
    private int port;
    private byte[] id;

    // Constructor from a byte array
    public Peer(byte[] peerInfo) {
        try {
            assert(peerInfo.length == 6);
            // The first 4 bytes are used to store the IP
            byte[] ipInBytes = Arrays.copyOfRange(peerInfo, 0, 4);
            ip = InetAddress.getByAddress(ipInBytes);

            // The next 2 are used to store the port
            byte[] portInBytes = Arrays.copyOfRange(peerInfo, 4, 6);
            port = Utils.portByteToInt(portInBytes);

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Peer(int port) {
        this.port = port;
        randomId();
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public void randomId() {
        this.id = new byte[20];
        new Random().nextBytes(this.id);
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Peer [id="+ Utils.bytesToHex(id) +", ip=" + ip + ", port=" + port + "]";
    }

    public Boolean equals(Peer p) {
        return port == p.getPort();
    }

}
