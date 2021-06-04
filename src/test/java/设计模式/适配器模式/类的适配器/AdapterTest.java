package 设计模式.适配器模式.类的适配器;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class AdapterTest {

    @Test
    public void test() {
        Adapter adapter = new Adapter();
        adapter.eat();
        adapter.drink();
    }

}
