package bittorrent.client.torrent;

public class BadTorrentFileException extends Exception {
    public BadTorrentFileException(String m) {
        super(m);
    }
}
