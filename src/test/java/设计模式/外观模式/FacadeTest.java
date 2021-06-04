package 设计模式.外观模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class FacadeTest {

    @Test
    public void test() throws Exception {
        Facade facade = new Facade();
        facade.morning();
        Thread.sleep(2000);
        facade.evening();
    }

}
