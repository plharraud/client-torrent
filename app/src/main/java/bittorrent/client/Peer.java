package bittorrent.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;


public class Peer {
    public static final int PEER_SIZE = 6;
    private InetAddress ip;
    private int port;
    private int id;

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
    }

    public int getId() {
        return 0;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Peer [ip=" + ip + ", port=" + port + "]";
    }

}
