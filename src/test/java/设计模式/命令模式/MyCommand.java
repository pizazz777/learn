package 设计模式.命令模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description 命令者
 */
public class MyCommand implements Command {

    private Receiver receiver;

    public MyCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * 执行
     */
    @Override
    public <T> void execute(T obj) {
        receiver.action(obj);
    }

}
