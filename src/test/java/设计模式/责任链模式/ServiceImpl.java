package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public abstract class ServiceImpl<T> implements SubjectService<T> {

    protected CommandExecutor commandExecutor;

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    /**
     * 考试
     *
     * @param command 执行的任务
     * @return t
     */
    @Override
    public abstract T example(Command<T> command);
}
