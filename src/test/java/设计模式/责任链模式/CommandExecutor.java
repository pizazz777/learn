package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public interface CommandExecutor {

    <T> T execute(Command<T> command);

}
