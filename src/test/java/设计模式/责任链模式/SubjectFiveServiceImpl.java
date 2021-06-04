package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public class SubjectFiveServiceImpl<T> extends ServiceImpl<T> implements SubjectService<T> {


    /**
     * 考试
     *
     * @param command 执行的任务
     * @return t
     */
    @Override
    public T example(Command<T> command) {
        return commandExecutor.execute(command);
    }


}
