package org.gjgr.pig.chivalrous.core.json;

import org.junit.Test;

/**
 * @author gongwendong
 * @time 12-07-2020  星期一
 * @description: miparent:
 * @target:
 * @more:
 */
public class GsonObjectTest {
    @Test
    public void testConvert(){
        String string="{}";
        GsonObject gsonObject = JsonCommand.gsonObject(string);
        System.out.println(gsonObject);
    }

    public static void main(String[] args) {
        GsonObjectTest gsonObjectTest = new GsonObjectTest();
        gsonObjectTest.testConvert();
    }

    @Test
    public void testJsonCommandParse(){
        Sun sun = new Sun();
        sun.setName("sun star");
        String string = JsonCommand.json(sun);
        System.out.println(string);
        Sun theSun = JsonCommand.fromJson(string,Sun.class);
        System.out.println(theSun);
    }
}
