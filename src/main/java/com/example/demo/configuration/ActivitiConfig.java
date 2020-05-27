package com.example.demo.configuration;

import com.example.demo.manager.workflow.WorkflowRequest;
import com.example.demo.manager.workflow.impl.WorkflowRequestImpl;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @date 2020-05-07 13:55
 */
@Configuration
@ConditionalOnBean(WorkflowRequestImpl.class)
public class ActivitiConfig implements ApplicationRunner {

    private WorkflowRequest workflowRequest;

    @Autowired
    public ActivitiConfig(WorkflowRequest workflowRequest) {
        this.workflowRequest = workflowRequest;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        workflowRequest.deployment("选商项目", "processes/project-main.bpmn");
    }


}
