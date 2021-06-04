package 设计模式.代理模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class ProxyTest {

    @Test
    public void test() {
        // 不知道代理了那个对象
        Subject subject = new Proxy();
        subject.doAction();
        System.out.println(subject);
    }

}
