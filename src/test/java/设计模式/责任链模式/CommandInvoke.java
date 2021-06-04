package 设计模式.责任链模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description   这个模式是仿照activiti的设计搬过来的
 */
public class CommandInvoke {

    public static void main(String[] args) {
        ConfigurationImpl example = new ConfigurationImpl();

        ServiceImpl service = new SubjectFourServiceImpl();
        service.setCommandExecutor(example.commandExecutor);

        Command<String> command = new SubjectOneCommand();
        Object example1 = service.example(command);
        command.execute("盲僧");
    }

}
