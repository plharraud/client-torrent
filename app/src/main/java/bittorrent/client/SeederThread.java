package bittorrent.client;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeederThread implements Runnable {

    private Socket clientSocket;

    Logger log = LogManager.getLogger();

    public SeederThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        log.info("handling client " + clientSocket.getRemoteSocketAddress());
    }

}
