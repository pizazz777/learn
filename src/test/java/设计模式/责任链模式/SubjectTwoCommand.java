package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public class SubjectTwoCommand implements Command<String> {

    @Override
    public String execute(String message) {
        System.out.println("科二考试是实践考试! 有倒车入库,侧边停车,半坡起步");
        return message;
    }

}