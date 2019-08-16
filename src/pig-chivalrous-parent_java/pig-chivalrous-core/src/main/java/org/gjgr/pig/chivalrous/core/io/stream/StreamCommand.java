package org.gjgr.pig.chivalrous.core.io.stream;

import org.gjgr.pig.chivalrous.core.lang.AssertCommand;
import org.gjgr.pig.chivalrous.core.lang.Nullable;
import org.gjgr.pig.chivalrous.core.lang.ObjectCommand;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Scanner;

/**
 * File Name : pig-chivalrous - org.gjgr.pig.chivalrous.core.stream CopyRright (c) 1949-xxxx: File Number： Author：gwd
 * Date：on 2018/12/7 Modify：gwd Time ： Comment： Description： Version：
 */
public final class StreamCommand {

    public static final int BUFFER_SIZE = 4096;
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final byte[] EMPTY_CONTENT = new byte[0];

    /**
     * Copy the contents of the given InputStream into a new byte array. Leaves the stream open when done.
     *
     * @param in the stream to copy from (may be {@code null} or empty)
     * @return the new byte array that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(@Nullable InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * Copy the contents of the given InputStream into a String. Leaves the stream open when done.
     *
     * @param in the InputStream to copy from (may be {@code null} or empty)
     * @return the String that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static String copyToString(@Nullable InputStream in, Charset charset) throws IOException {
        if (in == null) {
            return "";
        }

        StringBuilder out = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in, charset);
        char[] buffer = new char[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = reader.read(buffer)) != -1) {
            out.append(buffer, 0, bytesRead);
        }
        return out.toString();
    }

    /**
     * Copy the contents of the given byte array to the given OutputStream. Leaves the stream open when done.
     *
     * @param in  the byte array to copy from
     * @param out the OutputStream to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        AssertCommand.notNull(in, "No input byte array specified");
        AssertCommand.notNull(out, "No OutputStream specified");

        out.write(in);
    }

    /**
     * Copy the contents of the given String to the given output OutputStream. Leaves the stream open when done.
     *
     * @param in      the String to copy from
     * @param charset the Charset
     * @param out     the OutputStream to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(String in, Charset charset, OutputStream out) throws IOException {
        AssertCommand.notNull(in, "No input String specified");
        AssertCommand.notNull(charset, "No charset specified");
        AssertCommand.notNull(out, "No OutputStream specified");

        Writer writer = new OutputStreamWriter(out, charset);
        writer.write(in);
        writer.flush();
    }

    /**
     * Copy the contents of the given InputStream to the given OutputStream. Leaves both streams open when done.
     *
     * @param in  the InputStream to copy from
     * @param out the OutputStream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        AssertCommand.notNull(in, "No InputStream specified");
        AssertCommand.notNull(out, "No OutputStream specified");

        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * Copy a range of content of the given InputStream to the given OutputStream.
     * <p>
     * If the specified range exceeds the length of the InputStream, this copies up to the end of the stream and returns
     * the actual number of copied bytes.
     * <p>
     * Leaves both streams open when done.
     *
     * @param in    the InputStream to copy from
     * @param out   the OutputStream to copy to
     * @param start the position to start copying from
     * @param end   the position to end copying
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     * @since 4.3
     */
    public static long copyRange(InputStream in, OutputStream out, long start, long end) throws IOException {
        AssertCommand.notNull(in, "No InputStream specified");
        AssertCommand.notNull(out, "No OutputStream specified");

        long skipped = in.skip(start);
        if (skipped < start) {
            throw new IOException("Skipped only " + skipped + " bytes out of " + start + " required");
        }

        long bytesToCopy = end - start + 1;
        byte[] buffer = new byte[StreamCommand.BUFFER_SIZE];
        while (bytesToCopy > 0) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) {
                break;
            } else if (bytesRead <= bytesToCopy) {
                out.write(buffer, 0, bytesRead);
                bytesToCopy -= bytesRead;
            } else {
                out.write(buffer, 0, (int) bytesToCopy);
                bytesToCopy = 0;
            }
        }
        return (end - start + 1 - bytesToCopy);
    }

    /**
     * Drain the remaining content of the given InputStream. Leaves the InputStream open when done.
     *
     * @param in the InputStream to drain
     * @return the number of bytes read
     * @throws IOException in case of I/O errors
     * @since 4.3
     */
    public static int drain(InputStream in) throws IOException {
        AssertCommand.notNull(in, "No InputStream specified");
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        int byteCount = 0;
        while ((bytesRead = in.read(buffer)) != -1) {
            byteCount += bytesRead;
        }
        return byteCount;
    }

    /**
     * Return an efficient empty {@link InputStream}.
     *
     * @return a {@link ByteArrayInputStream} based on an empty byte array
     * @since 4.2.2
     */
    public static InputStream emptyInput() {
        return new ByteArrayInputStream(EMPTY_CONTENT);
    }

    /**
     * Return a variant of the given {@link InputStream} where calling {@link InputStream#close() close()} has no
     * effect.
     *
     * @param in the InputStream to decorate
     * @return a version of the InputStream that ignores calls to close
     */
    public static InputStream nonClosing(InputStream in) {
        AssertCommand.notNull(in, "No InputStream specified");
        return new NonClosingInputStream(in);
    }

    /**
     * Return a variant of the given {@link OutputStream} where calling {@link OutputStream#close() close()} has no
     * effect.
     *
     * @param out the OutputStream to decorate
     * @return a version of the OutputStream that ignores calls to close
     */
    public static OutputStream nonClosing(OutputStream out) {
        AssertCommand.notNull(out, "No OutputStream specified");
        return new NonClosingOutputStream(out);
    }

    /**
     * Use this function instead of new String(byte[]) to avoid surprises from non-standard default encodings.
     */
    public static String newStringFromBytes(byte[] bytes) {
        try {
            return new String(bytes, UTF8_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Impossible failure: Charset.forName(\"UTF-8\") returns invalid name.", e);
        }
    }

    /**
     * Use this function instead of new String(byte[], int, int) to avoid surprises from non-standard default encodings.
     */
    public static String newStringFromBytes(byte[] bytes, int start, int length) {
        try {
            return new String(bytes, start, length, UTF8_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Impossible failure: Charset.forName(\"UTF-8\") returns invalid name.", e);
        }
    }

    /**
     * Wraps the passed <code>in</code> into a {@link BufferedInputStream} object and returns that. If the passed
     * <code>in</code> is already an instance of {@link BufferedInputStream} returns the same passed <code>in</code>
     * reference as is (avoiding double wrapping).
     *
     * @param in the wrapee to be used for the buffering support
     * @return the passed <code>in</code> decorated through a {@link BufferedInputStream} object as wrapper
     */
    public static BufferedInputStream buffered(InputStream in) {
        ObjectCommand.notNull(in, "in");
        return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    /**
     * Wraps the passed <code>out</code> into a {@link BufferedOutputStream} object and returns that. If the passed
     * <code>out</code> is already an instance of {@link BufferedOutputStream} returns the same passed <code>out</code>
     * reference as is (avoiding double wrapping).
     *
     * @param out the wrapee to be used for the buffering support
     * @return the passed <code>out</code> decorated through a {@link BufferedOutputStream} object as wrapper
     */
    public static BufferedOutputStream buffered(OutputStream out) {
        ObjectCommand.notNull(out, "out");
        return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out);
    }

    public static boolean isExist(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        } else {
            try {
                int i = inputStream.available();
                if (i > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
    }

    /**
     * Wraps the passed <code>reader</code> into a {@link BufferedReader} object and returns that. If the passed
     * <code>reader</code> is already an instance of {@link BufferedReader} returns the same passed <code>reader</code>
     * reference as is (avoiding double wrapping).
     *
     * @param reader the wrapee to be used for the buffering support
     * @return the passed <code>reader</code> decorated through a {@link BufferedReader} object as wrapper
     */
    public static BufferedReader buffered(Reader reader) {
        ObjectCommand.notNull(reader, "reader");
        return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * Wraps the passed <code>writer</code> into a {@link BufferedWriter} object and returns that. If the passed
     * <code>writer</code> is already an instance of {@link BufferedWriter} returns the same passed <code>writer</code>
     * reference as is (avoiding double wrapping).
     *
     * @param writer the wrapee to be used for the buffering support
     * @return the passed <code>writer</code> decorated through a {@link BufferedWriter} object as wrapper
     */
    public static BufferedWriter buffered(Writer writer) {
        ObjectCommand.notNull(writer, "writer");
        return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    /**
     * A factory method which creates an {@link IOException} from the given exception and message
     *
     * @deprecated IOException support nested exception in Java 1.6. Will be removed in Camel 3.0
     */
    @Deprecated
    public static IOException createIOException(Throwable cause) {
        return createIOException(cause.getMessage(), cause);
    }

    /**
     * A factory method which creates an {@link IOException} from the given exception and message
     *
     * @deprecated IOException support nested exception in Java 1.6. Will be removed in Camel 3.0
     */
    @Deprecated
    public static IOException createIOException(String message, Throwable cause) {
        IOException answer = new IOException(message);
        answer.initCause(cause);
        return answer;
    }

    public static int copy(OutputStream output, InputStream input) throws IOException {
        return copy(input, output, BUFFER_SIZE);
    }

    public static int copy(final InputStream input, final OutputStream output, int bufferSize) throws IOException {
        return copy(input, output, bufferSize, false);
    }

    public static int copy(final InputStream input, final OutputStream output, int bufferSize, boolean flushOnEachWrite)
            throws IOException {
        if (input instanceof ByteArrayInputStream) {
            // optimized for byte array as we only need the max size it can be
            input.mark(0);
            input.reset();
            bufferSize = input.available();
        } else {
            int avail = input.available();
            if (avail > bufferSize) {
                bufferSize = avail;
            }
        }
        if (bufferSize > 262144) {
            // upper cap to avoid buffers too big
            bufferSize = 262144;
        }
        int total = 0;
        final byte[] buffer = new byte[bufferSize];
        int n = input.read(buffer);

        boolean hasData;
        hasData = n > -1;
        if (hasData) {
            while (-1 != n) {
                output.write(buffer, 0, n);
                if (flushOnEachWrite) {
                    output.flush();
                }
                total += n;
                n = input.read(buffer);
            }
        }
        if (!flushOnEachWrite) {
            // flush at end, if we didn't do it during the writing
            output.flush();
        }
        return total;
    }

    public static void copyAndCloseInput(InputStream input, OutputStream output) throws IOException {
        copyAndCloseInput(input, output, BUFFER_SIZE);
    }

    public static void copyAndCloseInput(InputStream input, OutputStream output, int bufferSize) throws IOException {
        copy(input, output, bufferSize);
        close(input, null);
    }

    public static int copy(final Reader input, final Writer output, int bufferSize) throws IOException {
        final char[] buffer = new char[bufferSize];
        int n = input.read(buffer);
        int total = 0;
        while (-1 != n) {
            output.write(buffer, 0, n);
            total += n;
            n = input.read(buffer);
        }
        output.flush();
        return total;
    }

    /**
     * Forces any updates to this channel's file to be written to the storage device that contains it.
     *
     * @param channel the file channel
     * @param name    the name of the resource
     */
    public static void force(FileChannel channel, String name) {
        try {
            if (channel != null) {
                channel.force(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Forces any updates to a FileOutputStream be written to the storage device that contains it.
     *
     * @param os   the file output stream
     * @param name the name of the resource
     */
    public static void force(FileOutputStream os, String name) {
        try {
            if (os != null) {
                os.getFD().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the given writer, logging any closing exceptions to the given log. An associated FileOutputStream can
     * optionally be forced to disk.
     *
     * @param writer the writer to close
     * @param os     an underlying FileOutputStream that will to be forced to disk according to the force parameter
     * @param name   the name of the resource
     * @param force  forces the FileOutputStream to disk
     */
    public static void close(Writer writer, FileOutputStream os, String name, boolean force) {
        if (writer != null && force) {
            // flush the writer prior to syncing the FD
            try {
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            force(os, name);
        }
        close(writer, name);
    }

    /**
     * Closes the given resource if it is available, logging any closing exceptions to the given log.
     *
     * @param closeable the object to close
     * @param name      the name of the resource
     */
    public static void close(Closeable closeable, String name) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the given resource if it is available and don't catch the exception
     *
     * @param closeable the object to close
     * @throws IOException
     */
    public static void closeWithException(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // don't catch the exception here
                throw e;
            }
        }
    }

    /**
     * Closes the given channel if it is available, logging any closing exceptions to the given log. The file's channel
     * can optionally be forced to disk.
     *
     * @param channel the file channel
     * @param name    the name of the resource
     * @param force   forces the file channel to disk
     */
    public static void close(FileChannel channel, String name, boolean force) {
        if (force) {
            force(channel, name);
        }
        close(channel, name);
    }

    /**
     * Closes the given resource if it is available.
     *
     * @param closeable the object to close
     */
    public static void close(Closeable closeable) {
        close(closeable, null);
    }

    /**
     * Closes the given resources if they are available.
     *
     * @param closeables the objects to close
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            close(closeable);
        }
    }

    public static void closeIterator(Object it) throws IOException {
        if (it instanceof java.util.Scanner) {
            // special for Scanner which implement the Closeable since JDK7
            java.util.Scanner scanner = (java.util.Scanner) it;
            scanner.close();
            IOException ioException = scanner.ioException();
            if (ioException != null) {
                throw ioException;
            }
        } else if (it instanceof Scanner) {
            // special for Scanner which implement the Closeable since JDK7
            Scanner scanner = (Scanner) it;
            scanner.close();
            IOException ioException = scanner.ioException();
            if (ioException != null) {
                throw ioException;
            }

        } else if (it instanceof Closeable) {
            StreamCommand.closeWithException((Closeable) it);
        }
    }

    public static void validateCharset(String charset) throws UnsupportedCharsetException {
        if (charset != null) {
            if (Charset.isSupported(charset)) {
                Charset.forName(charset);
                return;
            }
        }
        throw new UnsupportedCharsetException(charset);
    }

    /**
     * This method will take off the quotes and double quotes of the charset
     */
    public static String normalizeCharset(String charset) {
        if (charset != null) {
            String answer = charset.trim();
            if (answer.startsWith("'") || answer.startsWith("\"")) {
                answer = answer.substring(1);
            }
            if (answer.endsWith("'") || answer.endsWith("\"")) {
                answer = answer.substring(0, answer.length() - 1);
            }
            return answer.trim();
        } else {
            return null;
        }
    }

    private static String getDefaultCharsetName() {
        return UTF8_CHARSET.name();
    }

    /**
     * Loads the entire stream into memory as a String and returns it.
     * <p/>
     * <b>Notice:</b> This implementation appends a <tt>\n</tt> as line terminator at the of the text.
     * <p/>
     * Warning, don't use for crazy big streams :)
     */
    public static String loadText(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(in);
        try {
            BufferedReader reader = buffered(isr);
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    builder.append(line);
                    builder.append("\n");
                } else {
                    break;
                }
            }
            return builder.toString();
        } finally {
            close(isr, in);
        }
    }

    /**
     * Get the charset name from the content type string
     *
     * @param contentType
     * @return the charset name, or <tt>UTF-8</tt> if no found
     */
    public static String getCharsetNameFromContentType(String contentType) {
        String[] values = contentType.split(";");
        String charset = "";

        for (String value : values) {
            value = value.trim();
            if (value.toLowerCase().startsWith("charset=")) {
                // Take the charset name
                charset = value.substring(8);
            }
        }
        if ("".equals(charset)) {
            charset = "UTF-8";
        }
        return StreamCommand.normalizeCharset(charset);

    }

    private static class NonClosingInputStream extends FilterInputStream {

        public NonClosingInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
        }
    }

    private static class NonClosingOutputStream extends FilterOutputStream {

        public NonClosingOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public void write(byte[] b, int off, int let) throws IOException {
            // It is critical that we override this method for performance
            out.write(b, off, let);
        }

        @Override
        public void close() throws IOException {
        }
    }
}
