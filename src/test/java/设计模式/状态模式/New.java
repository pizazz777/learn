package 设计模式.状态模式;


/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/03
 * @description 新建状态
 */
public class New extends ThreadState {

    public New() {
        stateName = "新建状态";
        System.out.println("当前线程处于=======>" + stateName);
    }

    public void start(ThreadContext context) {
        System.out.println("调用start()方法======>");
        if (stateName.equals("新建状态")) {
            context.setState(new Runnable());
        } else {
            System.out.println("当前线程不是新建状态，不能调用start()方法.");
        }
    }

}
