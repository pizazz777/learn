package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public class SubjectFourCommand implements Command<String> {

    @Override
    public String execute(String message) {
        System.out.println("科四考试是理论考试!  当前考试人为: " + message);
        return message;
    }
}
