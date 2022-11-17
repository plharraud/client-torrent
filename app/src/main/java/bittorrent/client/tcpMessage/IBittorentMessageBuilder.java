package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IBittorentMessageBuilder  {
    // Builds the message to the DataOutputStream
    public void build(DataOutputStream out) throws IOException;
}
