package 设计模式.命令模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description
 */
public class InvokeTest {


    @Test
    public void test() {
        // 被调用者
        Receiver receiver = new Receiver();
        // 命令者
        Command command = new MyCommand(receiver);
        // 调用者
        Invoke invoke = new Invoke(command);
        invoke.invoke("士兵许三多");
    }

}
