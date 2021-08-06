package 设计模式.适配器模式.对象适配器;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class Adapter implements Sourceable {


    private Source source;

    public Adapter(Source source) {
        this.source = source;
    }

    @Override
    public void eat() {
        source.eat();
    }

    @Override
    public void drink() {
        System.out.println("喝水!");
    }


}
