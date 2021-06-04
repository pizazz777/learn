package 设计模式.策略模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class Price {

    private MemberStrategy strategy;

    public Price(MemberStrategy strategy) {
        this.strategy = strategy;
    }

    public double quote(double bookPrice) {
        return strategy.calcPrice(bookPrice);
    }

}
