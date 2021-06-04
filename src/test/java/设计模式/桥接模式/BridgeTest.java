package 设计模式.桥接模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class BridgeTest {

    @Test
    public void test() {
        Bridge bridge = new Bridge();
        bridge.setDriverManager(new OracleDriver());
        bridge.connection();

        bridge.setDriverManager(new MysqlDriver());
        bridge.connection();
    }

}
