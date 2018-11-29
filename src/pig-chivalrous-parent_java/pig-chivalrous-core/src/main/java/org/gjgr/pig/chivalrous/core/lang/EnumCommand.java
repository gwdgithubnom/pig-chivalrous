package org.gjgr.pig.chivalrous.core.lang;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @Author gwd
 * @Time 10-29-2018  Monday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class EnumCommand {
    /**
     * 将字符串转换为 EnumSet. <br>
     * 例如： 对于
     * <pre> <code>
     * public enum Type { A, B, C, D }
     * 方法 parse(Type.class, "A;B", ";") 将返回一个 EnumSet<Type>, 其中包括 Type.A, Type.B
     * </pre>
     *
     * @param <E>
     * @param clazz
     * @param input
     * @param separator
     * @return
     */
    public <E extends Enum<E>> EnumSet<E> parse(Class<E> clazz, String input, String separator) {
        Validate.notNull(input, "input");
        Validate.notNull(separator, "separator");

        String values[] = input.split(separator);
        List<E> list = new ArrayList<E>();
        for (String value : values) {
            list.add(Enum.valueOf(clazz, value.trim()));
        }

        return EnumSet.copyOf(list);
    }
}
