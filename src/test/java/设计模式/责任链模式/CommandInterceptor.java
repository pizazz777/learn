package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/02
 * @description
 */
public interface CommandInterceptor {

    /**
     * 执行任务
     *
     * @param command 要执行的任务
     * @return {@link T}
     */
    <T> T execute(Command command);


    CommandInterceptor getNext();


    void setNext(CommandInterceptor next);

}
