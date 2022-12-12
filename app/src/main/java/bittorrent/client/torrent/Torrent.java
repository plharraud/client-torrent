package bittorrent.client.torrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.Hash;
import bittorrent.client.tcpMessage.Bitfield;

public class Torrent {

    public static Logger log = LogManager.getLogger();

	private TorrentFile metaInfo;
	private TargetFile target;

	public Torrent(File torrent, File targetDir) throws IOException, BadTorrentFileException, FileNotFoundException, NoSuchAlgorithmException {
		this.metaInfo = new TorrentFile(torrent);
		this.target = new TargetFile(FilenameUtils.concat(targetDir.getPath(), this.metaInfo.getName()));
		target.setFileLength(metaInfo.getLength());
		log.info("target file length={}", target.getFileLength());
	}

	public TorrentFile getMetaInfo() {
		return this.metaInfo;
	}

	public TargetFile getTarget() {
		return this.target;
	}

	public Bitfield verifyFile() throws IOException {
		byte[] bitfieldArray = new byte[metaInfo.getPiecesAmount()/8 + 1];
		Bitfield bitfield = new Bitfield(bitfieldArray);
		int i = 0;
		for (Hash metaHash : metaInfo.getHashes()) {
			Hash fileHash = getPieceHash(i);
			if (fileHash.equals(metaHash)) {
				log.debug("piece {} validated ({})", i, fileHash);
				bitfield.havePiece(i);
			} else {
				log.debug("piece {} invalid (meta {}|{} file)", i, metaHash, fileHash);
			}
			i++;
		}
		return bitfield;
	}

	public Hash getPieceHash(int pieceIndex) throws IOException {
		int pieceLength = metaInfo.getPieceLength();
		byte[] piece = target.getBlock(pieceLength, pieceIndex, 0, pieceLength);
		return new Hash(piece);
	}

	//TODO move dans targetfile ??
    public int getLeft() {
        return 0;
    }
}
