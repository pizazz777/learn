package 设计模式.责任链模式;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/03
 * @description
 */

public class ConfigurationImpl extends ExecutorConfigurationImpl {


    public ConfigurationImpl() {
        super.init();
    }


    @Override
    public List<CommandInterceptor> createOtherInterceptor() {
        return Lists.newArrayList(new SubjectFiveInterceptor());
    }



}
