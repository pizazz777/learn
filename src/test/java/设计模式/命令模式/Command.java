package 设计模式.命令模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description
 */
public interface Command {


    /**
     * 执行
     *
     * @param obj obj
     */
    <T> void execute(T obj);

}
