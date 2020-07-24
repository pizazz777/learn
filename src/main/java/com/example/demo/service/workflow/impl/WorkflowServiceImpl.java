package com.example.demo.service.workflow.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.activiti.CheckHistoryDTO;
import com.example.demo.manager.workflow.WorkflowRequest;
import com.example.demo.service.workflow.WorkflowService;
import com.example.demo.util.container.ContainerUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.constant.workflow.ProcessConst.*;

/**
 * @author Administrator
 * @date 2020-05-07 17:17
 * @description: 工作流处理类
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

    private AuthComponent authComponent;
    private WorkflowRequest workflowRequest;

    @Autowired
    public WorkflowServiceImpl(AuthComponent authComponent, WorkflowRequest workflowRequest) {
        this.authComponent = authComponent;
        this.workflowRequest = workflowRequest;
    }

    /**
     * 部署资源文件压缩包
     *
     * @param name    流程名称
     * @param zipFile 流程压缩包
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public ResResult deploymentAct(String name, MultipartFile zipFile) throws ServiceException {
        Deployment deployment = workflowRequest.deployment(name, zipFile);
        return ResResult.success(deployment);
    }

    /**
     * 挂起部署流程
     *
     * @param deploymentId  部署id
     * @param localDateTime 挂起时间
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public ResResult suspendDeployment(String deploymentId, LocalDateTime localDateTime) throws ServiceException {
        ProcessDefinition processDefinition = workflowRequest.getProcessDefinitionByDeploymentId(deploymentId);
        String processDefinitionId = processDefinition.getId();
        // 查找当前流程有没有执行的对象
        List<Execution> executionList = workflowRequest.listExecutionByProcessDefinitionId(processDefinitionId);
        if (ContainerUtil.isNotEmpty(executionList)) {
            return ResResult.fail(ResCode.FAILED, "该流程正在使用中,不能挂起!");
        }
        workflowRequest.suspendProcess(processDefinitionId, true, localDateTime);
        return ResResult.success();
    }

    /**
     * 激活流程
     *
     * @param deploymentId       部署id
     * @param activationDateTime 激活时间
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public ResResult activationDeploy(String deploymentId, LocalDateTime activationDateTime) throws ServiceException {
        ProcessDefinition processDefinition = workflowRequest.getProcessDefinitionByDeploymentId(deploymentId);
        workflowRequest.activateProcess(processDefinition.getId(), true, activationDateTime);
        return ResResult.success();
    }


    /**
     * 启动部署流程
     *
     * @param deploymentId 部署id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult start(String deploymentId) throws ServiceException {
        Map<String, Object> paramsMap = Maps.newHashMap();
        String principal = String.valueOf(authComponent.getPrimaryPrincipal(Long.class));
        paramsMap.put("assignee", principal);
        ProcessInstance processInstance = workflowRequest.start(deploymentId, paramsMap);
        return ResResult.success(processInstance);
    }


    /**
     * 删除部署流程
     *
     * @param deploymentId 部署id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult deleteDeploymentProcess(String deploymentId) throws ServiceException {
        workflowRequest.deleteDeploymentProcess(deploymentId, true);
        return ResResult.success();
    }


    /**
     * 获取当前节点任务
     *
     * @param processInstanceId 流程实例id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult listCurrentTask(String processInstanceId) throws ServiceException {
        List<Task> taskList = workflowRequest.listCurrentTask(processInstanceId);
        return ContainerUtil.isNotEmpty(taskList) ? ResResult.success(taskList) : ResResult.fail(ResCode.NOT_FOUND);
    }


    /**
     * 查看流程历史记录
     *
     * @param processInstanceId 流程实例
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public ResResult history(String processInstanceId) throws ServiceException {
        CheckHistoryDTO history = workflowRequest.history(processInstanceId);
        return ResResult.success(history);
    }

    /**
     * 获取该流程的流程图的列表树级结构
     *
     * @param processInstanceId 流程实例
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public ResResult treeByDeployment(String processInstanceId) throws ServiceException {
        return null;
    }

    /**
     * 添加代办人
     *
     * @param taskId     任务id
     * @param userIdList 用户id
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public ResResult addCandidateUser(String taskId, List<Long> userIdList) throws ServiceException {
        workflowRequest.addCandidateUser(taskId, userIdList.stream().map(String::valueOf).collect(Collectors.toList()));
        return ResResult.success();
    }

    /**
     * 删除代办人
     *
     * @param taskId     任务id
     * @param userIdList 用户id
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public ResResult deleteCandidateUser(String taskId, List<Long> userIdList) throws ServiceException {
        workflowRequest.deleteCandidateUser(taskId, userIdList.stream().map(String::valueOf).collect(Collectors.toList()));
        return ResResult.success();
    }


    /**
     * 完成开始项目
     *
     * @param type    类型
     * @param comment 意见
     * @return r
     * @throws ServiceException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult completeProject(String processInstanceId, Integer type, String comment) throws ServiceException {
        Long principal = authComponent.getPrimaryPrincipal(Long.class);
        Task task = workflowRequest.getCurrentTaskSingleResult(processInstanceId);
        List<Long> candidateUserList = Lists.newArrayList(3L, 4L);
        Map<String, Object> paramsMap = getVariables(null, candidateUserList, null, type, 1L);
        workflowRequest.complete("2", task.getId(), processInstanceId, comment, paramsMap);
        return ResResult.success();
    }


    /**
     * 完成任务
     *
     * @param processInstanceId 流程实例id
     * @param taskDefinitionKey 任务定义key-会签任务完成指定任务时传
     * @param check             审核
     * @param comment           意见
     * @return r
     * @throws ServiceException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult complete(String processInstanceId, String taskDefinitionKey, Integer check, String comment) throws ServiceException {
        List<Task> taskList = workflowRequest.listCurrentTask(processInstanceId);
        if (ContainerUtil.isEmpty(taskList)) {
            String principal = String.valueOf(authComponent.getPrimaryPrincipal(Long.class));
            String taskId = null;
            if (taskList.size() > 1) {
                if (StringUtils.isNotBlank(taskDefinitionKey)) {
                    // 筛选指定任务
                    taskList = taskList.stream().filter(e -> Objects.equals(taskDefinitionKey, e.getTaskDefinitionKey())).collect(Collectors.toList());
                }
                for (Task task : taskList) {
                    List<String> candidateUserIdList = workflowRequest.listCurrentTaskCandidateUser(task.getId());
                    if (candidateUserIdList.contains(principal)) {
                        taskId = task.getId();
                        break;
                    }
                }
            } else {
                Task task = taskList.get(0);
                taskId = task.getId();
            }
            if (StringUtils.isNotBlank(taskId)) {
                completeTask(principal, taskId, processInstanceId, check, comment);
                return ResResult.success();
            }
        }
        return ResResult.fail("没有找到任务!");
    }


    /**
     * 完成任务
     *
     * @param userId            任务完成人
     * @param taskId            任务id
     * @param processInstanceId 流程实例id
     * @param check             审核
     * @param comment           意见
     * @throws ServiceException e
     */
    private void completeTask(String userId, String taskId, String processInstanceId, Integer check, String comment) throws ServiceException {
        // 设置下一个任务的代办人
        List<Long> candidateUserList = Lists.newArrayList(1L, 2L);
        // 设置参数
        Map<String, Object> paramsMap = getVariables(null, candidateUserList, check, null, 1L);
        // 完成任务
        workflowRequest.complete(userId, taskId, processInstanceId, comment, paramsMap);
    }


    /**
     * 获取流程变量
     *
     * @param branch            分支
     * @param candidateUserList 待办用户
     * @param check             审核
     * @param type              类型
     * @param businessKey       业务的key(使用项目ID作为业务的key）
     * @return r
     * @throws ServiceException e
     */
    private Map<String, Object> getVariables(Integer branch, List<Long> candidateUserList, Integer check, Integer type, Long businessKey) throws ServiceException {
        return getVariables(branch, candidateUserList, check, type, businessKey, null);
    }


    /**
     * 获取流程变量
     *
     * @param branch            分支
     * @param candidateUserList 待办用户
     * @param check             审核
     * @param type              类型
     * @param checkType         审查的类型 1：同时审批文件和方式 2：只审批方式
     * @return r
     */
    private Map<String, Object> getVariables(Integer branch,
                                             List<Long> candidateUserList,
                                             Integer check,
                                             Integer type,
                                             Long businessKey,
                                             Integer checkType) {
        Map<String, Object> map = Maps.newHashMap();
        if (Objects.isNull(branch)) {
            branch = DEFAULT_BRANCH_VALUE;
        }
        map.put(BRANCH_KEY, branch);
        if (ContainerUtil.isNotEmpty(candidateUserList)) {
            List<String> stringList = candidateUserList.stream().map(Objects::toString).collect(Collectors.toList());
            map.put(CANDIDATE_USER_KEY, stringList);
        }
        if (Objects.isNull(check)) {
            check = CHECK_PASS;
        }
        map.put(CHECK_KEY, check);
        map.put(BUSINESS_KEY, businessKey);
        map.put(CHECK_TYPE_KEY, checkType);
        if (Objects.nonNull(type)) {
            map.put(TYPE_KEY, type);
        }
        return map;
    }

}
