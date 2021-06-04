package 设计模式.代理模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class RealSubject implements Subject {

    @Override
    public void doAction() {
        System.out.println("被代理的类");
    }

}
