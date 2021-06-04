package 设计模式.代理模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 *
 */
public class Proxy implements Subject {

    private Subject subject;

    /**
     * 代理模式关注于控制对对象的访问
     * 用代理模式,代理类proxy可以对它的客户隐藏一个对象的具体信息.因此,当使用代理模式的时候,我们常常在代理类中创建一个对象的实例
     */
    public Proxy() {
        this.subject = new RealSubject();
    }

    @Override
    public void doAction() {
        System.out.println("代理前动作");
        subject.doAction();
        System.out.println("代理后动作");
    }
}
