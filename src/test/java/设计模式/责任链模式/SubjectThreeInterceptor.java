package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/02
 * @description 科三
 */
public class SubjectThreeInterceptor extends AbstractCommandInterceptor {

    /**
     * 执行任务
     *
     * @param command 要执行的任务
     * @return {@link T}
     */
    @Override
    public <T> T execute(Command command) {
        System.out.println("这是科三");
        if (command instanceof SubjectThreeCommand) {
            System.out.println("当前正在考科目三!");
        }
        return next.execute(command);
    }

    public static class SubjectThreeCommand implements Command<String> {

        @Override
        public String execute(String message) {
            System.out.println("科三考试是路面考试! 有加减档,变道,灯光调节");
            return message;
        }
    }

}
