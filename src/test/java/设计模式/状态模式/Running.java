package 设计模式.状态模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/03
 * @description 运行状态
 */
public class Running extends ThreadState {

    public Running() {
        stateName = "运行状态";
        System.out.println("当前线程处于=======>" + stateName);
    }

    /**
     * 暂停
     *
     * @param context 上下文
     */
    public void suspend(ThreadContext context) {
        System.out.println("调用suspend()方法=======>");
        if (stateName.equals("运行状态")) {
            context.setState(new Blocked());
        } else {
            System.out.println("当前线程不是运行状态,不能调用suspend()方法.");
        }
    }

    /**
     * 结束线程
     *
     * @param context 上下文
     */
    public void stop(ThreadContext context) {
        System.out.println("调用stop()方法=======>");
        if (stateName.equals("运行状态")) {
            context.setState(new Dead());
        } else {
            System.out.println("当前线程不是运行状态,不能调用stop()方法.");
        }
    }

}
