package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public class SubjectThreeCommand implements Command<String> {

    @Override
    public String execute(String message) {
        System.out.println("科三考试是路面考试! 有加减档,变道,灯光调节");
        return message;
    }

}
