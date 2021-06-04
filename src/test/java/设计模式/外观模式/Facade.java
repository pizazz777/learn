package 设计模式.外观模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description  外观模式主要作用:解耦,否则层层依赖
 * 由于Facade的作用,客户可以根本不知道子系统类的存在,只需要对Facade对象进行操作即可
 */
public class Facade {

    private Bed bed;
    private Clothing clothing;
    private Tooth tooth;

    public Facade() {
        bed = new Bed();
        clothing = new Clothing();
        tooth = new Tooth();
    }

    public void morning() {
        bed.getUp();
        clothing.getDressed();
        tooth.brashTooth();
    }

    public void evening() {
        tooth.brashTooth();
        clothing.undress();
        bed.goToBed();
    }

}
