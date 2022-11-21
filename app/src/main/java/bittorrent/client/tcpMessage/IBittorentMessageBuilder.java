package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IBittorentMessageBuilder  {
    /**
     * Builds the message to DataOutPutStream
     * @param out
     * @throws IOException
     */
    public void build(DataOutputStream out) throws IOException;
}
