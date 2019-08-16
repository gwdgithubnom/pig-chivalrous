package org.gjgr.pig.chivalrous.aop.test;

import org.gjgr.pig.chivalrous.core.aop.ProxyCommand;
import org.gjgr.pig.chivalrous.core.aop.aspects.TimeIntervalAspect;
import org.gjgr.pig.chivalrous.core.lang.Console;
import org.junit.Test;

/**
 * AOP模块单元测试
 *
 * @author Looly
 */
public class AopTest {

    @Test
    public void aopTest() {
        Animal cat = ProxyCommand.proxy(new Cat(), TimeIntervalAspect.class);
        cat.eat();
    }

    static interface Animal {
        void eat();
    }

    static class Cat implements Animal {

        @Override
        public void eat() {
            Console.log("猫吃鱼");
        }

    }
}
