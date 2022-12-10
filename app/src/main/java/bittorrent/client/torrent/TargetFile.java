package bittorrent.client.torrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TargetFile {

    private File file;
    private RandomAccessFile RAFile;

    public TargetFile(String filepath) throws FileNotFoundException {
        this.file = new File(filepath);
        this.RAFile = new RandomAccessFile(this.file, "rw");
    }

    public void writeBlock() throws IOException {
        this.RAFile.write(null, 0, 0);
    }
}
