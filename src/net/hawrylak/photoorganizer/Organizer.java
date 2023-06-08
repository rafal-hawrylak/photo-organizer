package net.hawrylak.photoorganizer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rafal
 */
public class Organizer {
    private final File sourceDir;
    private final File targetDir;
    private final int maxLeap;
    private final String fileNamePattern;
    private final String dirNamePattern;
    private final Pattern filePattern;
    private final String datePattern;

    public Organizer(final File sourceDir, final File targetDir,
            final int maxLeap, final String fileNamePattern,
            final String dirNamePattern, final String datePattern) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
        this.maxLeap = maxLeap;
        this.fileNamePattern = fileNamePattern;
        this.dirNamePattern = dirNamePattern;
        filePattern = Pattern.compile(fileNamePattern);
        this.datePattern = datePattern;
    }

    public void run() throws ParseException, IOException {
        final File[] listFiles = sourceDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return name.matches(fileNamePattern);
            }
        });
        if (listFiles == null || listFiles.length == 0) {
            System.out
                    .println("No files matching pattern found in source directory");
            return;
        }
        File currentDir = null;
        File lastFile = null;
        for (final File file : listFiles) {
            if (currentDir == null) {
                currentDir = makeCurrentDir(file, targetDir, dirNamePattern);
            }
            if (!isWithinMaxLeap(file, lastFile)) {
                currentDir = makeCurrentDir(file, targetDir, dirNamePattern);
            }
            System.out.println("move " + file.getName() + " to "
                    + currentDir.getName());
            moveFile(file, currentDir);
            lastFile = file;
        }
    }

    private File makeCurrentDir(final File file, final File targetDir,
            final String dirNamePattern) {
        File currentDir = null;
        final Matcher m = filePattern.matcher(file.getName());
        if (m.find()) {
            final String output = m.replaceFirst(dirNamePattern);
            final String newDirName = targetDir.getAbsolutePath()
                    + File.separator + output;
            currentDir = new File(newDirName);
            currentDir.mkdir();
        }
        return currentDir;
    }

    private boolean isWithinMaxLeap(final File file, final File lastFile)
            throws ParseException {
        if (lastFile == null) {
            return true;
        }
        final Date dateA = makeDate(file.getName());
        final Date dateB = makeDate(lastFile.getName());
        final long dayDiff = TimeUnit.DAYS.convert(
                dateA.getTime() - dateB.getTime(), TimeUnit.MILLISECONDS);
        return Math.abs(dayDiff) <= maxLeap;
    }

    private Date makeDate(final String name) throws ParseException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        final Matcher m = filePattern.matcher(name);
        if (m.find()) {
            final String output = m.replaceFirst(datePattern);
            return df.parse(output);
        }
        return null;
    }

    private void moveFile(final File file, final File currentDir)
            throws IOException {
        Files.move(file.toPath(), new File(currentDir.getAbsolutePath()
                + File.separator + file.getName()).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
