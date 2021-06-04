package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/02
 * @description
 */
public interface Command<T> {

    T execute(String message);

}
