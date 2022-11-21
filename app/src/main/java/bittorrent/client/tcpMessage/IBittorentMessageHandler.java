package bittorrent.client.tcpMessage;

public interface IBittorentMessageHandler {
    /**
     * Handles the message. To be called after a message has been identified, to have a proper handling.
     */
    public void handle();
}
