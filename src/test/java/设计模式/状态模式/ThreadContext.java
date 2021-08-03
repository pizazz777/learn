package 设计模式.状态模式;

import lombok.Data;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/03
 * @description
 */
@Data
public class ThreadContext {

    private ThreadState state;

    ThreadContext() {
        state = new New();
    }

    public void start() {
        ((New) state).start(this);
    }

    public void getCPU() {
        ((Runnable) state).getCPU(this);
    }

    public void suspend() {
        ((Running) state).suspend(this);
    }

    public void stop() {
        ((Running) state).stop(this);
    }

    public void resume() {
        ((Blocked) state).resume(this);
    }

}
