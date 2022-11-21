package bittorrent.client.tcpMessage;

import java.util.HashMap;

public enum BittorrentMessageType {
    KEEP_ALIVE,
    CHOKE,
    UNCHOKE,
    INTERESTED,
    NOT_INTERESTED,
    HAVE,
    BITFIELD,
    REQUEST,
    PIECE,
    CANCEL,
    PORT;

    public static final HashMap<Integer, BittorrentMessageType> INT_TO_MESSAGE_TYPE_MAP = new HashMap<Integer, BittorrentMessageType>();

    static {
        INT_TO_MESSAGE_TYPE_MAP.put(-1, KEEP_ALIVE);
        INT_TO_MESSAGE_TYPE_MAP.put(0, CHOKE);
        INT_TO_MESSAGE_TYPE_MAP.put(1, UNCHOKE);
        INT_TO_MESSAGE_TYPE_MAP.put(2, INTERESTED);
        INT_TO_MESSAGE_TYPE_MAP.put(3, NOT_INTERESTED);
        INT_TO_MESSAGE_TYPE_MAP.put(4, HAVE);
        INT_TO_MESSAGE_TYPE_MAP.put(5, BITFIELD);
        INT_TO_MESSAGE_TYPE_MAP.put(6, REQUEST);
        INT_TO_MESSAGE_TYPE_MAP.put(7, PIECE);
        INT_TO_MESSAGE_TYPE_MAP.put(8, CANCEL);
        INT_TO_MESSAGE_TYPE_MAP.put(9, PORT);
        
    }
}
