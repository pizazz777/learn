package 设计模式.责任链模式;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */
public abstract class ExecutorConfigurationImpl {

    protected List<CommandInterceptor> commandInterceptors;

    protected CommandExecutor commandExecutor;

    public void init() {
        initCommandInterceptors();
        initCommandExecutor();
    }

    public void initCommandInterceptors() {
        if (commandInterceptors == null) {
            commandInterceptors = Lists.newArrayList();
            // commandInterceptors.addAll(customPreCommandInterceptors);
            commandInterceptors.addAll(getDefaultCommandInterceptors());
            // commandInterceptors.addAll(customPostCommandInterceptors());
            commandInterceptors.add(new SubjectFourInterceptor());
        }
    }

    public void initCommandExecutor() {
        if (commandExecutor == null) {
            CommandInterceptor first = initInterceptorChain(commandInterceptors);
            commandExecutor = new CommandExecutorImpl(first);
        }
    }

    private CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new RuntimeException("invalid command interceptor chain configuration: " + chain);
        }
        for (int i = 0; i < chain.size() - 1; i++) {
            chain.get(i).setNext(chain.get(i + 1));
        }
        return chain.get(0);
    }

    private List<CommandInterceptor> getDefaultCommandInterceptors() {
        List<CommandInterceptor> interceptors = Lists.newArrayList();
        List<CommandInterceptor> commandInterceptorList = createOtherInterceptor();
        if (commandInterceptorList != null) {
            interceptors.addAll(commandInterceptorList);
        }
        interceptors.add(new SubjectOneInterceptor());
        interceptors.add(new SubjectTwoInterceptor());
        interceptors.add(new SubjectThreeInterceptor());
        return interceptors;
    }

    public abstract List<CommandInterceptor> createOtherInterceptor();

}
