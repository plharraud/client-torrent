package bittorrent.client.torrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TargetFile {

    public static Logger log = LogManager.getLogger();

    private File file;
    private RandomAccessFile RAFile;

    public TargetFile(String filepath) throws FileNotFoundException {
        log.debug("opening {}", filepath);
        this.file = new File(filepath);
        this.RAFile = new RandomAccessFile(this.file, "rw");
    }

    public byte[] getBlock(int pieceLength, int pieceIndex, int beginOffset, int length) throws IOException {
        RAFile.seek(pieceLength * pieceIndex + beginOffset);
        int effectivePieceLength = getPieceLength(pieceLength, pieceIndex, beginOffset);
        log.trace("read starting at {} for {} bytes", pieceLength * pieceIndex + beginOffset, effectivePieceLength);
        byte[] target = new byte[effectivePieceLength];
        RAFile.read(target, 0, effectivePieceLength);
        return target;
    }

    public void writeBlock(byte[] data, int pieceLength, int pieceIndex, int beginOffset, int length) throws IOException {
        RAFile.seek(pieceLength * pieceIndex + beginOffset);
        log.trace("write starting at {} for {} bytes", pieceLength * pieceIndex + beginOffset, getPieceLength(pieceLength, pieceIndex, beginOffset));
        RAFile.write(data, 0, getPieceLength(pieceLength, pieceIndex, beginOffset));
    }

	public int getPieceLength(int pieceLength, int pieceIndex, int beginOffset) throws IOException {
        int start = pieceLength * pieceIndex + beginOffset;
        if (getFileLength() - start < pieceLength) {
            return Math.toIntExact(getFileLength()) - start;
        }
        return pieceLength;
	}

    public long getFileLength() throws IOException {
        return RAFile.length();
    }

    public void setFileLength(int length) throws IOException {
        RAFile.setLength(length);
    }

    // temporaire
    public File getFile() {
        return file;
    }

    public void close() throws IOException {
        RAFile.close();
    }
}
