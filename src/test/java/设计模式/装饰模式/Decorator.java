package 设计模式.装饰模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class Decorator implements Sourceable {

    private Source source;

    /**
     * 装饰器模式关注于在一个对象上动态的添加方法
     * 使用装饰器模式的时候,我们通常的做法是将原始对象作为一个参数传给装饰者的构造器
     *
     * @param source 原始对象
     */
    public Decorator(Source source) {
        this.source = source;
    }

    @Override
    public void method() {
        System.out.println("装饰之前的动作");
        source.method();
        System.out.println("装饰之后的动作");
    }


}
