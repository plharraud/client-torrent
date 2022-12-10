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

    public void writeBlock() throws IOException {
        this.RAFile.write(null, 0, 0);
    }

    public File getFile() {
        return file;
    }
}
