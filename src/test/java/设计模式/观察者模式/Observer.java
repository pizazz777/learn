package 设计模式.观察者模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description 观察者接口
 */
public interface Observer {

    /**
     * 通知
     *
     * @param title 消息标题
     */
    void read(String title);

}
