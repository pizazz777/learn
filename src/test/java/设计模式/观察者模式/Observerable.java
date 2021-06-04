package 设计模式.观察者模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public interface Observerable {

    /**
     * 注册观察者
     *
     * @param observer 观察者
     */
    void registerObserver(Observer observer);

    /**
     * 取消观察者
     *
     * @param observer 观察者
     */
    void removeObserver(Observer observer);

    /**
     * 通知观察者
     *
     * @param message 通知消息
     */
    void notifyObserver(String message);

}
