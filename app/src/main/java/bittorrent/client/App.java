/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package bittorrent.client;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class App {

    public static boolean DEBUG = false;
    public static boolean INFO = false;
    public static boolean SEEDING = false;
    public static final int DEFAULT_PORT = 6685;
    public static final String PATHNAME = "src/test/resources/torrents/CuteTogepi.jpg.torrent";
    public static final String PATHNAMEICEBERG = "src/test/resources/torrents/iceberg.jpg.torrent";
    public static final String PATHNAMETROLL = "src/test/resources/torrents/troll.jpg.torrent";
    public static final String PATHNAMEBOEUF= "src/test/resources/torrents/boeufalabiere.jpg.torrent";
    public static final String PATHNAME4K= "src/test/resources/torrents/Hutton_in_the_Forest_4K.jpg.torrent";
    public static final String PATHNAMEMP4= "src/test/resources/torrents/yeah.mp4.torrent";

    //TODO: lancer app depuis une nouvelle classe main

    public static void main(String[] args) {
        Options cliOptions = new Options();
        cliOptions.addOption("debug", false, "");
        cliOptions.addOption("info", false, "");

        // TODO : Make a proper CLI program
        // We start by loading the torrent file
        Torrent torrent = new Torrent(new File(PATHNAMEMP4));
        // Then, we get the tracker's informations
        TrackerConnect tc = new TrackerConnect(torrent);
        TrackerInfo info = tc.getTrackerInfo();

        System.out.println(info);

        // TODO : getOtherPeers
        try {

            CommandLineParser cliParser = new DefaultParser();
            CommandLine cliCmd = cliParser.parse(cliOptions, args);
        
            // if (cliCmd.hasOption("debug")) {
            //     App.DEBUG = true;
            // }
            // if (cliCmd.hasOption("info")) {
            //     App.INFO = true;
            // }
            // if (cliCmd.hasOption("seed")) {
            //     App.SEEDING = true;
            // }

            String[] cliArgs = cliCmd.getArgs();
            
            if (cliArgs.length != 2 && !App.SEEDING) {
                throw new CLIException("specify .torrent file and destination directory");
            }

            String torrentFilePath = cliArgs[0];
            String destinationPath = cliArgs[1];

            if (! FilenameUtils.getExtension(torrentFilePath).equals("torrent")) {
                throw new CLIException(torrentFilePath + " is not a .torrent file");
            }

            File torrentFile = new File(torrentFilePath);
            if (! torrentFile.exists()) {
                throw new CLIException(torrentFilePath + " does not exist");
            }

            File destinationFile = new File(destinationPath);
            if (! destinationFile.isDirectory()) {
                throw new CLIException(destinationPath + " does not exist or is not a directory");
            }

            Torrent torrent = new Torrent(torrentFile);
            // Then, we get the tracker's informations
            TrackerConnect tc = new TrackerConnect(torrent,DEFAULT_PORT);

            // Check if the file is in the specified directory
            String downloadedFile = FilenameUtils.concat(destinationPath, torrent.getName());
            if ( (new File(downloadedFile)).exists()) {
                // Seed the file
                TrackerInfo info = tc.iHaveTheFullFile();
                System.out.println(info);
                new Seeder().seed(torrent, DEFAULT_PORT,tc.getPeer_id());
            } else {
                // Start leeching
                TrackerInfo info = tc.getTrackerInfo();
                byte[] selfPeerId = tc.getPeer_id();
                System.out.println("peers:");
                for (int i = 1; i < info.peersList.size(); i++) {
                    Peer peer = info.peersList.get(i);
                    System.out.println(peer.getIp().toString() +":"+ peer.getPort());
                    //TODO multithread et choix des peers
                    new Leecher().leech(torrent, selfPeerId, peer);
                }
            }




        } catch (CLIException e) {
            System.err.println(e.getMessage());
            // HelpFormatter cliHelpFormatter = new HelpFormatter();
            // cliHelpFormatter.printHelp(args[0], cliOptions);
            System.exit(1);
        } catch (ParseException e) {
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
