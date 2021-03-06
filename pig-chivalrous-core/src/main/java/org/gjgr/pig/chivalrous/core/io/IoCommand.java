package org.gjgr.pig.chivalrous.core.io;

import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import org.gjgr.pig.chivalrous.core.convert.Convert;
import org.gjgr.pig.chivalrous.core.io.exception.IORuntimeException;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.io.resource.LocationCommand;
import org.gjgr.pig.chivalrous.core.io.stream.BOMInputStream;
import org.gjgr.pig.chivalrous.core.io.stream.FastByteArrayOutputStream;
import org.gjgr.pig.chivalrous.core.io.stream.StreamProgress;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.math.HexCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IO工具类
 *
 * @author xiaoleilu
 */
public final class IoCommand {

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 4096;
    /**
     * 数据流末尾
     */
    public static final int EOF = -1;
    private static final String DEFAULT_ENCODING = Charset.defaultCharset().name();
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final Logger logger = LoggerFactory.getLogger(IoCommand.class);

    private IoCommand() {
    }

    /**
     * Close the closeable, and ignore IOException.
     *
     * @param closeable
     * @return
     */
    public static boolean closeIgnoreException(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 读文件，返回文件内容
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String readAllTexts(File file) throws FileNotFoundException {
        return readAllTexts(file, DEFAULT_ENCODING);

    }

    /**
     * 读文件，返回文件内容
     *
     * @param file
     * @param encoding
     * @return
     * @throws FileNotFoundException
     */
    public static String readAllTexts(File file, String encoding) throws FileNotFoundException {
        List<String> lines = readAllLines(file, encoding);
        return StringUtils.join(lines, LINE_SEPARATOR);
    }

    /**
     * 读文件， 返回文件中的所有行。
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static List<String> readAllLines(File file) throws FileNotFoundException {
        return readAllLines(file, DEFAULT_ENCODING);
    }

    /**
     * 读文件， 返回文件中的所有行。
     *
     * @param file
     * @param encoding
     * @return
     * @throws FileNotFoundException
     */
    public static List<String> readAllLines(File file, String encoding) throws FileNotFoundException {
        return readAllLines(new FileInputStream(file), encoding);
    }

    /**
     * 读InputStream， 返回InputStream的所有行。
     *
     * @param is
     * @param encoding
     * @return
     */
    public static List<String> readAllLines(InputStream is, String encoding) {
        List<String> lines = new ArrayList<String>();
        Scanner scanner = new Scanner(new BufferedInputStream(is), encoding);
        try {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
        return lines;
    }

    /**
     * 将文本内容写到文件中。
     *
     * @param file
     * @param content
     * @param append
     * @throws IOException
     */
    public static void writeAllTexts(File file, String content, boolean append) throws IOException {
        writeAllTexts(file, content, append, DEFAULT_ENCODING);
    }

    /**
     * 将文本内容写到文件中。
     *
     * @param file
     * @param content
     * @param append
     * @param encoding
     * @throws IOException
     */
    public static void writeAllTexts(File file, String content, boolean append, String encoding) throws IOException {
        writeAllLines(file, Arrays.asList(content), append, encoding);
    }

    /**
     * 将多行文本写到文件中。
     *
     * @param file
     * @param lines
     * @param append
     * @throws IOException
     */
    public static void writeAllLines(File file, Collection<String> lines, boolean append) throws IOException {
        writeAllLines(file, lines, append, DEFAULT_ENCODING);
    }

    /**
     * 将多行文本写到文件中。
     *
     * @param file
     * @param lines
     * @param append
     * @param encoding
     * @throws IOException
     */
    public static void writeAllLines(File file, Collection<String> lines, boolean append, String encoding)
            throws IOException {
        writeAllLines(new FileOutputStream(file, append), lines, encoding);
    }

    /**
     * 将多行文本写到文件中。
     *
     * @param os
     * @param lines
     * @param encoding
     * @throws IOException
     */
    public static void writeAllLines(OutputStream os, Collection<String> lines, String encoding) throws IOException {
        Writer out = new OutputStreamWriter(new BufferedOutputStream(os), encoding);
        try {
            boolean isFirst = true;
            for (String line : lines) {
                if (!isFirst) {
                    out.write(LINE_SEPARATOR);
                }
                out.write(line);
                isFirst = false;
            }
        } finally {
            out.close();
        }
    }

    // -------------------------------------------------------------------------------------- Copy start

    /**
     * 将Reader中的内容复制到Writer中 使用默认缓存大小
     *
     * @param reader Reader
     * @param writer Writer
     * @return 拷贝的字节数
     * @throws IOException
     */
    public static long copy(Reader reader, Writer writer) throws IOException {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 将Reader中的内容复制到Writer中
     *
     * @param reader     Reader
     * @param writer     Writer
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IOException
     */
    public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        return copy(reader, writer, bufferSize, null);
    }

    /**
     * 将Reader中的内容复制到Writer中
     *
     * @param reader     Reader
     * @param writer     Writer
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IOException
     */
    public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress)
            throws IOException {
        char[] buffer = new char[bufferSize];
        long size = 0;
        int readSize;
        if (null != streamProgress) {
            streamProgress.start();
        }
        while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
            writer.write(buffer, 0, readSize);
            size += readSize;
            writer.flush();
            if (null != streamProgress) {
                streamProgress.progress(size);
            }
        }
        if (null != streamProgress) {
            streamProgress.finish();
        }
        return size;
    }

    /**
     * 拷贝流，使用默认Buffer大小
     *
     * @param in  输入流
     * @param out 输出流
     * @return 传输的byte数
     * @throws IOException
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 拷贝流
     *
     * @param in         输入流
     * @param out        输出流
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IOException
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        return copy(in, out, bufferSize, null);
    }

    /**
     * 拷贝流
     *
     * @param in             输入流
     * @param out            输出流
     * @param bufferSize     缓存大小
     * @param streamProgress 进度条
     * @return 传输的byte数
     * @throws IOException
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress)
            throws IOException {
        if (null == in) {
            throw new NullPointerException("InputStream is null!");
        }
        if (null == out) {
            throw new NullPointerException("OutputStream is null!");
        }
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        byte[] buffer = new byte[bufferSize];
        long size = 0;
        if (null != streamProgress) {
            streamProgress.start();
        }
        for (int readSize = -1; (readSize = in.read(buffer)) != EOF; ) {
            out.write(buffer, 0, readSize);
            size += readSize;
            out.flush();
            if (null != streamProgress) {
                streamProgress.progress(size);
            }
        }
        if (null != streamProgress) {
            streamProgress.finish();
        }
        return size;
    }

    /**
     * 拷贝流 thanks to:
     * https://github.com/venusdrogon/feilong-io/blob/master/src/main/java/com/feilong/io/IOWriteUtil.java
     *
     * @param in             输入流
     * @param out            输出流
     * @param bufferSize     缓存大小
     * @param streamProgress 进度条
     * @return 传输的byte数
     * @throws IOException
     */
    public static long copyByNIO(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress)
            throws IOException {
        return copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize, streamProgress);
    }

    /**
     * 拷贝文件流，使用NIO
     *
     * @param in  输入
     * @param out 输出
     * @return 拷贝的字节数
     * @throws IOException
     */
    public static long copy(FileInputStream in, FileOutputStream out) throws IOException {
        if (null == in) {
            throw new NullPointerException("FileInputStream is null!");
        }
        if (null == out) {
            throw new NullPointerException("FileOutputStream is null!");
        }

        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        return inChannel.transferTo(0, inChannel.size(), outChannel);
    }

    /**
     * 拷贝流，使用NIO，不会关闭流
     *
     * @param in  {@link ReadableByteChannel}
     * @param out {@link WritableByteChannel}
     * @return 拷贝的字节数
     * @throws IOException
     */
    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize,
                            StreamProgress streamProgress)
            throws IOException {
        if (null == in) {
            throw new NullPointerException("In is null!");
        }
        if (null == out) {
            throw new NullPointerException("Out is null!");
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        long size = 0;
        if (null != streamProgress) {
            streamProgress.start();
        }
        while (in.read(byteBuffer) != EOF) {
            byteBuffer.flip();// 写转读
            size += out.write(byteBuffer);
            byteBuffer.clear();
            if (null != streamProgress) {
                streamProgress.progress(size);
            }
        }
        if (null != streamProgress) {
            streamProgress.finish();
        }

        return size;
    }
    // -------------------------------------------------------------------------------------- Copy end

    // -------------------------------------------------------------------------------------- getReader and getWriter
    // start

    /**
     * 获得一个文件读取器
     *
     * @param in          输入流
     * @param charsetName 字符集名称
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(InputStream in, String charsetName) {
        return getReader(in, Charset.forName(charsetName));
    }

    /**
     * 获得一个Reader
     *
     * @param in      输入流
     * @param charset 字符集
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(InputStream in, Charset charset) {
        if (null == in) {
            return null;
        }

        InputStreamReader reader = null;
        if (null == charset) {
            reader = new InputStreamReader(in);
        } else {
            reader = new InputStreamReader(in, charset);
        }

        return new BufferedReader(reader);
    }

    /**
     * 获得一个Writer
     *
     * @param out         输入流
     * @param charsetName 字符集
     * @return OutputStreamWriter对象
     * @throws IOException
     */
    public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
        return getWriter(out, Charset.forName(charsetName));
    }

    /**
     * 获得一个Writer
     *
     * @param out     输入流
     * @param charset 字符集
     * @return OutputStreamWriter对象
     * @throws IOException
     */
    public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
        if (null == out) {
            return null;
        }

        if (null == charset) {
            return new OutputStreamWriter(out);
        } else {
            return new OutputStreamWriter(out, charset);
        }
    }
    // -------------------------------------------------------------------------------------- getReader and getWriter
    // end

    // -------------------------------------------------------------------------------------- read start

    /**
     * 从流中读取内容
     *
     * @param in          输入流
     * @param charsetName 字符集
     * @return 内容
     * @throws IOException
     */
    public static String read(InputStream in, String charsetName) throws IOException {
        FastByteArrayOutputStream out = read(in);
        return StringCommand.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
    }

    /**
     * 从流中读取内容
     *
     * @param in      输入流
     * @param charset 字符集
     * @return 内容
     * @throws IOException
     */
    public static String read(InputStream in, Charset charset) throws IOException {
        FastByteArrayOutputStream out = read(in);
        return null == charset ? out.toString() : out.toString(charset);
    }

    /**
     * 从流中读取内容，读到输出流中
     *
     * @param in 输入流
     * @return 输出流
     * @throws IOException
     */
    public static FastByteArrayOutputStream read(InputStream in) throws IOException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out;
    }

    /**
     * 从Reader中读取String
     *
     * @param reader Reader
     * @return String
     * @throws IOException
     */
    public static String read(Reader reader) throws IOException {
        final StringBuilder builder = StringCommand.builder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        while (-1 != reader.read(buffer)) {
            builder.append(buffer.flip().toString());
        }
        return builder.toString();
    }

    /**
     * 从FileChannel中读取内容
     *
     * @param fileChannel 文件管道
     * @param charsetName 字符集
     * @return 内容
     * @throws IOException
     */
    public static String read(FileChannel fileChannel, String charsetName) throws IOException {
        return read(fileChannel, CharsetCommand.charset(charsetName));
    }

    /**
     * 从FileChannel中读取内容
     *
     * @param fileChannel 文件管道
     * @param charset     字符集
     * @return 内容
     * @throws IOException
     */
    public static String read(FileChannel fileChannel, Charset charset) throws IOException {
        final MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
        return StringCommand.str(buffer, charset);
    }

    /**
     * 从流中读取bytes
     *
     * @param in {@link InputStream}
     * @return bytes
     * @throws IOException
     */
    public static byte[] readBytes(InputStream in) throws IOException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * 读取指定长度的byte数组
     *
     * @param in     {@link InputStream}
     * @param length 长度
     * @return bytes
     * @throws IOException
     */
    public static byte[] readBytes(InputStream in, int length) throws IOException {
        byte[] b = new byte[length];
        in.read(b);
        return b;
    }

    /**
     * 读取16进制字符串
     *
     * @param in          {@link InputStream}
     * @param length      长度
     * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
     * @return 16进制字符串
     * @throws IOException
     */
    public static String readHex(InputStream in, int length, boolean toLowerCase) throws IOException {
        return HexCommand.encodeHexStr(readBytes(in, length), toLowerCase);
    }

    /**
     * 从流中读取前28个byte并转换为16进制，字母部分使用大写
     *
     * @param in {@link InputStream}
     * @return 16进制字符串
     * @throws IOException
     */
    public static String readHex28Upper(InputStream in) throws IOException {
        return readHex(in, 28, false);
    }

    /**
     * 从流中读取前28个byte并转换为16进制，字母部分使用小写
     *
     * @param in {@link InputStream}
     * @return 16进制字符串
     * @throws IOException
     */
    public static String readHex28Lower(InputStream in) throws IOException {
        return readHex(in, 28, false);
    }

    /**
     * 从流中读取内容，读到输出流中
     *
     * @param in 输入流
     * @return 输出流
     * @throws IOException
     */
    public static <T> T readObj(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(in);
            @SuppressWarnings("unchecked") // may fail with CCE if serialised form is incorrect
            final T obj = (T) ois.readObject();
            return obj;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * 从流中读取内容
     *
     * @param in          输入流
     * @param charsetName 字符集
     * @param collection  返回集合
     * @return 内容
     * @throws IOException
     */
    public static <T extends Collection<String>> T readLines(InputStream in, String charsetName, T collection)
            throws IOException {
        return readLines(in, CharsetCommand.charset(charsetName), collection);
    }

    /**
     * 从流中读取内容
     *
     * @param in         输入流
     * @param charset    字符集
     * @param collection 返回集合
     * @return 内容
     * @throws IOException
     */
    public static <T extends Collection<String>> T readLines(InputStream in, Charset charset, T collection)
            throws IOException {
        // 从返回的内容中读取所需内容
        BufferedReader reader = getReader(in, charset);
        String line = null;
        while ((line = reader.readLine()) != null) {
            collection.add(line);
        }

        return collection;
    }
    // -------------------------------------------------------------------------------------- read end

    /**
     * String 转为流
     *
     * @param content     内容
     * @param charsetName 编码
     * @return 字节流
     */
    public static ByteArrayInputStream toStream(String content, String charsetName) {
        return toStream(content, CharsetCommand.charset(charsetName));
    }

    /**
     * String 转为流
     *
     * @param content 内容
     * @param charset 编码
     * @return 字节流
     */
    public static ByteArrayInputStream toStream(String content, Charset charset) {
        if (content == null) {
            return null;
        }
        return new ByteArrayInputStream(StringCommand.bytes(content, charset));
    }

    /**
     * 文件转为流
     *
     * @param file 文件
     * @return {@link FileInputStream}
     */
    public static FileInputStream toStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将byte[]写到流中
     *
     * @param out        输出流
     * @param isCloseOut 写入完毕是否关闭输出流
     * @param content    写入的内容
     * @throws IOException
     */
    public static void write(OutputStream out, boolean isCloseOut, byte[] content) throws IOException {
        try {
            out.write(content);
        } finally {
            if (isCloseOut) {
                close(out);
            }
        }
    }

    /**
     * 将多部分内容写到流中，自动转换为字符串
     *
     * @param out        输出流
     * @param charset    写出的内容的字符集
     * @param isCloseOut 写入完毕是否关闭输出流
     * @param contents   写入的内容，调用toString()方法，不包括不会自动换行
     * @throws IOException
     */
    public static void write(OutputStream out, String charset, boolean isCloseOut, Object... contents)
            throws IOException {
        OutputStreamWriter osw = null;
        try {
            osw = getWriter(out, charset);
            for (Object content : contents) {
                if (content != null) {
                    osw.write(Convert.toStr(content, StringCommand.EMPTY));
                    osw.flush();
                }
            }
        } catch (Exception e) {
            throw new IOException("Write content to OutputStream error!", e);
        } finally {
            if (isCloseOut) {
                close(osw);
            }
        }
    }

    /**
     * 将多部分内容写到流中
     *
     * @param out        输出流
     * @param charset    写出的内容的字符集
     * @param isCloseOut 写入完毕是否关闭输出流
     * @param contents   写入的内容
     * @throws IOException
     */
    public static void writeObjects(OutputStream out, String charset, boolean isCloseOut, Serializable... contents)
            throws IOException {
        ObjectOutputStream osw = null;
        try {
            osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
            for (Object content : contents) {
                if (content != null) {
                    osw.writeObject(content);
                    osw.flush();
                }
            }
        } catch (Exception e) {
            throw new IOException("Write content to OutputStream error!", e);
        } finally {
            if (isCloseOut) {
                close(osw);
            }
        }
    }

    /**
     * 关闭<br>
     * 关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
        }
    }

    /**
     * 关闭<br>
     * 关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
        }
    }

    /**
     * This implementation opens an InputStream for the given class path resource.
     *
     * @see java.lang.ClassLoader#getResourceAsStream(String)
     * @see java.lang.Class#getResourceAsStream(String)
     */
    public static InputStream getInputStream(String path, Class clazz, ClassLoader classLoader) {
        InputStream is = null;
        if(FileCommand.isExist(path)){
            try {
                is =  new FileInputStream(path);
            } catch (FileNotFoundException e) {
                logger.error("found in File, but load file failed {}",path);
            }
        }else {
            String dir = LocationCommand.userDir();
            if(FileCommand.isExist(dir+File.separator+path)){
                try {
                    is =  new FileInputStream(dir+File.separator+path);
                } catch (FileNotFoundException e) {
                    logger.error("found in File, but load file failed {}",path);
                }
            }
        }
        if(is ==null){
            if (clazz != null) {
                is = clazz.getResourceAsStream(path);
                if (is ==null && classLoader != null) {
                    is = classLoader.getResourceAsStream(path);
                }else{
                    is = clazz.getClassLoader().getResourceAsStream(path);
                    if(is==null){
                        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                        logger.warn("failed read by {}, use loader {}",clazz,Thread.currentThread().getContextClassLoader());
                    }
                }
            }else{
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            }
        }

        if (is == null) {
            logger.warn(path + " cannot be opened because it does not exist");
        }
        return is;
    }

    public static InputStream tryInputStreamUserSystemExternal(String path,Class clazz, ClassLoader classLoader) {
        InputStream inputStream;
        if (System.getenv(path) != null) {
            if (FileCommand.isExist(System.getenv(path))) {
                inputStream = getInputStream(System.getProperty(path),clazz,classLoader);
            } else {
                inputStream = null;
            }
        } else {
            inputStream = null;
        }
        if (inputStream == null) {
            if (FileCommand.isExist(System.getProperty(path))) {
                try {
                    inputStream = getInputStream(System.getProperty(path),clazz,classLoader);
                } catch (RuntimeException e) {
                    inputStream = null;
                }
            } else {
                inputStream = null;
            }
        }
        return inputStream;
    }


    public static InputStream tryInputStreamUserSystemDefault(String key, String filename,Class clazz,ClassLoader classLoader) {
        InputStream inputStream;
        if (System.getProperty(key) != null) {
            if (FileCommand.isExist(System.getProperty(key) + File.separator + (filename))) {
                try {
                    inputStream = getInputStream(System.getProperty(key) + File.separator + (filename),clazz,classLoader);
                } catch (RuntimeException e) {
                    inputStream = null;
                }
            } else {
                inputStream = null;
            }
        } else {
            inputStream = null;
        }
        if (inputStream == null) {
            if (System.getenv(key) != null) {
                if (FileCommand.isExist(System.getenv(key) + File.separator + (filename))) {
                    try {
                        inputStream = getInputStream(System.getenv(key) + File.separator + (filename),clazz,classLoader);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    inputStream = null;
                }
            } else {
                inputStream = null;
            }
        }
        return inputStream;
    }


    public static InputStream inputStream(String path,Class clazz,ClassLoader classLoader) {
        InputStream inputStream;
        try {
            inputStream = getInputStream(path,clazz,classLoader);
        } catch (RuntimeException e) {
            logger.error("failed read data ",e);
            e.printStackTrace();
            inputStream = null;
        }
        if (inputStream == null) {
            inputStream = tryInputStreamUserSystemExternal(path,clazz,classLoader);
            if(inputStream==null){
                // LocationCommand.path(path)
                inputStream = tryInputStreamUserSystemDefault("user.dir", path,clazz,classLoader);
                if (inputStream == null) {
                    inputStream = tryInputStreamUserSystemDefault("basedir", path,clazz,classLoader);
                }
                if (inputStream == null) {
                    logger.warn("should define the {} file location.", path);
                }
            }
        }
        return inputStream;
    }

    /**
     * find file by clazz path location, and return the @See InputStreem
     *
     * @param clazz
     * @param path
     * @return
     */
    public static InputStream inputStream(String path,Class clazz) {
        return inputStream(path,clazz,null);
    }


    public static InputStream inputStream(String path){
        return inputStream(path,null,null);
    }

    /**
     * 获得输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws FileNotFoundException
     */
    public static BufferedInputStream bufferedInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
    }

    public static BufferedInputStream bufferedInputStream(InputStream inputStream){
        return new BufferedInputStream(inputStream);
    }

    /**
     * 获得输入流
     *
     * @param path 文件路径
     * @return 输入流
     * @throws FileNotFoundException
     */
    public static BufferedInputStream bufferedInputStream(String path) throws FileNotFoundException {
        return bufferedInputStream(inputStream(path));
    }

    // -------------------------------------------------------------------------------------------- out start

    /**
     * 获得BOM输入流，用于处理带BOM头的文件
     *
     * @param file 文件
     * @return 输入流
     * @throws FileNotFoundException
     */
    public static BOMInputStream bomInputStream(File file) throws FileNotFoundException {
        return new BOMInputStream(new FileInputStream(file));
    }

    public static InputStream debug(String path){
        return inputStream(path);
    }


    /**
     * 获得一个输出流对象
     *
     * @param file 文件
     * @return 输出流对象
     * @throws IOException
     */
    public static BufferedOutputStream bufferedOutputStream(File file) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    /**
     * 获得一个输出流对象
     *
     * @param path 输出到的文件路径，绝对路径
     * @return 输出流对象
     * @throws IOException
     */
    public static BufferedOutputStream bufferedOutputStream(String path) throws IOException {
        return bufferedOutputStream(path);
    }

    public static InputStream inputStream(File file) {
        return inputStream(file.getAbsolutePath());
    }

    public static InputStream inputStream(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }




}
