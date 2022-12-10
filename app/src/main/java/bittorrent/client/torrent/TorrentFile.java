package bittorrent.client.torrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dampcake.bencode.BencodeInputStream;
import com.dampcake.bencode.Type;

public class TorrentFile {

    public static Logger log = LogManager.getLogger();

    private TorrentInfo info;

    @SuppressWarnings("unchecked")
    public TorrentFile(File file) throws IOException {
        log.info("parsing torrent " + file.getName());
        FileInputStream in = new FileInputStream(file);

        BencodeInputStream bencode = new BencodeInputStream(in);
        if (! bencode.nextType().equals(Type.DICTIONARY)) {
            bencode.close();
            throw new IOException(".torrent mal formé");
        }

        Map<String, Object> meta = bencode.readDictionary();
            String announce = (String) meta.get("announce");
            String comment = (String) meta.get("comment.utf-8");

        Map<String, Object> info = (Map<String, Object>) meta.get("info");
            long length = (long) info.get("length");
            long piece_length = (long) info.get("piece length");
            int piecesAmount = (int) Math.ceil(length / (float) piece_length);
            String pieces = (String) info.get("pieces");
            List<String> hashes = new ArrayList<String>();
            for (int i = 0; i < pieces.length(); i += 20) {
                hashes.add(pieces.substring(i, Math.min(pieces.length(), i + 20)));
            }
        bencode.close();
        this.info = new TorrentInfo(); 
    }
}
