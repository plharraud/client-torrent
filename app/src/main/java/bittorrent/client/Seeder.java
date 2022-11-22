package bittorrent.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.util.Arrays;

import bittorrent.client.tcpMessage.Bitfield;
import bittorrent.client.tcpMessage.BittorrentMessage;
import bittorrent.client.tcpMessage.NotInterested;
import bittorrent.client.tcpMessage.Request;
import bittorrent.client.tcpMessage.Unchocke;
import bittorrent.client.tcpMessage.Piece;

public class Seeder {

    public void seed(Torrent torrent,int server_port, byte[] peer_id) {
        try {
            // TODO : BAD BAD HARDCODED
            final String FILEPATH = "src/test/personalTorrents/Soleil.png";
            // Load the file as a byte Array
            File file = new File(FILEPATH);
            byte[] fileAsBytes = Files.readAllBytes(file.toPath());

            // When seeding, we are the server, waiting for a client connexion.
            // As such, we need to open a server socket, on the port specified
            System.out.println("Accepting requests on port " + server_port);
            ServerSocket seederSocket = new ServerSocket(server_port);

            // This function is blocking, and will advance only uppon recieving a request
            Socket clientSocket = seederSocket.accept();
            
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);

            DataInputStream data_in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream data_out = new DataOutputStream(clientSocket.getOutputStream());
            
            // HANDSHAKE <===
            Handshake handreq1 = new Handshake(data_in);
            System.out.println("Handshake request : ");
            System.out.println(handreq1.toString());

            // HANDSHAKE ===>
            Handshake handresp = new Handshake(new byte[8], torrent.getInfo_hash(), peer_id);
            handresp.sendHandshake(data_out);
            System.out.println("Handshake response : \n" + handresp);

            // BITFIELD <===
            BittorrentMessage bitfield = (new BittorrentMessage(data_in)).identify();
            System.out.println("<=== " + bitfield);

            // BITFIELD ===>
            Bitfield seederBitfield = new Bitfield(Utils.hexStringToByteArray("ffffff80"));
            System.out.println("===> " + seederBitfield);
            seederBitfield.send(data_out);

            // INTERESTED <===
            BittorrentMessage interested = new BittorrentMessage(data_in).identify();
            System.out.println("<=== " + interested);
            Boolean clientInterested = true;

            // UNCHOKE ===>
            Unchocke seederUnchocke = new Unchocke();
            System.out.println("===> " + seederUnchocke);
            seederUnchocke.send(data_out);

            // REQUEST <=<=<=<=<===
            while(true){
                BittorrentMessage incomingMessage = new BittorrentMessage(data_in).identify();
                System.out.println("<=== " + incomingMessage);
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
                        System.out.println("===> " + pieceMessage);
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
