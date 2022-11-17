package bittorrent.client.tcpMessage;

public interface IBittorentMessageHandler {
    // Handle the message , what's executed upon message reception
    public void handle();
}
