
package bittorrent.client;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Framer {
  void frameMsg(byte[] message, DataOutputStream out) throws IOException;
  byte[] nextMsg() throws IOException;
}
