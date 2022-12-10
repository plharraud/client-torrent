
package bittorrent.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LengthFramer implements Framer {
  public static final int MAXMESSAGELENGTH = 65535;
  public static final int BYTEMASK = 0xff;
  public static final int SHORTMASK = 0xffff;
  public static final int BYTESHIFT = 8;

  private DataInputStream in; // wrapper for data I/O

  public LengthFramer(InputStream in) throws IOException {
    this.in = new DataInputStream(in);
  }

  public void frameMsg(byte[] message, DataOutputStream out) throws IOException {
    if (message.length > MAXMESSAGELENGTH) {
      throw new IOException("message too long");
    }
    out.writeInt(message.length);
    // out.writeByte(message.type);
    out.write(message);
    out.flush();
  }

  public byte[] nextMsg() throws IOException {
    int length;
    try {
      length = in.readUnsignedShort(); // read 2 bytes
    } catch (EOFException e) { // no (or 1 byte) message
      return null;
    }
    // 0 <= length <= 65535
    byte[] msg = new byte[length];
    in.readFully(msg); // if exception, it's a framing error.
    return msg;
  }
}
