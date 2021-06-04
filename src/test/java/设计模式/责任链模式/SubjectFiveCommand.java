package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public class SubjectFiveCommand implements Command<String> {

    @Override
    public String execute(String message) {
        System.out.println("科五考试在科一之前! ");
        return message;
    }
}
