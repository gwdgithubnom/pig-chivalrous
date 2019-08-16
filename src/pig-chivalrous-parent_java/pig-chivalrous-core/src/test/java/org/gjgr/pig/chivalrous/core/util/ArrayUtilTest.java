package org.gjgr.pig.chivalrous.core.util;

import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;
import org.gjgr.pig.chivalrous.core.lang.Editor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * 数组工具单元测试
 *
 * @author Looly
 */
public class ArrayUtilTest {

    @Test
    public void isEmptyTest() {
        int[] a = {};
        int[] b = null;
        Assert.assertTrue(ArrayCommand.isEmpty(a));
        Assert.assertTrue(ArrayCommand.isEmpty(b));
    }

    @Test
    public void isNotEmptyTest() {
        int[] a = {1, 2};
        Assert.assertTrue(ArrayCommand.isNotEmpty(a));
    }

    @Test
    public void newArrayTest() {
        String[] newArray = ArrayCommand.newArray(String.class, 3);
        Assert.assertEquals(3, newArray.length);
    }

    @Test
    public void cloneTest() {
        Integer[] b = {1, 2, 3};
        Integer[] cloneB = ArrayCommand.clone(b);
        Assert.assertArrayEquals(b, cloneB);

        int[] a = {1, 2, 3};
        int[] clone = ArrayCommand.clone(a);
        Assert.assertArrayEquals(a, clone);
    }

    @Test
    public void filterTest() {
        Integer[] a = {1, 2, 3, 4, 5, 6};
        Integer[] filter = ArrayCommand.filter(a, new Editor<Integer>() {
            @Override
            public Integer edit(Integer t) {
                return (t % 2 == 0) ? t : null;
            }
        });
        Assert.assertArrayEquals(filter, new Integer[] {2, 4, 6});
    }

    @Test
    public void mapTest() {
        String[] keys = {"a", "b", "c"};
        Integer[] values = {1, 2, 3};
        Map<String, Integer> map = ArrayCommand.zip(keys, values, true);
        Assert.assertEquals(map.toString(), "{a=1, b=2, c=3}");
    }
}
