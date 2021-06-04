package 设计模式.观察者模式;

import java.util.Vector;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description 微信公众号 可以注册或取消会员 发布订阅消息时关注的会收到通知
 */
public class WeChatServer implements Observerable {

    private Vector<Observer> vector;

    public WeChatServer() {
        this.vector = new Vector<>();
    }

    public void publish(String message) {
        System.out.println("微信公众号发布订阅文章: " + message);
    }

    /**
     * 注册观察者
     *
     * @param observer 观察者
     */
    @Override
    public void registerObserver(Observer observer) {
        vector.add(observer);
    }

    /**
     * 取消观察者
     *
     * @param observer 观察者
     */
    @Override
    public void removeObserver(Observer observer) {
        if (!vector.isEmpty()) {
            vector.remove(observer);
        }
    }

    /**
     * 通知观察者
     *
     * @param title 消息标题
     */
    @Override
    public void notifyObserver(String title) {
        vector.forEach(observer -> observer.read(title));
    }

}
