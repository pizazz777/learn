package com.example.demo.manager.workflow.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.entity.activiti.*;
import com.example.demo.jump.DeleteTaskCmd;
import com.example.demo.jump.SetFlowNodeAndGoCmd;
import com.example.demo.manager.workflow.WorkflowRequest;
import com.example.demo.util.container.ContainerUtil;
import com.example.demo.util.time.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static com.example.demo.constant.workflow.ProcessConst.*;

/**
 * @author Administrator
 * @version 1.0.0
 * @date 2020-05-07 15:47
 * @description: 工作流通用逻辑处理类
 */
@Service
public class WorkflowRequestImpl implements WorkflowRequest {

    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private HistoryService historyService;
    private TaskService taskService;
    private ManagementService managementService;
    private AuthComponent authComponent;

    @Autowired
    public WorkflowRequestImpl(RepositoryService repositoryService,
                               RuntimeService runtimeService,
                               HistoryService historyService,
                               TaskService taskService,
                               ManagementService managementService,
                               AuthComponent authComponent) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.taskService = taskService;
        this.managementService = managementService;
        this.authComponent = authComponent;
    }


    /**
     * 通过压缩文件 部署
     *
     * @param name 部署名称
     * @param file 压缩文件
     * @return Deployment
     * @throws ServiceException e
     */
    @Override
    public Deployment deployment(String name, MultipartFile file) throws ServiceException {
        Optional.ofNullable(file).orElseThrow(() -> new ServiceException(""));
        try (InputStream inputStream = file.getInputStream()) {
            return deployment(name, inputStream);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * 通过压缩文件流 部署
     *
     * @param name        部署名称
     * @param inputStream 压缩文件流
     * @return Deployment
     * @throws ServiceException e
     */
    @Override
    public Deployment deployment(String name, InputStream inputStream) throws ServiceException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        return repositoryService.createDeployment()
                .name(name)
                .addZipInputStream(zipInputStream)
                .deploy();
    }


    /**
     * 通过文件路径 部署
     *
     * @param name      部署名称
     * @param classPath 类路径
     * @return Deployment
     */
    @Override
    public Deployment deployment(String name, String classPath) throws ServiceException {
        return repositoryService.createDeployment()
                .name(name)
                .addClasspathResource(classPath)
                .deploy();
    }


    /**
     * 通过bpmnModel模型 部署
     *
     * @param name      部署名称
     * @param bpmnModel 流程模型
     * @return Deployment
     * @throws ServiceException e
     */
    @Override
    public Deployment deployment(String name, BpmnModel bpmnModel) throws ServiceException {
        return repositoryService.createDeployment()
                .name(name)
                .addBpmnModel(name + ".bpmn", bpmnModel)
                .deploy();
    }


    /**
     * 根据部署id查询部署对象
     *
     * @param deploymentId 部署id
     * @return Deployment
     */
    @Override
    public Deployment getDeploymentByDeploymentId(String deploymentId) {
        return repositoryService.createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult();
    }


    /**
     * 获取全部的部署对象
     *
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<Deployment> listAllDeployments() throws ServiceException {
        List<ProcessDefinition> processDefinitionList = this.listNewestProcessDefinitionList();
        List<Deployment> resultList = Lists.newArrayList();
        processDefinitionList.forEach(processDefinition -> resultList.add(getDeploymentByDeploymentId(processDefinition.getDeploymentId())));
        return resultList;
    }


    /**
     * 获取所有的最新的流程定义
     *
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<ProcessDefinition> listNewestProcessDefinitionList() throws ServiceException {
        return repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .asc()
                .list();
    }


    /**
     * 启动流程
     *
     * @param processKey 流程的key
     * @return 流程实例
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance start(String processKey) throws ServiceException {
        return runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 启动流程
     *
     * @param processKey  流程的key
     * @param businessKey 业务key
     * @return 流程实例
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance start(String processKey, String businessKey) throws ServiceException {
        return runtimeService.startProcessInstanceByKey(processKey, businessKey);
    }

    /**
     * 启动流程
     *
     * @param processKey 流程的key
     * @param variables  流程变量
     * @return 流程实例
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance start(String processKey, Map<String, Object> variables) throws ServiceException {
        return runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    /**
     * 启动流程，以类名为id进行启动
     *
     * @param clz 类
     * @return 流程实例
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance start(Class clz) throws ServiceException {
        return runtimeService.startProcessInstanceByKey(clz.getSimpleName());
    }

    /**
     * 启动流程，以类名为id进行启动
     *
     * @param clz       类
     * @param variables 流程变量
     * @return 流程实例
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance start(Class clz, Map<String, Object> variables) throws ServiceException {
        return runtimeService.startProcessInstanceByKey(clz.getSimpleName(), variables);
    }

    /**
     * 根据部署id来启动流程
     *
     * @param deploymentId 部署id
     * @return 流程实例
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance startByDeploymentId(String deploymentId) throws ServiceException {
        ProcessDefinition processDefinition = this.getProcessDefinitionByDeploymentId(deploymentId);
        return this.start(processDefinition.getKey());
    }

    /**
     * 根据流程定义启动
     *
     * @param processDefinitionId 流程定义id
     * @return 流程实例
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance startByProcessDefinition(String processDefinitionId) throws ServiceException {
        return runtimeService.startProcessInstanceById(processDefinitionId);
    }

    /**
     * 激活部署流程
     *
     * @param processDefinitionId     流程定义id
     * @param suspendProcessInstances 是否级联激活
     * @param suspensionDateTime      激活的时间,null立即激活
     * @throws ServiceException e
     */
    @Override
    public void activateProcess(String processDefinitionId, boolean suspendProcessInstances, LocalDateTime suspensionDateTime) throws ServiceException {
        repositoryService.activateProcessDefinitionById(processDefinitionId, suspendProcessInstances, Objects.nonNull(suspensionDateTime) ? DateUtil.localDateTimeToDate(suspensionDateTime) : null);
    }


    /**
     * 挂起部署流程
     *
     * @param processDefinitionId     流程定义id
     * @param suspendProcessInstances 是否级联挂起
     * @param suspensionDateTime      挂起的时间,null立即挂起
     * @throws ServiceException e
     */
    @Override
    public void suspendProcess(String processDefinitionId, boolean suspendProcessInstances, LocalDateTime suspensionDateTime) throws ServiceException {
        repositoryService.suspendProcessDefinitionById(processDefinitionId, suspendProcessInstances, Objects.nonNull(suspensionDateTime) ? DateUtil.localDateTimeToDate(suspensionDateTime) : null);
    }


    /**
     * 迭代获取主流程下的所有子流程
     *
     * @param processInstanceId 主流程实例ID
     * @return list
     */
    @Override
    public List<ProcessInstance> listChildByProcessInstanceId(String processInstanceId) {
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                .superProcessInstanceId(processInstanceId)
                .list();
        if (ContainerUtil.isNotEmpty(processInstanceList)) {
            // 递归调用
            processInstanceList.forEach(processInstance -> processInstanceList.addAll(this.listChildByProcessInstanceId(processInstance.getProcessInstanceId())));
        }
        return processInstanceList;
    }


    /**
     * 获取当前任务,并行网关/包含网关可能会有多个任务,一般只有一个
     *
     * @param processInstanceId 流程实例id
     * @return task
     * @throws ServiceException e
     */
    @Override
    public List<Task> listCurrentTask(String processInstanceId) throws ServiceException {
        // 所有子流程
        List<ProcessInstance> processInstanceList = listChildByProcessInstanceId(processInstanceId);
        List<String> processInstanceIdList = processInstanceList.stream().map(ProcessInstance::getProcessInstanceId).collect(Collectors.toList());
        // 当前流程id
        processInstanceIdList.add(processInstanceId);
        // 对应任务
        List<Task> resList = Lists.newArrayList();
        processInstanceIdList.forEach(instanceId -> {
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(instanceId)
                    .list();
            resList.addAll(taskList);
        });
        return resList;
    }


    /**
     * 获取单个任务
     *
     * @param processInstanceId 流程实例id
     * @return task
     * @throws ServiceException e
     */
    @Override
    public Task getCurrentTaskSingleResult(String processInstanceId) throws ServiceException {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }


    /**
     * 获取该用户能够办理的任务的流程实例id集合
     *
     * @param userId 用户id
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<String> listCandidateUserTaskProcessInstanceId(String userId) throws ServiceException {
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateOrAssigned(userId)
                .list();
        return taskList.stream().map(Task::getProcessInstanceId).collect(Collectors.toList());
    }


    /**
     * 得到当前任务的办理人id集合
     *
     * @param taskId 任务id
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<String> listCurrentTaskCandidateUser(String taskId) throws ServiceException {
        List<IdentityLink> list = taskService.getIdentityLinksForTask(taskId);
        return list.stream().map(IdentityLink::getUserId).collect(Collectors.toList());
    }


    /**
     * 获取当前任务的前一级任务的代办人(这个只适合与没有并行网关)
     *
     * @param processInstanceId 流程实例
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<String> listPreTaskCandidateUserByProcessInstanceId(String processInstanceId) throws ServiceException {
        return null;
    }


    /**
     * 获取当前任务的前一级任务的代办人
     *
     * @param taskId 当前任务id
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<String> listPreTaskCandidateUserByTaskId(String taskId) throws ServiceException {
        List<String> list = Lists.newArrayList();
        // 获取当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = this.getBpmnModel(taskDefinitionKey);
        Process process = bpmnModel.getMainProcess();
        String prevUserTaskDefKey = this.getPreUserTaskKey(taskDefinitionKey, process);
        if (Objects.nonNull(prevUserTaskDefKey)) {
            List<HistoricTaskInstance> hisPrevTaskList = listHistoryOfTheCurrentTask(prevUserTaskDefKey, task.getProcessInstanceId());
            for (HistoricTaskInstance his : hisPrevTaskList) {
                list.addAll(this.listHistoryUserOfTheTask(his.getId()));
            }
        } else {
            list.add(authComponent.getPrimaryPrincipal(Long.class).toString());
        }
        return list.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 获取流程模型
     *
     * @param processDefinitionId 流程定义id
     * @return BpmnModel
     * @throws ServiceException e
     */
    @Override
    public BpmnModel getBpmnModel(String processDefinitionId) throws ServiceException {
        return repositoryService.getBpmnModel(processDefinitionId);
    }


    /**
     * 完成任务
     *
     * @param completeDTO 传输对象
     * @throws ServiceException e
     */
    @Override
    public void complete(CompleteDTO completeDTO) throws ServiceException {
        //参数校验
        parameterChecking(completeDTO);
        //传递变量
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(BRANCH_KEY, completeDTO.getBranch());
        variables.put(CHECK_KEY, completeDTO.getCheck());
        // 如果是不通过并且没有设置待办人,返回上级流程,将上级流程的代办人查出来,设置成当前任务完成后的代办人
        if (Objects.equals(completeDTO.getCheck(), CHECK_ROLLBACK) && ContainerUtil.isEmpty(completeDTO.getCandidateUserList())) {
            variables.put(CANDIDATE_USER_KEY, this.listPreTaskCandidateUserByProcessInstanceId(completeDTO.getProcessInstanceId()));
        } else {
            variables.put(CANDIDATE_USER_KEY, completeDTO.getCandidateUserList());
        }
        taskService.claim(completeDTO.getTaskId(), completeDTO.getAssignee());
        this.addComment(completeDTO.getTaskId(), completeDTO.getProcessInstanceId(), completeDTO.getCheck(), completeDTO.getComment());
        taskService.complete(completeDTO.getTaskId(), variables);
    }


    /**
     * 完成任务
     *
     * @param principal         当前待办人
     * @param taskId            任务id
     * @param processInstanceId 流程实例
     * @param comment           批注
     * @param branch            具体走那条分支
     * @param check             通过还是回退
     * @param candidateUserList 下级任务的待办人,如果是回退则可以不传
     * @throws ServiceException e
     */
    @Override
    public void complete(String principal, String taskId, String processInstanceId, String comment, Integer branch, Integer check, List<String> candidateUserList) throws ServiceException {
        if (StringUtils.isBlank(taskId)) {
            Task task = this.getCurrentTaskSingleResult(processInstanceId);
            taskId = task.getId();
        }
        CompleteDTO completeDTO = CompleteDTO.builder()
                .assignee(principal)
                .comment(comment)
                .taskId(taskId)
                .branch(branch)
                .candidateUserList(candidateUserList)
                .check(check)
                .processInstanceId(processInstanceId)
                .build();
        this.complete(completeDTO);
    }


    /**
     * 完成任务
     *
     * @param principal         当前完成人
     * @param taskId            任务id
     * @param processInstanceId 流程实例
     * @param comment           完成批准
     * @param paramsMap         流程变量
     * @throws ServiceException e
     */
    @Override
    public void complete(String principal, String taskId, String processInstanceId, String comment, Map<String, Object> paramsMap) throws ServiceException {
        taskService.claim(taskId, principal);
        int check = ContainerUtil.isNotEmpty(paramsMap) && Objects.nonNull(paramsMap.get(CHECK_KEY)) ? Integer.valueOf(String.valueOf(paramsMap.get(CHECK_KEY))) : CHECK_PASS;
        this.addComment(taskId, processInstanceId, check, comment);
        taskService.complete(taskId, paramsMap);
    }


    /**
     * 添加代办人
     *
     * @param taskId   任务id
     * @param userList 用户id
     * @throws ServiceException e
     */
    @Override
    public void addCandidateUser(String taskId, List<String> userList) throws ServiceException {
        userList.forEach(userId -> taskService.addCandidateUser(taskId, userId));
    }


    /**
     * 移除代办人
     *
     * @param taskId   任务id
     * @param userList 用户id
     * @throws ServiceException e
     */
    @Override
    public void deleteCandidateUser(String taskId, List<String> userList) throws ServiceException {
        userList.forEach(userId -> taskService.deleteCandidateUser(taskId, userId));
    }


    /**
     * 根据流程实例获取流程定义id
     *
     * @param processInstanceId 流程实例
     * @return processDefinitionId
     * @throws ServiceException e
     */
    @Override
    public String getProcessDefinitionIdByProcessInstanceId(String processInstanceId) throws ServiceException {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        return list.get(0).getProcessDefinitionId();
    }


    /**
     * 根据部署id获取流程定义
     *
     * @param deploymentId 部署id
     * @return ProcessDefinition
     * @throws ServiceException e
     */
    @Override
    public ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId) throws ServiceException {
        return repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
    }


    /**
     * 根据定义id,查询流程定义
     *
     * @param id 定义id
     * @return ProcessDefinition
     * @throws ServiceException e
     */
    @Override
    public ProcessDefinition getProcessDefinitionById(String id) throws ServiceException {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(id)
                .singleResult();
    }


    /**
     * 获取流程实例对象
     *
     * @param processInstanceId 流程实例
     * @return ProcessInstance
     * @throws ServiceException e
     */
    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) throws ServiceException {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }


    /**
     * 获取所有的最新的流程定义
     *
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<ProcessDefinition> getProcessDefinitionList() throws ServiceException {
        return repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .list()
                .stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(
                                        Comparator.comparing(ProcessDefinition::getKey)
                                )), Lists::newArrayList
                        )
                );
    }


    /**
     * 获取历史流程实例
     *
     * @param processInstanceId 流程实例
     * @return HistoricProcessInstance
     * @throws ServiceException e
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) throws ServiceException {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }


    /**
     * 获取所有的历史活动对象
     *
     * @param processInstanceId 流程实例
     * @return List<HistoricActivityInstance>
     * @throws ServiceException e
     */
    @Override
    public List<HistoricActivityInstance> listActiveHistoricActivityInstance(String processInstanceId) throws ServiceException {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
    }


    /**
     * 获取所有流程已完成的实例
     *
     * @param processInstanceId 流程实例
     * @return List<HistoricActivityInstance>
     * @throws ServiceException e
     */
    @Override
    public List<HistoricActivityInstance> listFinishHistoricActivityInstance(String processInstanceId) throws ServiceException {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
    }


    /**
     * 根据流程实例获取模型
     *
     * @param processInstanceId 流程实例
     * @return BpmnModel
     * @throws ServiceException e
     */
    @Override
    public BpmnModel getBpmnModelByProcessInstanceId(String processInstanceId) throws ServiceException {
        return repositoryService.getBpmnModel(this.getProcessDefinitionIdByProcessInstanceId(processInstanceId));
    }


    /**
     * 获取流程的执行对象
     *
     * @param executionId 执行id
     * @return Execution
     * @throws ServiceException e
     */
    @Override
    public Execution getExecution(String executionId) throws ServiceException {
        return runtimeService.createExecutionQuery()
                .executionId(executionId)
                .singleResult();
    }


    /**
     * 根据流程定义,查询该流程正在执行的执行对象
     *
     * @param processDefinitionId 流程定义id
     * @return list
     * @throws ServiceException e
     */
    @Override
    public List<Execution> listExecutionByProcessDefinitionId(String processDefinitionId) throws ServiceException {
        return runtimeService.createExecutionQuery()
                .processDefinitionId(processDefinitionId)
                .list();
    }


    /**
     * 根据流程实例判断该流程是否已结束
     *
     * @param processInstanceId 流程实例
     * @return boolean
     * @throws ServiceException e
     */
    @Override
    public boolean isEnd(String processInstanceId) throws ServiceException {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        return Objects.isNull(processInstance);
    }


    /**
     * 是否是开始节点下一级的节点
     *
     * @param processInstanceId 流程实例
     * @return boolean
     * @throws ServiceException e
     */
    @Override
    public boolean isNextEventOfTheStartEvent(String processInstanceId) throws ServiceException {
        Task task = this.getCurrentTaskSingleResult(processInstanceId);
        Collection<FlowElement> flowElements = this.listFlowElement(processInstanceId);
        StartEvent startEvent = this.getStartEvent(flowElements);
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                assert startEvent != null;
                if (Objects.equals(startEvent.getId(), sequenceFlow.getSourceRef()) && Objects.equals(task.getTaskDefinitionKey(), sequenceFlow.getTargetRef())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 查找当前任务的上一步流程的任务节点
     *
     * @param processInstanceId 流程实例
     * @return FlowElement
     * @throws ServiceException e
     */
    @Override
    public List<FlowElement> listPreActEvent(String processInstanceId) throws ServiceException {
        Task task = this.getCurrentTaskSingleResult(processInstanceId);
        Collection<FlowElement> flowElements = this.listFlowElement(processInstanceId);
        Process process = this.getProcess(processInstanceId);
        List<FlowElement> flowElementList = Lists.newArrayList();
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                if (Objects.equals(task.getTaskDefinitionKey(), sequenceFlow.getTargetRef())) {
                    FlowElement element = process.getFlowElement(sequenceFlow.getSourceRef());
                    flowElementList.add(element);
                }
            }
        }
        return flowElementList;
    }


    /**
     * 流程跳转
     *
     * @param processInstanceId 流程实例
     * @param flowElementId     跳转的节点id
     * @param deleteReason      跳转原因
     * @throws ServiceException e
     */
    @Override
    public void jump(String processInstanceId, String flowElementId, String deleteReason) throws ServiceException {
        //获取流程定义
        Process process = this.getBpmnModelByProcessInstanceId(processInstanceId).getMainProcess();
        //获取目标节点定义
        FlowNode targetNode = (FlowNode) process.getFlowElement(flowElementId);
        List<Task> currentTaskList = this.listCurrentTask(processInstanceId);
        // 所有的任务都是相连接的，顺便一个都可以
        String executionEntityId = null;
        for (Task task : currentTaskList) {
            //删除当前运行任务
            executionEntityId = managementService.executeCommand(new DeleteTaskCmd(task.getId(), deleteReason));
        }
        if (StringUtils.isNotBlank(executionEntityId)) {
            //流程执行到来源节点
            managementService.executeCommand(new SetFlowNodeAndGoCmd(targetNode, executionEntityId));
        }
    }


    /**
     * 根据流程实例ID获取已经完成的节点ID集合和进行中的节点ID集合
     *
     * @param processInstanceId 流程实例ID
     * @return a
     * @throws ServiceException e
     */
    @Override
    public ActivityInfoDTO getActivityInfoByProcessInstanceId(String processInstanceId) throws ServiceException {
        ProcessInstance processInstance = this.getProcessInstance(processInstanceId);
        List<Task> currentTaskList = Lists.newArrayList();
        if (Objects.nonNull(processInstance)) {
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
            currentTaskList = this.listCurrentTask(processInstanceId);
        }
        // 获取正在进行中的节点ID和名称
        List<String> underwayFlowElementIdList = Lists.newArrayList();
        List<String> underwayFlowElementNameList = Lists.newArrayList();
        List<LocalDateTime> startTimeList = Lists.newArrayList();
        if (ContainerUtil.isNotEmpty(currentTaskList)) {
            for (Task task : currentTaskList) {
                ExecutionEntity execution = (ExecutionEntity) this.getExecution(task.getExecutionId());
                underwayFlowElementIdList.add(execution.getActivityId());
                underwayFlowElementNameList.add(task.getName());
                startTimeList.add(DateUtil.dateToLocalDateTime(execution.getStartTime()));
            }
        }
        // 获取已经完成的节点ID
        return ActivityInfoDTO.builder()
                .underwayFlowElementIdList(underwayFlowElementIdList.stream().distinct().collect(Collectors.toList()))
                .underwayFlowElementNameList(underwayFlowElementNameList)
                .startTimeList(startTimeList)
                .build();
    }


    /**
     * 获取已经完成的节点ID
     */
    private List<String> generateProcessDiagram(String processInstanceId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取历史流程实例
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        //获取历史流程定义
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)
                processEngine.getRepositoryService()).getDeployedProcessDefinition(historicProcessInstance
                .getProcessDefinitionId());
        //查询历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processDefinitionEntity.getId());
        //已执行flow的ID集和
        return executedFlowIdList(bpmnModel, historicActivityInstanceList);
    }

    /**
     * 获取已执行flow的ID集和
     */
    private List<String> executedFlowIdList(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> executedFlowIdList = Lists.newArrayList();
        for (int i = 0; i < historicActivityInstanceList.size() - 1; i++) {
            HistoricActivityInstance hai = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
            List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
            if (sequenceFlows.size() > 1) {
                HistoricActivityInstance nextHai = historicActivityInstanceList.get(i + 1);
                sequenceFlows.forEach(sequenceFlow -> {
                    if (sequenceFlow.getTargetFlowElement().getId().equals(nextHai.getActivityId())) {
                        if (Objects.nonNull(sequenceFlow.getName())) {
                            executedFlowIdList.add(sequenceFlow.getName());
                        }
                    }
                });
            } else {
                if (Objects.nonNull(sequenceFlows.get(0).getName())) {
                    executedFlowIdList.add(sequenceFlows.get(0).getName());
                }
            }
        }
        return executedFlowIdList;
    }


    /**
     * 根据业务KEY流程实例ID集合
     *
     * @param businessKey 业务KEY
     * @return a
     * @throws ServiceException e
     */
    @Override
    public List<ProcessInstance> listProcessInstanceByBusinessKey(String businessKey) throws ServiceException {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).list().stream().distinct().collect(Collectors.toList());
    }


    /**
     * 根据子流程实例ID获取主流程实例ID
     *
     * @param childProcessInstanceId 子流程实例ID
     * @return a
     * @throws ServiceException e
     */
    @Override
    public String getMainProcessInstanceIdByChildProcessInstanceId(String childProcessInstanceId) throws ServiceException {
        ProcessInstance processInstance = this.getProcessInstance(childProcessInstanceId);
        return processInstance.getRootProcessInstanceId();
    }


    /**
     * 根据流程实例ID获取流程业务KEY
     *
     * @param processInstanceId 流程实例ID
     * @return a
     * @throws ServiceException e
     */
    @Override
    public String getBusinessKeyByProcessInstanceId(String processInstanceId) throws ServiceException {
        ProcessInstance processInstance = this.getProcessInstance(processInstanceId);
        return processInstance.getBusinessKey();
    }


    /**
     * 终止流程
     *
     * @param processInstanceId 流程实例ID
     * @param comment           批注
     * @throws ServiceException e
     */
    @Override
    public void stopProcess(String processInstanceId, String comment) throws ServiceException {
        runtimeService.deleteProcessInstance(processInstanceId, comment);
    }


    /**
     * 查看流程历史记录
     *
     * @param processInstanceId 流程实例
     * @return ResResult
     * @throws ServiceException e
     */
    @Override
    public CheckHistoryDTO history(String processInstanceId) throws ServiceException {
        List<ActHistoryDTO> actHistoryDtoList = treeByHistory(processInstanceId);
        actHistoryDtoList = actHistoryDtoList.stream()
                .sorted(Comparator.comparing(ActHistoryDTO::getAssigneeTime).reversed())
                .collect(Collectors.toList());
        return CheckHistoryDTO.builder()
                .actHistoryDtoList(actHistoryDtoList)
                .build();
    }

    /**
     * 获取历史记录树结构
     */
    private List<ActHistoryDTO> treeByHistory(String processInstanceId) throws ServiceException {
        List<ActHistoryDTO> resResult = Lists.newArrayList();
        //获取流程历史记录
        List<HistoryDO> historyList = this.listHistoryActByProcessInstanceId(processInstanceId);
        //根据活动节点分组
        Map<String, List<HistoryDO>> actHistoryMap = historyList.stream().collect(Collectors.groupingBy(HistoryDO::getActId));
        for (Map.Entry<String, List<HistoryDO>> entry : actHistoryMap.entrySet()) {
            List<HistoryDO> actHistoryList = entry.getValue();
            List<ActHistoryDTO.Assignee> list = Lists.newArrayList();
            for (HistoryDO history : actHistoryList) {
                if (StringUtils.isNotBlank(history.getChildProcessInstanceId())) {
                    resResult.addAll(treeByHistory(history.getChildProcessInstanceId()));
                } else {
                    List<Comment> commentList = history.getCommentList();
                    if (ContainerUtil.isEmpty(commentList)) {
                        String check = "";
                        String commentMessage = "";
                        for (Comment comment : commentList) {
                            if (Objects.equals(comment.getType(), COMMENT_CHECK)) {
                                check = comment.getFullMessage();
                            }
                            if (Objects.equals(comment.getType(), CommentEntity.TYPE_COMMENT)) {
                                commentMessage = comment.getFullMessage();
                            }
                        }
                        ActHistoryDTO.Assignee assignee = ActHistoryDTO.Assignee.builder()
                                .assigneeId(history.getAssignee())
                                .assigneeName("")
                                .assigneeCorpName("")
                                .assigneeTime(history.getEndTime())
                                .comment(commentMessage)
                                .check(check)
                                .build();
                        list.add(assignee);
                    }
                }
            }
            list.sort(Comparator.comparing(ActHistoryDTO.Assignee::getAssigneeTime));
            ActHistoryDTO actHistoryDTO = ActHistoryDTO.builder()
                    .actId(entry.getKey())
                    .actName(actHistoryList.get(0).getActName())
                    .assigneeList(list)
                    .assigneeTime(list.get(0).getAssigneeTime())
                    .build();
            resResult.add(actHistoryDTO);
        }
        return resResult;
    }


    /**
     * 获取历史活动
     */
    private List<HistoryDO> listHistoryActByProcessInstanceId(String processInstanceId) throws ServiceException {
        List<HistoricActivityInstance> list = this.listActiveHistoricActivityInstance(processInstanceId);
        return list.stream()
                .filter(e -> Objects.nonNull(e.getTaskId()))
                .map(history -> {
                    List<Comment> commentList = taskService.getTaskComments(history.getTaskId());
                    return HistoryDO.builder()
                            .processInstanceId(history.getProcessInstanceId())
                            .taskId(history.getTaskId())
                            .actId(history.getActivityId())
                            .actName(history.getActivityName())
                            .actType(history.getActivityType())
                            .assignee(history.getAssignee())
                            .commentList(commentList)
                            .endTime(DateUtil.dateToLocalDateTime(history.getEndTime()))
                            .startTime(DateUtil.dateToLocalDateTime(history.getStartTime()))
                            .childProcessInstanceId(history.getCalledProcessInstanceId())
                            .build();
                }).collect(Collectors.toList());
    }


    /**
     * 会签的加减签
     *
     * @param taskIdList        当前任务ID
     * @param addOrDelete       加签还是减签 1:加签 2:减签
     * @param assigneeList      审核人
     * @param assignee          任务执行人
     * @param leaderConfirmUser 处级领导确认人
     * @return str
     * @throws ServiceException e
     */
    @Override
    public String addOrDeleteMultiInstance(List<String> taskIdList, Integer addOrDelete, List<String> assigneeList, String assignee, List<String> leaderConfirmUser) throws ServiceException {
        return null;
    }


    /**
     * 删除部署流程(报错的话可能还有其它的正在使用不能删除)
     *
     * @param deploymentId 部署id
     * @param cascade      是否级联删除
     * @throws ServiceException e
     */
    @Override
    public void deleteDeploymentProcess(String deploymentId, boolean cascade) throws ServiceException {
        repositoryService.deleteDeployment(deploymentId, cascade);
    }


    private List<String> listHistoryUserOfTheTask(String taskId) {
        List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(taskId);
        return linksForTask.stream().map(HistoricIdentityLink::getUserId).collect(Collectors.toList());
    }


    /**
     * 获取当前任务节点的历史任务
     */
    private List<HistoricTaskInstance> listHistoryOfTheCurrentTask(String actId, String processInstanceId) {
        List<HistoricTaskInstance> hisTaskList = Lists.newArrayList();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityId(actId)
                .list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            String taskId = historicActivityInstance.getTaskId();
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                    .taskId(taskId)
                    .singleResult();
            hisTaskList.add(historicTaskInstance);
        }
        return hisTaskList;
    }


    /**
     * 获取上级的用户任务的key
     *
     * @param currentTaskId 当前任务的id
     * @param process       流程模型中的主要流程对象<process></process>
     * @return 上级用户任务的key
     * @throws ServiceException e
     */
    private String getPreUserTaskKey(String currentTaskId, Process process) throws ServiceException {
        Collection<FlowElement> flowElements = process.getFlowElements();
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                if (Objects.equals(sequenceFlow.getTargetRef(), currentTaskId)) {
                    String sourceRef = sequenceFlow.getSourceRef();
                    FlowElement element = process.getFlowElement(sourceRef);
                    if (element instanceof ParallelGateway) {
                        throw new ServiceException("不支持回退到并行任务之前!");
                    } else if (element instanceof UserTask) {
                        return element.getId();
                    } else {
                        if (element instanceof StartEvent) {
                            return null;
                        } else {
                            getPreUserTaskKey(element.getId(), process);
                        }
                    }
                }
            }
        }
        return null;
    }


    private StartEvent getStartEvent(String processInstanceId) throws ServiceException {
        Collection<FlowElement> flowElements = this.listFlowElement(processInstanceId);
        return getStartEvent(flowElements);
    }

    private StartEvent getStartEvent(Collection<FlowElement> flowElements) throws ServiceException {
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof StartEvent) {
                return (StartEvent) flowElement;
            }
        }
        return null;
    }

    private Collection<FlowElement> listFlowElement(String processInstanceId) throws ServiceException {
        Process process = this.getProcess(processInstanceId);
        return process.getFlowElements();
    }


    private Process getProcess(String processInstanceId) throws ServiceException {
        BpmnModel bpmnModel = this.getBpmnModelByProcessInstanceId(processInstanceId);
        return bpmnModel.getMainProcess();
    }


    private void addComment(String taskId, String processInstanceId, Integer check, String comment) {
        if (StringUtils.isNotBlank(comment)) {
            // 添加意见批注
            taskService.addComment(taskId, processInstanceId, comment);
        }
        if (Objects.nonNull(check)) {
            // 添加审核批注
            taskService.addComment(taskId, processInstanceId, COMMENT_CHECK, String.valueOf(check));
        }
    }


    /**
     * 完成任务的参数校验
     */
    private void parameterChecking(CompleteDTO completeDTO) throws ServiceException {
        if (Objects.isNull(completeDTO.getTaskId())) {
            throw new ServiceException("完成任务id不能为空!");
        }
        if (StringUtils.isBlank(completeDTO.getAssignee())) {
            throw new ServiceException("完成任务办理人不能为空!");
        }
        if (StringUtils.isNotBlank(completeDTO.getComment()) && Objects.isNull(completeDTO.getProcessInstanceId())) {
            throw new ServiceException("添加批注,流程实例id不能为空!");
        }
        if (Objects.isNull(completeDTO.getBranch())) {
            completeDTO.setBranch(DEFAULT_BRANCH_VALUE);
        }
        if (Objects.isNull(completeDTO.getCheck())) {
            completeDTO.setCheck(CHECK_PASS);
        }
    }

}
