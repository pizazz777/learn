package 设计模式.状态模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/03
 * @description 就绪状态
 */
public class Runnable extends ThreadState {

    public Runnable() {
        stateName = "就绪状态";
        System.out.println("当前线程处于=======>" + stateName);
    }

    public void getCPU(ThreadContext hj) {
        System.out.println("获得CPU时间=======>");
        if (stateName.equals("就绪状态")) {
            hj.setState(new Running());
        } else {
            System.out.println("当前线程不是就绪状态，不能获取CPU.");
        }
    }

}
