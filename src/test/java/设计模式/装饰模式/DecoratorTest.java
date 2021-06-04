package 设计模式.装饰模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class DecoratorTest {

    @Test
    public void test() {
        // 指定了装饰者需要装饰的是哪一个类
        Source source = new Source();
        Decorator decorator = new Decorator(source);
        decorator.method();
    }

}
