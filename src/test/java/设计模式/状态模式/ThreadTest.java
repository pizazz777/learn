package 设计模式.状态模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/03
 * @description
 */
public class ThreadTest {

    @Test
    public void test() {
        ThreadContext context = new ThreadContext();
        context.start();
        context.getCPU();
        context.suspend();
        context.resume();
        context.getCPU();
        context.stop();
    }

}
