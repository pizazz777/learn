package 设计模式.策略模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class PriceTest {

    @Test
    public void test() {
        MemberStrategy strategy = new AdvancedMemberStrategy();
        Price price = new Price(strategy);
        double quote = price.quote(10);
        System.out.println(quote);
    }

}
