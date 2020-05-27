package com.example.demo.listener;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.util.SpringContextHandler;
import com.example.demo.util.container.ContainerUtil;
import com.google.common.collect.Lists;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.example.demo.constant.workflow.ProcessConst.CANDIDATE_USER_KEY;

/**
 * @author Administrator
 * @date 2020-05-06 17:28
 * @description: 设置所有任务创建时的监听
 */
@Component
public class WorkflowListener implements TaskListener {

    private static final long serialVersionUID = 5861106839797196597L;

    private AuthComponent authComponent;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notify(DelegateTask delegateTask) {
        System.out.println("执行了TaskListener的任务监听!");
        if (authComponent == null) {
            authComponent = SpringContextHandler.getBean(AuthComponent.class);
        }
        Map<String, Object> variablesMap = delegateTask.getVariables();
        @SuppressWarnings("unchecked")
        List<String> candidateList = (List<String>) variablesMap.get(CANDIDATE_USER_KEY);
        if (ContainerUtil.isEmpty(candidateList)) {
            try {
                candidateList = Lists.newArrayList();
                candidateList.add(String.valueOf(authComponent.getPrimaryPrincipal(Long.class)));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        delegateTask.setVariable(CANDIDATE_USER_KEY, candidateList);
    }

}
