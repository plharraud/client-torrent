package bittorrent.client.torrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FilenameUtils;

public class Torrent {

	private TorrentFile metaInfo;
	private TargetFile target;

	public Torrent(File torrent, File targetDir) throws IOException, BadTorrentFileException, FileNotFoundException, NoSuchAlgorithmException {
		this.metaInfo = new TorrentFile(torrent);
		this.target = new TargetFile(FilenameUtils.concat(targetDir.getPath(), this.metaInfo.getName()));
	}

	public TorrentFile getMetaInfo() {
		return this.metaInfo;
	}

	public TargetFile getTarget() {
		return this.target;
	}

	//TODO move dans targetfile ??
    public int getLeft() {
        return 0;
    }
}
