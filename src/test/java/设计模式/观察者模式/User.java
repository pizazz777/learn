package 设计模式.观察者模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class User implements Observer {

    private String name;

    public User(String name) {
        this.name = name;
    }

    /**
     * 通知
     *
     * @param title 消息标题
     */
    @Override
    public void read(String title) {
        System.out.println(this.name + "收到了订阅消息通知: " + title);
    }

}
