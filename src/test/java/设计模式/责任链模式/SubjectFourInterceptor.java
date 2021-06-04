package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/02
 * @description 科四
 */
public class SubjectFourInterceptor extends AbstractCommandInterceptor {

    /**
     * 执行任务
     *
     * @param command 要执行的任务
     * @return {@link T}
     */
    @Override
    public <T> T execute(Command command) {
        System.out.println("这是科四");
        if (command instanceof SubjectFourCommand) {
            System.out.println("当前正在考科目四!");
        }
        return null;
    }


    @Override
    public void setNext(CommandInterceptor next) {
        throw new RuntimeException("CommandInvoker must be the last interceptor in the chain / 科四必须在最后!");
    }
}
