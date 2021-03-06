package org.gjgr.pig.chivalrous.core.io.file;

import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.exception.IORuntimeException;
import org.gjgr.pig.chivalrous.core.lang.AssertCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文件写入器
 *
 * @author Looly
 */
public class FileWriter extends FileWrapper {
    /**
     * 构造
     *
     * @param file    文件
     * @param charset 编码，使用 {@link CharsetCommand}
     */
    public FileWriter(File file, Charset charset) {
        super(file, charset);
        checkFile();
    }

    /**
     * 构造
     *
     * @param file    文件
     * @param charset 编码，使用 {@link CharsetCommand#charset(String)}
     */
    public FileWriter(File file, String charset) {
        this(file, CharsetCommand.charset(charset));
    }

    // ------------------------------------------------------- Constructor start

    /**
     * 构造
     *
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     * @param charset  编码，使用 {@link CharsetCommand}
     */
    public FileWriter(String filePath, Charset charset) {
        this(FileCommand.file(filePath), charset);
    }

    /**
     * 构造
     *
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     * @param charset  编码，使用 {@link CharsetCommand#charset(String)}
     */
    public FileWriter(String filePath, String charset) {
        this(FileCommand.file(filePath), CharsetCommand.charset(charset));
    }

    /**
     * 构造<br>
     * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
     *
     * @param file 文件
     */
    public FileWriter(File file) {
        this(file, DEFAULT_CHARSET);
    }

    /**
     * 构造<br>
     * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
     *
     * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
     */
    public FileWriter(String filePath) {
        this(filePath, DEFAULT_CHARSET);
    }

    /**
     * 创建 FileWriter
     *
     * @param file    文件
     * @param charset 编码，使用 {@link CharsetCommand}
     * @return {@link FileWriter}
     */
    public static FileWriter create(File file, Charset charset) {
        return new FileWriter(file, charset);
    }

    /**
     * 创建 FileWriter, 编码：{@link FileWrapper#DEFAULT_CHARSET}
     *
     * @param file 文件
     * @return {@link FileWriter}
     */
    public static FileWriter create(File file) {
        return new FileWriter(file);
    }
    // ------------------------------------------------------- Constructor end

    /**
     * 将String写入文件
     *
     * @param content  写入的内容
     * @param isAppend 是否追加
     * @throws IORuntimeException
     */
    public File write(String content, boolean isAppend) throws IORuntimeException {
        BufferedWriter writer = null;
        try {
            writer = getWriter(isAppend);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoCommand.close(writer);
        }
        return file;
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @return File
     * @throws IORuntimeException
     */
    public File write(String content) throws IORuntimeException {
        return write(content, false);
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public File append(String content) throws IORuntimeException {
        return write(content, true);
    }

    /**
     * 将列表写入文件，覆盖模式
     *
     * @param list 列表
     * @return File
     * @throws IORuntimeException
     */
    public <T> File writeLines(Collection<T> list) throws IORuntimeException {
        return writeLines(list, false);
    }

    public <T> File appendLine(String data) throws IORuntimeException {
        List list = new ArrayList<>();
        list.add(data);
        return appendLines(list);
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param list 列表
     * @return File
     * @throws IORuntimeException
     */
    public <T> File appendLines(Collection<T> list) throws IORuntimeException {
        return writeLines(list, true);
    }

    public <T> File writeLine(String data) throws IORuntimeException {
        List list = new ArrayList<>();
        list.add(data);
        return writeLines(list);
    }

    /**
     * 将列表写入文件
     *
     * @param list     列表
     * @param isAppend 是否追加
     * @return File
     * @throws IORuntimeException
     */
    public <T> File writeLines(Collection<T> list, boolean isAppend) throws IORuntimeException {
        PrintWriter writer = null;
        try {
            writer = getPrintWriter(isAppend);
            for (T t : list) {
                if (t != null) {
                    writer.println(t.toString());
                    writer.flush();
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoCommand.close(writer);
        }
        return this.file;
    }

    /**
     * 写入数据到文件
     *
     * @param data 数据
     * @param off  数据开始位置
     * @param len  数据长度
     * @return File
     * @throws IORuntimeException
     */
    public File write(byte[] data, int off, int len) throws IORuntimeException {
        return write(data, off, len, false);
    }

    /**
     * 追加数据到文件
     *
     * @param data 数据
     * @param off  数据开始位置
     * @param len  数据长度
     * @return File
     * @throws IORuntimeException
     */
    public File append(byte[] data, int off, int len) throws IORuntimeException {
        return write(data, off, len, true);
    }

    /**
     * 写入数据到文件
     *
     * @param data     数据
     * @param off      数据开始位置
     * @param len      数据长度
     * @param isAppend 是否追加模式
     * @return File
     * @throws IORuntimeException
     */
    public File write(byte[] data, int off, int len, boolean isAppend) throws IORuntimeException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FileCommand.touch(file), isAppend);
            out.write(data, off, len);
            out.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoCommand.close(out);
        }
        return file;
    }

    /**
     * 将流的内容写入文件<br>
     * 此方法不会关闭输入流
     *
     * @param in 输入流，不关闭
     * @return dest
     * @throws IOException
     */
    public File writeFromStream(InputStream in) throws IORuntimeException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FileCommand.touch(file));
            IoCommand.copy(in, out);
        } catch (Exception e) {
            throw new IORuntimeException(e);
        } finally {
            IoCommand.close(out);
        }
        return file;
    }

    /**
     * 获得一个输出流对象
     *
     * @return 输出流对象
     * @throws IOException
     */
    public BufferedOutputStream getOutputStream() throws IORuntimeException {
        try {
            return new BufferedOutputStream(new FileOutputStream(FileCommand.touch(file)));
        } catch (Exception e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IOException
     */
    public BufferedWriter getWriter(boolean isAppend) throws IORuntimeException {
        try {
            return new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(FileCommand.touch(file), isAppend), charset));
        } catch (Exception e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IOException
     */
    public PrintWriter getPrintWriter(boolean isAppend) throws IOException {
        return new PrintWriter(getWriter(isAppend));
    }

    /**
     * 检查文件
     *
     * @throws IOException
     */
    private void checkFile() throws IORuntimeException {
        AssertCommand.notNull(file, "File to write content is null !");
        if (this.file.exists() && false == file.isFile()) {
            throw new IORuntimeException("File [{}] is not a file !", this.file.getAbsoluteFile());
        }
    }
}
