package 设计模式.单例模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class Singleton {

    /**
     * 私有构造方法
     */
    private Singleton() {
    }

    /**
     * 使用内部类维护单例
     */
    private static class SingletonFactory {
        private static Singleton instance = new Singleton();
    }

    /**
     * 提供一个获取的公共方法 注意要是static修饰
     *
     * @return object
     */
    public static Singleton getInstance() {
        return SingletonFactory.instance;
    }

    /**
     * 如果该对象被用于序列化，可以保证对象在序列化前后保持一致
     *
     * @return object
     */
    public Object readResolve() {
        return getInstance();
    }

}
