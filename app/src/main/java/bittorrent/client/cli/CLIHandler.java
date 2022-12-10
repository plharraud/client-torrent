package bittorrent.client.cli;

import org.apache.commons.cli.Options;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import bittorrent.client.torrent.Torrent;

public class CLIHandler {

    public static Logger log = LogManager.getLogger();

    public static Torrent parse(String[] args) throws CLIException, IOException, ParseException {
        Options cliOptions = new Options();
        cliOptions.addOption("debug", false, "print debug messages");
        cliOptions.addOption("info", false, "print information");

        CommandLineParser cliParser = new DefaultParser();
        CommandLine cliCmd = cliParser.parse(cliOptions, args);

        if (cliCmd.hasOption("debug")) {
            initLogger(Level.DEBUG);
        }
        if (cliCmd.hasOption("info")) {
            initLogger(Level.INFO);
        }

        log.info("set log level to {}", log.getLevel());

        String[] cliArgs = cliCmd.getArgs();

        if (cliArgs.length != 2) {
            throw new CLIException("specify .torrent file and destination directory");
        }

        String torrentFilePath = cliArgs[0];
        String targetDirPath = cliArgs[1];

        if (! FilenameUtils.getExtension(torrentFilePath).equals("torrent")) {
            throw new CLIException(torrentFilePath + " is not a .torrent file");
        }

        File torrent = new File(torrentFilePath);
        if (! torrent.exists()) {
            throw new CLIException(torrentFilePath + " does not exist");
        }

        File target = new File(targetDirPath);
        if (! target.isDirectory()) {
            throw new CLIException(targetDirPath + " is not a directory");
        }

        return new Torrent(torrent, target);
    }

    public static void initLogger(Level level) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME); 
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
    }
}
