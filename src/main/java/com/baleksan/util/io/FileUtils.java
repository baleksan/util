package com.baleksan.util.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public final class FileUtils {
    /**
     * utility logger.
     */
    public static final Logger LOG = LogManager.getLogger(FileUtils.class);
    /**
     * line separator.
     */
    public static final String EOL = System.getProperty("line.separator");

    // disable subclassing and instantiation
    private FileUtils() {
    }

    /**
     * Deletes all files and sub-directories under dir. Returns true if all
     * deletions were successful. If a deletion fails, the method stops
     * attempting to delete and returns false.
     * 
     * @param dir directory to delete
     * @return true on success
     */
    public static boolean deleteDir(final File dir) {
        if (dir == null) {
            return true;
        }
        if (dir.isDirectory()) {
            final String[] children = dir.list();
            for (final String child : children) {
                final boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    /**
     * Remove directory by renaming and then removing the renamed dir.
     * 
     * @param dir dir to delete
     */
    public static void fastDeleteDir(final File dir) {
        final String name = dir.getName() + "-" + System.currentTimeMillis();
        final File parent = dir.getParentFile();
        final File toBeDeleted = new File(parent, name);

        dir.renameTo(toBeDeleted);
        final boolean success = deleteDir(toBeDeleted);
        if (!success) {
            LOG.error("Failed to remove dir " + toBeDeleted);
        }
    }

    /**
     * Copies a file using stream channel objects and channel buffer.
     * 
     * @param src source file
     * @param dest destination file
     * @throws IOException propagated exceptions
     */
    public static void copyLargeFile(final File src, final File dest) throws IOException {
        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(src).getChannel();
            out = new FileOutputStream(dest).getChannel();

            final long size = in.size();
            final MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);

            out.write(buf);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Copies a file using 1KB buffer.
     * 
     * @param src source file
     * @param dest destination file
     * @throws IOException propagated exceptions
     */
    public static void copySmallFile(final File src, final File dest) throws IOException {
        final int bufferSize = 1024;
        final InputStream in = new FileInputStream(src);
        final OutputStream out = new FileOutputStream(dest);

        final byte[] buf = new byte[bufferSize];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Loads a string from a named file.
     * 
     * @param fileName source data
     * @return string compiled from source
     * @throws IOException propagated exceptions
     */
    public static String readFile(final String fileName) throws IOException {
        return readString(new FileReader(fileName));
    }

    /**
     * Loads a string from a File object.
     * 
     * @param file source data
     * @return string compiled from source
     * @throws IOException propagated exceptions
     */
    public static String readFile(final File file) throws IOException {
        return readString(new FileReader(file));
    }

    /**
     * Loads a string from a InputStream object.
     * 
     * @param in source data
     * @return string compiled from source
     * @throws IOException propagated exceptions
     */
    public static String readString(final InputStream in) throws IOException {
        return readString(new InputStreamReader(in));
    }

    /**
     * Loads a string from a reader object.
     * 
     * @param reader source data
     * @return string compiled from source
     * @throws IOException propagated exceptions
     */
    public static String readString(final Reader reader) throws IOException {
        final StringBuilder actual = new StringBuilder(128);
        final LineNumberReader r = new LineNumberReader(reader);
        String s;

        while (null != (s = r.readLine())) {
            actual.append(s).append("\n");
        }

        r.close();

        return actual.toString();
    }

    /**
     * Read a named file into a list of lines.
     * 
     * @param fileName source file name
     * @return list compiled from source
     * @throws IOException propagated exceptions
     */
    public static List<String> readLines(final String fileName) throws IOException {
        return readLines(fileName, -1);
    }

    /**
     * Read a named file into a list of lines (limited).
     * 
     * @param fileName source file name
     * @param max maximum number of lines to read
     * @return list compiled from source
     * @throws IOException propagated exceptions
     */
    public static List<String> readLines(final String fileName, final int max) throws IOException {
        final List<String> lines = new ArrayList<String>();

        final Reader reader = new FileReader(new File(fileName));
        final LineNumberReader r = new LineNumberReader(reader);
        String s;

        int count = 0;
        while (null != (s = r.readLine())) {
            lines.add(s);
            count++;
            if (count == max) {
                break;
            }
        }

        r.close();

        return lines;
    }

    /**
     * Helper interface for key mapping.
     * 
     */
    public interface KeyFunction {
        /**
         * map key spaces.
         * 
         * @param key input key
         * @return output key
         */
        int eval(int key);
    }

    /**
     * Reads a delimited file, filtering records using a function.
     * 
     * Reads a text file, splitting each line into fields. If the fields at
     * _keyPosition_ mapped by the _keyFunction_ equals the _key_ the line is
     * added to the resulting list.
     * 
     * @param fileName input file
     * @param key key value to match
     * @param keyFunction mapping function
     * @param keyPosition field to pass the mapping function
     * @param delimiter field seperator
     * @return list of strings that pass the constraint
     */
    public static List<String> readLines(final String fileName, final int key, final KeyFunction keyFunction,
            final int keyPosition,
            final String delimiter) throws IOException {
        final List<String> lines = new ArrayList<String>();

        final Reader reader = new FileReader(new File(fileName));
        final LineNumberReader r = new LineNumberReader(reader);
        String s;

        int count = 0;
        int numExceptions = 0;
        while (null != (s = r.readLine())) {
            final String[] splits = s.split(delimiter);
            try {
                final int functKey = Integer.parseInt(splits[keyPosition].trim());
                if (keyPosition < splits.length && keyFunction.eval(functKey) == key) {
                    lines.add(s.trim());
                    count++;
                }
            } catch (final NumberFormatException ex) {
                numExceptions++;
            }
        }

        r.close();

        LOG.info("Accepted " + count + " lines. Exceptions in " + numExceptions + " lines.");

        return lines;
    }

    /**
     * Feed each line form a file into a file consumer.
     * 
     * @param fileName input text file
     * @param consumer consumer to call for each line
     * @throws IOException propagated exception
     */
    public static void consumeLines(final String fileName, final FileConsumer consumer) throws IOException {
        final Reader reader = new FileReader(new File(fileName));
        final LineNumberReader r = new LineNumberReader(reader);
        String s;

        while (null != (s = r.readLine())) {
            consumer.consume(s);
        }

        r.close();
    }

    /**
     * utility interface for iterating over files.
     */
    public interface FileConsumer {
        /**
         * consume an input line.
         * 
         * @param line input.
         */
        void consume(String line);
    }

    /**
     * Get list of file names.
     * 
     * @param topLevelDirName directory to list
     * @return list of file names
     */
    public static List<File> getFilesRecursively(final String topLevelDirName) {
        final List<File> fileList = new ArrayList<File>();
        final File topLevelDir = new File(topLevelDirName);

        getFilesRecursively0(topLevelDir, fileList);

        return fileList;
    }

    private static void getFilesRecursively0(final File dir, final List<File> fileList) {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory()) {
                getFilesRecursively0(file, fileList);
            } else {
                if (!file.getName().startsWith(".") && !file.getName().endsWith(".xlsx")) {
                    fileList.add(file);
                }
            }
        }
    }

    /**
     * Reads a zipped collection of text files into a list of strings.
     * 
     * @param sourceZipFileName source fo;e
     * @return list of strings from zipped files
     * @throws IOException propagated exception
     */
    public static List<String> readZipFile(final String sourceZipFileName) throws IOException {
        final int reportFreq = 250;
        final List<String> resultingItems = new ArrayList<String>();
        final File sourceZipFile = new File(sourceZipFileName);
        final ZipFile zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
        final Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();

        int entriesCount = 1;
        while (zipFileEntries.hasMoreElements()) {
            final ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

            if (!entry.isDirectory()) {
                final BufferedInputStream is =
                        new BufferedInputStream(zipFile.getInputStream(entry));

                final String contents = FileUtils.readString(is);

                resultingItems.add(contents);

                IOUtils.closeQuietly(is);
            }

            if (entriesCount % reportFreq == 0) {
                LOG.info("Processed " + entriesCount + " entries in the " + sourceZipFileName + " zip file.");
            }
            entriesCount++;
        }

        LOG.info("Done. Processed all " + entriesCount + " entries in the " + sourceZipFileName + " zip file.");
        zipFile.close();

        return resultingItems;
    }

    /**
     * Assembles a directory into a zip file.
     * 
     * @param dirToZip source directory
     * @param zipFileName output file name
     * @throws IOException propagated exception
     */
    public static void writeToZipFile(final String dirToZip, final String zipFileName) throws IOException {
        writeToZipFile(dirToZip, zipFileName, null);
    }

    /**
     * Assembles a directory into a zip file with filtering.
     * 
     * @param dirToZip source directory
     * @param zipFileName output file name
     * @param filter file filter
     * @throws IOException propagated exception
     */
    public static void writeToZipFile(final String dirToZip, final String zipFileName, final FilenameFilter filter)
            throws IOException {
        // zip up the results
        final File outputDirFile = new File(dirToZip);

        try {
            final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
            zos.setLevel(9);
            zos.setMethod(ZipOutputStream.DEFLATED);

            final File[] files = filter == null ? outputDirFile.listFiles() : outputDirFile.listFiles(filter);

            for (final File file : files) {
                final ZipEntry ze = new ZipEntry(file.getName());
                ze.setTime(file.lastModified());

                FileInputStream fis = null;
                try {
                    final int fileLength = (int) file.length();
                    fis = new FileInputStream(file);
                    final byte[] wholeFile = new byte[fileLength];
                    final int bytesRead = fis.read(wholeFile, 0, fileLength);
                    if (bytesRead != fileLength) {
                        LOG.error("Error in processing " + file);
                    }

                    zos.putNextEntry(ze);
                    zos.write(wholeFile, 0, fileLength);

                    zos.closeEntry();
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }

            zos.flush();
            zos.finish();
            zos.close();
        } catch (final FileNotFoundException e) {
            LOG.error("Error in zipping", e);
        } catch (final IOException e) {
            LOG.error("Error in zipping", e);
        }

        // remove all the intermideary files
        org.apache.commons.io.FileUtils.deleteDirectory(outputDirFile);
    }

    /**
     * Filter for files ending in "xml".
     */
    public static class XMLFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(final File dir, final String name) {
            return name.endsWith("xml");
        }
    }

    /**
     * Filter for files ending in "txt".
     */
    public static class TxtFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(final File dir, final String name) {
            return name.endsWith("txt");
        }
    }

    /**
     * Writes string to text file.
     * 
     * @param fileName file to write
     * @param text text to write
     * @throws IOException propagated exception
     */
    public static void writeTextFile(final String fileName, final String text) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            writer.append(text);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Appends string to text file.
     * 
     * @param fileName file to append
     * @param text text to append
     * @throws IOException propagated exception
     */
    public static void appendToTextFile(final String fileName, final String text) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, true);
            writer.append(text);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
