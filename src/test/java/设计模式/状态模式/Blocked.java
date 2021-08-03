package 设计模式.状态模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/03
 * @description 锁定状态
 */
public class Blocked extends ThreadState {

    public Blocked() {
        stateName = "阻塞状态";
        System.out.println("当前线程处于=======>" + stateName);
    }

    /**
     * 恢复状态
     *
     * @param context 上下文
     */
    public void resume(ThreadContext context) {
        System.out.println("调用resume()方法=======>");
        if (stateName.equals("阻塞状态")) {
            // 重新变为就绪状态
            context.setState(new Runnable());
        } else {
            System.out.println("当前线程不是阻塞状态,不能调用resume()方法.");
        }
    }

}
