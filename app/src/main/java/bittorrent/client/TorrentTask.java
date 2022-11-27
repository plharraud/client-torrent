package bittorrent.client;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class TorrentTask {

    private File torrentFile;
    private Torrent torrent;

    private File destinationDir;

    private String downloadedFilePath;
    private File downloadedFile;


    public TorrentTask(File torrentFile, File destinationDir) {
        this.torrentFile = torrentFile;
        this.torrent = new Torrent(torrentFile);
        this.destinationDir = destinationDir;
        this.downloadedFilePath = FilenameUtils.concat(destinationDir.getPath(), torrent.getName());
        this.downloadedFile = new File(downloadedFilePath);
    }
    
    public boolean fileExists() {
        return downloadedFile.exists();
    }

    public void getHash() {
        //TODO return downloaded file hash
    }

    public boolean ready() {
        //TODO return true if ready to seed
        // meaning : we have the downloaded file and it is valid (hashes match)
        return fileExists();
    }

    public String getDownloadedFilePath() {
        return this.downloadedFilePath;
    }

    public File getDownloadedFile() {
        return this.downloadedFile;
    }

    public Torrent getTorrent() {
        return this.torrent;
    }

    public File getTorrentFile() {
        return this.torrentFile;
    }

    public void setTorrentFile(File torrentFile) {
        this.torrentFile = torrentFile;
    }

    public File getDestinationDir() {
        return this.destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }

}
