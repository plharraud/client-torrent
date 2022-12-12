package bittorrent.client.seeder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.Peer;
import bittorrent.client.tcpMessage.Bitfield;
import bittorrent.client.tcpMessage.BittorrentMessage;
import bittorrent.client.tcpMessage.Handshake;
import bittorrent.client.tcpMessage.Request;
import bittorrent.client.tcpMessage.Unchoke;
import bittorrent.client.torrent.Torrent;
import bittorrent.client.tcpMessage.Piece;

public class Seeder {

    private static Logger log = LogManager.getLogger();

    private static int handleRequest(Request r, Torrent t, DataOutputStream out) throws IOException {
        // Handle the request by sending a piece message
        // Select the piece from the file
        byte[] piece = t.getTarget().getBlock(r.getPieceLength(), r.getPieceIndex(), r.getPieceBeginOffset(), r.getPieceLength());
        assert piece.length == r.getPieceLength();

        // Create the message:
        Piece pieceMessage = new Piece(r.getPieceIndex(), r.getPieceBeginOffset(), piece);
        log.debug("===> " + pieceMessage);
        pieceMessage.send(out);
        return 0;
    }

    public static void seed(Torrent torrent, Socket clientSocket, Peer peer) {
        try {
            DataInputStream data_in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream data_out = new DataOutputStream(clientSocket.getOutputStream());

            // HANDSHAKE <===
            Handshake handreq1 = new Handshake(data_in);
            log.debug("Handshake request : " + handreq1);
            // TODO CHECK HANDSHAKE

            // HANDSHAKE ===>
            Handshake handresp = new Handshake(new byte[8], torrent.getMetaInfo().getInfoHash(), peer.getId());
            handresp.sendHandshake(data_out);
            log.debug("Handshake response : " + handresp);

            // BITFIELD <===
            //BittorrentMessage bitfield = (new BittorrentMessage(data_in)).identify();
            //log.debug("<=== " + bitfield);

            // BITFIELD ===>
            Bitfield seederBitfield = torrent.verifyFile();
            log.debug("===> " + seederBitfield);
            seederBitfield.send(data_out);

            // INTERESTED <===
            BittorrentMessage interested = new BittorrentMessage(data_in).identify();
            log.debug("<=== " + interested);
            Boolean clientInterested = true;

            // UNCHOKE ===>
            Unchoke seederUnchocke = new Unchoke();
            log.debug("===> " + seederUnchocke);
            seederUnchocke.send(data_out);

            // REQUEST <=<=<=<=<===
            while(true){
                BittorrentMessage incomingMessage = new BittorrentMessage(data_in).identify();
                log.debug("<=== " + incomingMessage);
                switch(incomingMessage.getMessageType()){
                    case REQUEST:
                        handleRequest((Request) incomingMessage, torrent, data_out);
                        break;
                    case NOT_INTERESTED:
                        clientInterested = false;
                        break;
                    default:
                        break;
                }

                // Exit if the client is no longer interested
                if (!clientInterested) {
                    break;
                }
            }

            clientSocket.close();

        } catch (IOException e) {
            log.error(e);
        }
    }
}
