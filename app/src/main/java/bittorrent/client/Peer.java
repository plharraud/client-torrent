package bittorrent.client;

import java.util.Arrays;

import javax.naming.directory.InvalidAttributesException;

public class Peer {
    public static final int PEER_SIZE = 6;
    private byte[] ipInBytes;
    private byte[] portInBytes;
    private String ip;
    private String port;

    // Constructor from a byte array
    public Peer(byte[] peerInfo) {
        assert(peerInfo.length == 6);
        // The first 4 bytes are used to store the IP
        ipInBytes = Arrays.copyOfRange(peerInfo, 0, 4);
        ip = Utils.ipByteToString(ipInBytes);

        // The next 2 are used to store the port
        portInBytes = Arrays.copyOfRange(peerInfo, 4, 6);
        port = Utils.portByteToString(portInBytes);
    }

    public byte[] getIpInBytes() {
        return ipInBytes;
    }

    public byte[] getPortInBytes() {
        return portInBytes;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}
