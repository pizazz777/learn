package 设计模式.观察者模式;

import org.junit.Test;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/01
 * @description
 */
public class ObserverTest {

    @Test
    public void test() {
        User user1 = new User("盖伦");
        User user2 = new User("赵信");
        User user3 = new User("拉克丝");

        WeChatServer weChatServer = new WeChatServer();

        // 关注
        weChatServer.registerObserver(user1);
        weChatServer.registerObserver(user2);
        weChatServer.registerObserver(user3);
        // 发布文章
        weChatServer.publish("德玛西亚是一个实力雄厚、奉公守法的国家，有着功勋卓著的光荣军史。");
        // 通知关注的人
        weChatServer.notifyObserver("德玛西亚");


        // 取消关注
        weChatServer.removeObserver(user2);
        // 发布文章
        weChatServer.publish("诺克萨斯是一个威名震天的强大帝国。");
        // 通知关注的人
        weChatServer.notifyObserver("诺克萨斯");
    }

}
