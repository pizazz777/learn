package 设计模式.状态模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/03
 * @description 死亡状态
 */
public class Dead extends ThreadState {

    public Dead() {
        stateName = "死亡状态";
        System.out.println("当前线程处于=======>" + stateName);
    }

}
