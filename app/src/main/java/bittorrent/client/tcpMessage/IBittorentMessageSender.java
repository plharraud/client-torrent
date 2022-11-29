package bittorrent.client.tcpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IBittorentMessageSender  {
    /**
     * Builds the message to DataOutPutStream
     * @param out
     * @throws IOException
     */
    public void send(DataOutputStream out) throws IOException;
}
