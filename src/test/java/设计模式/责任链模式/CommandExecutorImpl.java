package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public class CommandExecutorImpl implements CommandExecutor {

    protected CommandInterceptor first;

    public CommandExecutorImpl(CommandInterceptor first) {
        this.first = first;
    }

    public CommandInterceptor getFirst() {
        return first;
    }

    public void setFirst(CommandInterceptor first) {
        this.first = first;
    }

    @Override
    public <T> T execute(Command<T> command) {
        return first.execute(command);
    }

}
