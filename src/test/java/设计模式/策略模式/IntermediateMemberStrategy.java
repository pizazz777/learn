package 设计模式.策略模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class IntermediateMemberStrategy implements MemberStrategy {

    /**
     * 计算图书的价格
     *
     * @param booksPrice 图书的原价
     * @return 计算出打折后的价格
     */
    @Override
    public double calcPrice(double booksPrice) {
        System.out.println("对于中级会员的折扣为九折");
        return booksPrice * 0.9;
    }

}
