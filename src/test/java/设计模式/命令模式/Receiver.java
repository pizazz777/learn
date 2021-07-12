package 设计模式.命令模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description 被调用者对象
 */
public class Receiver {

    /**
     * 执行任务
     *
     * @param msg 消息
     */
    public <T> void action(T msg) {
        System.out.println("执行任务: " + msg);
    }

}
