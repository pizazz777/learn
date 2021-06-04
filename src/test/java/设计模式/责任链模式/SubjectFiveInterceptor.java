package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/02
 * @description 科五  用来测试随机新增
 */
public class SubjectFiveInterceptor extends AbstractCommandInterceptor {

    /**
     * 执行任务
     *
     * @param command 要执行的任务
     * @return {@link T}
     */
    @Override
    public <T> T execute(Command command) {
        System.out.println("这是科五");
        if (command instanceof SubjectFiveCommand) {
            System.out.println("当前正在考科目五!");
        }
        return next.execute(command);
    }

}
