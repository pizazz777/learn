package 设计模式.适配器模式.类的适配器;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class Adapter extends Source implements Sourceable {


    @Override
    public void drink() {
        System.out.println("喝水!");
    }


}
