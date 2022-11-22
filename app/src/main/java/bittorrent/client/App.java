package bittorrent.client;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class App {

    public static boolean INFO = false;
    public static boolean SEEDING = false;
    public static final int DEFAULT_PORT = 6685;

    public static Logger log = LogManager.getLogger();

    private static void initLogger(Level level) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME); 
        loggerConfig.setLevel(Level.DEBUG);
        ctx.updateLoggers();
    }

    //TODO: lancer app depuis une nouvelle classe main

    public static void main(String[] args) {
        Options cliOptions = new Options();
        cliOptions.addOption("debug", false, "");
        cliOptions.addOption("info", false, "");
        try {

            CommandLineParser cliParser = new DefaultParser();
            CommandLine cliCmd = cliParser.parse(cliOptions, args);
        
            if (cliCmd.hasOption("debug")) {
                initLogger(Level.DEBUG);
            }
            if (cliCmd.hasOption("info")) {
                initLogger(Level.INFO);
            }

            String[] cliArgs = cliCmd.getArgs();
            
            if (cliArgs.length != 2) {
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
            log.error(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
