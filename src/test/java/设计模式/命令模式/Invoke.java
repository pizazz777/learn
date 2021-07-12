package 设计模式.命令模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description 调用者
 */
public class Invoke {

    private Command command;

    public Invoke(Command command) {
        this.command = command;
    }

    public <T> void invoke(T obj) {
        command.execute(obj);
    }

}
