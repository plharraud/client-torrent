package bittorrent.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.tcpMessage.Bitfield;
import bittorrent.client.tcpMessage.BittorrentMessage;
import bittorrent.client.tcpMessage.Request;
import bittorrent.client.tcpMessage.Unchoke;
import bittorrent.client.torrent.Torrent;
import bittorrent.client.torrent.TorrentTask;
import bittorrent.client.tcpMessage.Piece;

public class Seeder {

    private static Logger log = LogManager.getLogger();


    public void seed(TorrentTask task,int server_port, byte[] peer_id) {
        try {
            Torrent torrent = task.getTorrent();

            // Load the file as a byte Array
            File file = new File(task.getDownloadedFilePath());
            byte[] fileAsBytes = Files.readAllBytes(file.toPath());

            // When seeding, we are the server, waiting for a client connexion.
            // As such, we need to open a server socket, on the port specified
            log.info("Accepting requests on port " + server_port);
            ServerSocket seederSocket = new ServerSocket(server_port);

            // This function is blocking, and will advance only uppon recieving a request
            Socket clientSocket = seederSocket.accept();
            
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            log.info("Handling client at " + clientAddress);

            DataInputStream data_in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream data_out = new DataOutputStream(clientSocket.getOutputStream());
            
            // HANDSHAKE <===
            Handshake handreq1 = new Handshake(data_in);
            log.debug("Handshake request : ");
            log.debug(handreq1.toString());

            // HANDSHAKE ===>
            Handshake handresp = new Handshake(new byte[8], torrent.getInfo_hash(), peer_id);
            handresp.sendHandshake(data_out);
            log.debug("Handshake response : \n" + handresp);

            // BITFIELD <===
            BittorrentMessage bitfield = (new BittorrentMessage(data_in)).identify();
            log.debug("<=== " + bitfield);

            // BITFIELD ===>
            Bitfield seederBitfield = new Bitfield(Utils.hexStringToByteArray("ffffff80"));
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
                        Request request = (Request)incomingMessage;
                        // Handle the request by sending a piece message
                        // Select the piece from the file
                        int from = request.getPieceIndex() * torrent.getPiece_length() + request.getPieceBeginOffset();
                        int to = from + request.getPieceLength();
                        byte[] piece = Arrays.copyOfRange(fileAsBytes,from,to );
                        assert piece.length == request.getPieceLength();
                        // Create the message:
                        Piece pieceMessage = new Piece(request.getPieceIndex(), request.getPieceBeginOffset(), piece);
                        log.debug("===> " + pieceMessage);
                        pieceMessage.send(data_out);
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
            //Closing socket on exiting
            seederSocket.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
