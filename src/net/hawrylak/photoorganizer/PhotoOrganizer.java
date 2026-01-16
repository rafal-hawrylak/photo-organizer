package net.hawrylak.photoorganizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * @author Rafal
 */
public class PhotoOrganizer {
    private static Logger logger = LogManager.getLogger(PhotoOrganizer.class);

    private static final int DEFAULT_MAX_LEAP = 0;
    private static final String DEFAULT_FILENAME_PATTERN = "([0-9]{4})-?([0-9]{2})-?([0-9]{2})[^0-9].*\\..*";
    private static final String DEFAULT_DIRNAME_PATTERN = "$1-$2-$3";
    private static final String DEFAULT_DATE_PATTERN = "$1-$2-$3";

    public static void main(final String[] args) {
        if (args.length < 2) {
            printUsage();
            return;
        }
        final String sourceDirStr = args[0];
        final String targetDirStr = args[1];
        int maxLeap = DEFAULT_MAX_LEAP;
        if (args.length >= 3) {
            maxLeap = Integer.parseInt(args[2]);
        }
        String fileNamePattern = DEFAULT_FILENAME_PATTERN;
        if (args.length >= 4) {
            fileNamePattern = args[3];
        }
        String dirNamePattern = DEFAULT_DIRNAME_PATTERN;
        if (args.length >= 5) {
            dirNamePattern = args[4];
        }
        String datePattern = DEFAULT_DATE_PATTERN;
        if (args.length >= 6) {
            datePattern = args[5];
        }
        final File sourceDir = new File(sourceDirStr);
        if (!sourceDir.isDirectory()) {
            printUsage();
            logger.info("sourceDir does not exist");
            return;
        }
        final File targetDir = new File(targetDirStr);
        if (!targetDir.isDirectory()) {
            targetDir.mkdir();
        }
        printRunInfo(sourceDirStr, targetDirStr, maxLeap, fileNamePattern,
                dirNamePattern, datePattern);
        final Organizer organizer = new Organizer(sourceDir, targetDir,
                maxLeap, fileNamePattern, dirNamePattern, datePattern);
        try {
            organizer.run();
        } catch (final ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        logger.info("######## usage:");
        logger.info("PhotoOrganizer sourceDir targetDir [maximum days of leap - default "
                        + DEFAULT_MAX_LEAP
                        + "] [filename pattern - default "
                        + DEFAULT_FILENAME_PATTERN
                        + "] [dir pattern - default "
                        + DEFAULT_DIRNAME_PATTERN
                        + "] [date pattern - default "
                        + DEFAULT_DATE_PATTERN
                        + "]");
    }

    private static void printRunInfo(final String sourceDir,
            final String targetDir, final int maxLeap,
            final String fileNamePattern, final String dirNamePattern,
            final String datePattern) {
        logger.info("######## run with params");
        logger.info("---------- sourceDir: " + sourceDir);
        logger.info("---------- targetDir: " + targetDir);
        logger.info("---------- maxLeap: " + maxLeap);
        logger.info("---------- fileNamePattern: " + fileNamePattern);
        logger.info("---------- dirNamePattern: " + dirNamePattern);
        logger.info("---------- datePattern: " + datePattern);
    }
}
