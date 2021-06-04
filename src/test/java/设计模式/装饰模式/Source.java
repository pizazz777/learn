package 设计模式.装饰模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class Source implements Sourceable {

    @Override
    public void method() {
        System.out.println("需要装饰的类");
    }

}
