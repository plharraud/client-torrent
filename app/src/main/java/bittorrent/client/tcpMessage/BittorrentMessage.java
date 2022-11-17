package bittorrent.client.tcpMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class BittorrentMessage implements IBittorentMessageBuilder, IBittorentMessageHandler {
    DataInputStream dataInput;
    int messageLength;
    int messageType;

    /**
     * Builds the bittorent message from a DataInputStream
     * @param dataInput
     * @throws IOException
     */
    public BittorrentMessage(DataInputStream dataInput) throws IOException{
        // readInt reads 4 bytes of data and outputs them as an int
        this.messageLength = dataInput.readInt();
        // reads 1 byte of data and output them as an int, if we are at the end of the message , returns -1
        this.messageType = dataInput.read();
        // Remainder of the message
        this.dataInput = dataInput;
    }

    /**
     * Constructor used when the message is build, not decoded
     * @param messageLength
     * @param messageType
     */
    public BittorrentMessage(int messageLength, int messageType) {
        this.messageLength = messageLength;
        this.messageType = messageType;
        // Explicitely nulls the dataInput
        this.dataInput = null;
    }

    /**
     * Copy constructor, usefull when we identify the message
     * @param bittorrentMessage
     */
    public BittorrentMessage(BittorrentMessage bittorrentMessage) {
        this.dataInput = bittorrentMessage.dataInput;
        this.messageLength = bittorrentMessage.messageLength;
        this.messageType = bittorrentMessage.messageType;
    }

    /**
     * Returns an identified and decoded message
     * @return
     * @throws IOException
     */
    public BittorrentMessage identify() throws IOException {
        BittorrentMessageType type = BittorrentMessageType.INT_TO_MESSAGE_TYPE_MAP.get(messageType);
        BittorrentMessage identifiedMessage = null;
        switch(type){
            case BITFIELD:
                identifiedMessage = new Bitfield(this);
                break;
            case CANCEL:
                identifiedMessage = new Cancel(this);
                break;
            case CHOKE:
                identifiedMessage = new Choke(this);
                break;
            case HAVE:
                identifiedMessage = new Have(this);
                break;
            case INTERESTED:
                identifiedMessage = new Interested(this);
                break;
            case KEEP_ALIVE:
                identifiedMessage = new KeepAlive(this);
                break;
            case NOT_INTERESTED:
                identifiedMessage = new NotInterested(this);
                break;
            case PIECE:
                identifiedMessage = new Piece(this);
                break;
            case PORT:
                identifiedMessage = new Port(this);
                break;
            case REQUEST:
                identifiedMessage = new Request(this);
                break;
            case UNCHOKE:
                identifiedMessage = new Unchocke(this);
                break;
            default:
                break;
        }
        return identifiedMessage;

    }

    @Override
    public void build(DataOutputStream out) throws IOException {
        out.writeInt(messageLength);
        out.writeByte(messageType);
    }

    @Override
    public void handle() {
        // Handling of the message will be done depending on the message type.
        // Each message class must have a proper handling
        throw new NoSuchMethodError("Not implemented yet");
    }

    @Override
    public String toString() {
        return "messageLength=" + messageLength + ", messageType=" + messageType;
    }

}
