package org.gjgr.pig.chivalrous.core.setting;

import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.io.resource.UrlResource;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.log.Log;
import org.gjgr.pig.chivalrous.core.log.LogFactory;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;
import org.gjgr.pig.chivalrous.core.setting.dialect.BasicSetting;
import org.gjgr.pig.chivalrous.core.util.RegexCommand;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Setting文件加载器
 *
 * @author Looly
 */
public class SettingLoader {
    /**
     * 注释符号（当有此符号在行首，表示此行为注释）
     */
    private static final String COMMENT_FLAG_PRE = "#";
    /**
     * 赋值分隔符（用于分隔键值对）
     */
    private static final String ASSIGN_FLAG = "=";
    /**
     * 分组行识别的环绕标记
     */
    private static final char[] GROUP_SURROUND = {'[', ']'};
    private static Log log = LogFactory.get();
    /**
     * 变量名称的正则
     */
    private String reg_var = "\\$\\{(.*?)\\}";

    /**
     * 本设置对象的字符集
     */
    private Charset charset;
    /**
     * 是否使用变量
     */
    private boolean isUseVariable;
    /**
     * Setting
     */
    private BasicSetting setting;

    public SettingLoader(BasicSetting setting) {
        this(setting, CharsetCommand.CHARSET_UTF_8, false);
    }

    public SettingLoader(BasicSetting setting, Charset charset, boolean isUseVariable) {
        this.setting = setting;
        this.charset = charset;
        this.isUseVariable = isUseVariable;
    }

    /**
     * 加载设置文件
     *
     * @param urlResource 配置文件URL
     * @return 加载是否成功
     */
    public boolean load(UrlResource urlResource) {
        if (urlResource == null) {
            throw new NullPointerException("Null setting url define!");
        }
        log.debug("Load setting file [{}]", urlResource);
        InputStream settingStream = null;
        try {
            settingStream = urlResource.getStream();
            load(settingStream);
        } catch (Exception e) {
            log.error(e, "Load setting error!");
            return false;
        } finally {
            IoCommand.close(settingStream);
        }
        return true;
    }

    /**
     * 加载设置文件。 此方法不会关闭流对象
     *
     * @param settingStream 文件流
     * @return 加载成功与否
     * @throws IOException
     */
    public boolean load(InputStream settingStream) throws IOException {
        setting.clear();
        BufferedReader reader = null;
        try {
            reader = IoCommand.getReader(settingStream, this.charset);
            // 分组
            String group = null;

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                // 跳过注释行和空行
                if (StringCommand.isBlank(line) || line.startsWith(COMMENT_FLAG_PRE)) {
                    continue;
                }

                // 记录分组名
                if (line.charAt(0) == GROUP_SURROUND[0] && line.charAt(line.length() - 1) == GROUP_SURROUND[1]) {
                    group = line.substring(1, line.length() - 1).trim();
                    this.setting.getGroups().add(group);
                    continue;
                }

                String[] keyValue = line.split(ASSIGN_FLAG, 2);
                // 跳过不符合简直规范的行
                if (keyValue.length < 2) {
                    continue;
                }

                String key = keyValue[0].trim();
                if (false == StringCommand.isBlank(group)) {
                    key = group + StringCommand.DOT + key;
                }
                String value = keyValue[1].trim();

                // 替换值中的所有变量变量（变量必须是此行之前定义的变量，否则无法找到）
                if (isUseVariable) {
                    value = replaceVar(value);
                }
                this.setting.put(key, value);
            }
        } finally {
            IoCommand.close(reader);
        }
        return true;
    }

    /**
     * 设置变量的正则<br/>
     * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
     *
     * @param regex 正则
     */
    public void setVarRegex(String regex) {
        this.reg_var = regex;
    }

    /**
     * 持久化当前设置，会覆盖掉之前的设置<br>
     * 持久化会不会保留之前的分组
     *
     * @param absolutePath 设置文件的绝对路径
     */
    public void store(String absolutePath) {
        Writer writer = null;
        try {
            writer = FileCommand.bufferedWriter(absolutePath, charset, false);
            for (Entry<Object, Object> entry : this.setting.entrySet()) {
                writer.write(StringCommand.format("{} {} {}", entry.getKey(), ASSIGN_FLAG, entry.getValue()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(StringCommand.format("Can not find file [{}]!", absolutePath), e);
        } catch (IOException e) {
            throw new RuntimeException("Store Setting error!", e);
        } finally {
            IoCommand.close(writer);
        }
    }

    public void autoReload() {
    }

    // ----------------------------------------------------------------------------------- Private method start

    /**
     * 替换给定值中的变量标识
     *
     * @param value 值
     * @return 替换后的字符串
     */
    private String replaceVar(String value) {
        // 找到所有变量标识
        final Set<String> vars = RegexCommand.findAll(reg_var, value, 0, new HashSet<String>());
        for (String var : vars) {
            // 查找变量名对应的值
            Object varValue = this.setting.get(RegexCommand.get(reg_var, var, 1));
            if (null != varValue && value instanceof CharSequence) {
                // 替换标识
                value = value.replace(var, (CharSequence) varValue);
            }
        }
        return value;
    }
    // ----------------------------------------------------------------------------------- Private method end
}
