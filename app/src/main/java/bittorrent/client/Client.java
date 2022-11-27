package bittorrent.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bittorrent.client.cli.CLIHandler;

public class Client {

    public static final int DEFAULT_PORT = 6685;

    public static Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            TorrentTask task = CLIHandler.parse(args);

            TrackerConnect tc = new TrackerConnect(task.getTorrent(), DEFAULT_PORT);

            // TODO ca changera certainement avec la FSM
            if (task.ready()) { // seed
                TrackerInfo info = tc.iHaveTheFullFile();
                log.info(info);
                new Seeder().seed(task, DEFAULT_PORT, tc.getPeer_id());
            } else { // leech
                TrackerInfo info = tc.getTrackerInfo();
                byte[] selfPeerId = tc.getPeer_id();
                log.info("peers:");
                for (int i = 1; i < info.peersList.size(); i++) {
                    Peer peer = info.peersList.get(i);
                    log.info(peer.getIp().toString() +":"+ peer.getPort());
                    new Leecher().leech(task, selfPeerId, peer);
                }
            }

        } catch (Exception e) {
            log.error(e);
            System.exit(1);
        }
    }
}
