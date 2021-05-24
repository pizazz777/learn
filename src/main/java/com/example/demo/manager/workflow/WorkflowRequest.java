package com.example.demo.manager.workflow;

import com.example.demo.entity.activiti.ActivityInfoDTO;
import com.example.demo.entity.activiti.CheckHistoryDTO;
import com.example.demo.entity.activiti.CompleteDTO;
import com.huang.exception.ServiceException;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0.0
 * @date 2020-05-07 15:47
 * @description: 工作流通用逻辑处理类
 */
public interface WorkflowRequest {


    /**
     * 通过压缩文件 部署
     *
     * @param name 部署名称
     * @param file 压缩文件
     * @return Deployment
     * @throws ServiceException e
     */
    Deployment deployment(String name, MultipartFile file) throws ServiceException;


    /**
     * 通过压缩文件流 部署
     *
     * @param name        部署名称
     * @param inputStream 压缩文件流
     * @return Deployment
     * @throws ServiceException e
     */
    Deployment deployment(String name, InputStream inputStream) throws ServiceException;


    /**
     * 通过文件路径 部署
     *
     * @param name      部署名称
     * @param classPath 类路径
     * @return Deployment
     * @throws ServiceException e
     */
    Deployment deployment(String name, String classPath) throws ServiceException;


    /**
     * 通过bpmnModel模型 部署
     *
     * @param name      部署名称
     * @param bpmnModel 流程模型
     * @return Deployment
     * @throws ServiceException e
     */
    Deployment deployment(String name, BpmnModel bpmnModel) throws ServiceException;


    /**
     * 根据部署id查询部署对象
     *
     * @param deploymentId 部署id
     * @return Deployment
     * @throws ServiceException e
     */
    Deployment getDeploymentByDeploymentId(String deploymentId) throws ServiceException;


    /**
     * 获取全部的部署对象
     *
     * @return list
     * @throws ServiceException e
     */
    List<Deployment> listAllDeployments() throws ServiceException;


    /**
     * 获取所有的最新的流程定义
     *
     * @return list
     * @throws ServiceException e
     */
    List<ProcessDefinition> listNewestProcessDefinitionList() throws ServiceException;


    /**
     * 启动流程
     *
     * @param processKey 流程的key
     * @return 流程实例
     * @throws ServiceException e
     */
    ProcessInstance start(String processKey) throws ServiceException;


    /**
     * 启动流程
     *
     * @param processKey  流程的key
     * @param businessKey 业务key
     * @return 流程实例
     * @throws ServiceException e
     */
    ProcessInstance start(String processKey, String businessKey) throws ServiceException;


    /**
     * 启动流程
     *
     * @param processKey 流程的key
     * @param variables  流程变量
     * @return 流程实例
     * @throws ServiceException e
     */
    ProcessInstance start(String processKey, Map<String, Object> variables) throws ServiceException;


    /**
     * 启动流程，以类名为id进行启动
     *
     * @param clz 类
     * @return 流程实例
     * @throws ServiceException e
     */
    ProcessInstance start(Class clz) throws ServiceException;


    /**
     * 启动流程，以类名为id进行启动
     *
     * @param clz       类
     * @param variables 流程变量
     * @return 流程实例
     * @throws ServiceException e
     */
    ProcessInstance start(Class clz, Map<String, Object> variables) throws ServiceException;


    /**
     * 根据部署id来启动流程
     *
     * @param deploymentId 部署id
     * @return 流程实例
     * @throws ServiceException e
     */
    ProcessInstance startByDeploymentId(String deploymentId) throws ServiceException;


    /**
     * 根据流程定义启动
     *
     * @param processDefinitionId 流程定义id
     * @return 流程实例
     * @throws ServiceException e
     */
    ProcessInstance startByProcessDefinition(String processDefinitionId) throws ServiceException;


    /**
     * 激活部署流程
     *
     * @param processDefinitionId     流程定义id
     * @param suspendProcessInstances 是否级联激活
     * @param suspensionDateTime      激活的时间,null立即激活
     * @throws ServiceException e
     */
    void activateProcess(String processDefinitionId, boolean suspendProcessInstances, LocalDateTime suspensionDateTime) throws ServiceException;


    /**
     * 挂起部署流程
     *
     * @param processDefinitionId     流程定义id
     * @param suspendProcessInstances 是否级联挂起
     * @param suspensionDateTime      挂起的时间,null立即挂起
     * @throws ServiceException e
     */
    void suspendProcess(String processDefinitionId, boolean suspendProcessInstances, LocalDateTime suspensionDateTime) throws ServiceException;


    /**
     * 获取主流程的所有层级子流程
     *
     * @param processInstanceId 主流程实例ID
     * @return list
     * @throws ServiceException e
     */
    List<ProcessInstance> listChildByProcessInstanceId(String processInstanceId) throws ServiceException;


    /**
     * 获取当前任务,并行网关是多个任务,一般只有一个
     *
     * @param processInstanceId 流程实例id
     * @return task
     * @throws ServiceException e
     */
    List<Task> listCurrentTask(String processInstanceId) throws ServiceException;


    /**
     * 获取单个任务
     *
     * @param processInstanceId 流程实例id
     * @return task
     * @throws ServiceException e
     */
    Task getCurrentTaskSingleResult(String processInstanceId) throws ServiceException;


    /**
     * 获取该用户能够办理的任务的流程实例id集合
     *
     * @param userId 用户id
     * @return list
     * @throws ServiceException e
     */
    List<String> listCandidateUserTaskProcessInstanceId(String userId) throws ServiceException;


    /**
     * 得到当前任务的办理人id集合
     *
     * @param taskId 任务id
     * @return list
     * @throws ServiceException e
     */
    List<String> listCurrentTaskCandidateUser(String taskId) throws ServiceException;


    /**
     * 获取当前任务的前一级任务的代办人(这个只适合与没有并行网关)
     *
     * @param processInstanceId 流程实例
     * @return list
     * @throws ServiceException e
     */
    List<String> listPreTaskCandidateUserByProcessInstanceId(String processInstanceId) throws ServiceException;


    /**
     * 获取当前任务的前一级任务的代办人
     *
     * @param taskId 当前任务id
     * @return list
     * @throws ServiceException e
     */
    List<String> listPreTaskCandidateUserByTaskId(String taskId) throws ServiceException;


    /**
     * 完成任务
     *
     * @param completeDTO 传输对象
     * @throws ServiceException e
     */
    void complete(CompleteDTO completeDTO) throws ServiceException;


    /**
     * 完成任务
     *
     * @param currentUserId     当前待办人
     * @param taskId            任务id
     * @param processInstanceId 流程实例
     * @param checkComment      批注
     * @param branchValue       具体走那条分支
     * @param pass              通过还是回退
     * @param userList          下级任务的待办人,如果是回退则可以不传
     * @throws ServiceException e
     */
    void complete(String currentUserId, String taskId, String processInstanceId, String checkComment, Integer branchValue, Integer pass, List<String> userList) throws ServiceException;


    /**
     * 完成任务
     *
     * @param currentCompleteUserId 当前完成人
     * @param taskId                任务id
     * @param processInstanceId     流程实例
     * @param checkComment          完成批准
     * @param variables             流程变量
     * @throws ServiceException e
     */
    void complete(String currentCompleteUserId, String taskId, String processInstanceId, String checkComment, Map<String, Object> variables) throws ServiceException;


    /**
     * 添加代办人
     *
     * @param taskId   任务id
     * @param userList 用户id
     * @throws ServiceException e
     */
    void addCandidateUser(String taskId, List<String> userList) throws ServiceException;


    /**
     * 移除代办人
     *
     * @param taskId   任务id
     * @param userList 用户id
     * @throws ServiceException e
     */
    void deleteCandidateUser(String taskId, List<String> userList) throws ServiceException;


    /**
     * 根据流程实例获取流程定义id
     *
     * @param processInstanceId 流程实例
     * @return processDefinitionId
     * @throws ServiceException e
     */
    String getProcessDefinitionIdByProcessInstanceId(String processInstanceId) throws ServiceException;


    /**
     * 根据部署id获取流程定义
     *
     * @param deploymentId 部署id
     * @return ProcessDefinition
     * @throws ServiceException e
     */
    ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId) throws ServiceException;


    /**
     * 根据定义id,查询流程定义
     *
     * @param id 定义id
     * @return ProcessDefinition
     * @throws ServiceException e
     */
    ProcessDefinition getProcessDefinitionById(String id) throws ServiceException;


    /**
     * 获取流程实例对象
     *
     * @param processInstanceId 流程实例
     * @return ProcessInstance
     * @throws ServiceException e
     */
    ProcessInstance getProcessInstance(String processInstanceId) throws ServiceException;


    /**
     * 获取所有的最新的流程定义
     *
     * @return list
     * @throws ServiceException e
     */
    List<ProcessDefinition> getProcessDefinitionList() throws ServiceException;


    /**
     * 获取历史流程实例
     *
     * @param processInstanceId 流程实例
     * @return HistoricProcessInstance
     * @throws ServiceException e
     */
    HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) throws ServiceException;


    /**
     * 获取所有的历史活动对象
     *
     * @param processInstanceId 流程实例
     * @return List<HistoricActivityInstance>
     * @throws ServiceException e
     */
    List<HistoricActivityInstance> listActiveHistoricActivityInstance(String processInstanceId) throws ServiceException;


    /**
     * 获取所有流程已完成的实例
     *
     * @param processInstanceId 流程实例
     * @return List<HistoricActivityInstance>
     * @throws ServiceException e
     */
    List<HistoricActivityInstance> listFinishHistoricActivityInstance(String processInstanceId) throws ServiceException;

    /**
     * 获取流程模型
     *
     * @param processDefinitionId 流程定义id
     * @return BpmnModel
     * @throws ServiceException e
     */
    BpmnModel getBpmnModel(String processDefinitionId) throws ServiceException;


    /**
     * 根据流程实例获取模型
     *
     * @param processInstanceId 流程实例
     * @return BpmnModel
     * @throws ServiceException e
     */
    BpmnModel getBpmnModelByProcessInstanceId(String processInstanceId) throws ServiceException;


    /**
     * 获取流程的执行对象
     *
     * @param executionId 执行id
     * @return Execution
     * @throws ServiceException e
     */
    Execution getExecution(String executionId) throws ServiceException;


    /**
     * 根据流程定义,查询该流程正在执行的执行对象
     *
     * @param processDefinitionId 流程定义id
     * @return list
     * @throws ServiceException e
     */
    List<Execution> listExecutionByProcessDefinitionId(String processDefinitionId) throws ServiceException;


    /**
     * 根据流程实例判断该流程是否已结束
     *
     * @param processInstanceId 流程实例
     * @return boolean
     * @throws ServiceException e
     */
    boolean isEnd(String processInstanceId) throws ServiceException;


    /**
     * 是否是开始节点下一级的节点
     *
     * @param processInstanceId 流程实例
     * @return boolean
     * @throws ServiceException e
     */
    boolean isNextEventOfTheStartEvent(String processInstanceId) throws ServiceException;


    /**
     * 查找当前任务的上一步流程的任务节点
     *
     * @param processInstanceId 流程实例
     * @return FlowElement
     * @throws ServiceException e
     */
    List<FlowElement> listPreActEvent(String processInstanceId) throws ServiceException;


    /**
     * 流程跳转
     *
     * @param processInstanceId 流程实例
     * @param flowElementId     跳转的节点id
     * @param reason            跳转原因
     * @throws ServiceException e
     */
    void jump(String processInstanceId, String flowElementId, String reason) throws ServiceException;


    /**
     * 根据流程实例ID获取已经完成的节点ID集合和进行中的节点ID集合
     *
     * @param processInstanceId 流程实例ID
     * @return a
     * @throws ServiceException e
     */
    ActivityInfoDTO getActivityInfoByProcessInstanceId(String processInstanceId) throws ServiceException;


    /**
     * 根据业务KEY流程实例ID集合
     *
     * @param businessKey 业务KEY
     * @return a
     * @throws ServiceException e
     */
    List<ProcessInstance> listProcessInstanceByBusinessKey(String businessKey) throws ServiceException;


    /**
     * 根据子流程实例ID获取主流程实例ID
     *
     * @param childProcessInstanceId 子流程实例ID
     * @return a
     * @throws ServiceException e
     */
    String getMainProcessInstanceIdByChildProcessInstanceId(String childProcessInstanceId) throws ServiceException;


    /**
     * 根据流程实例ID获取流程业务KEY
     *
     * @param processInstanceId 流程实例ID
     * @return a
     * @throws ServiceException e
     */
    String getBusinessKeyByProcessInstanceId(String processInstanceId) throws ServiceException;


    /**
     * 终止流程
     *
     * @param processInstanceId 流程实例ID
     * @param comment           批注
     * @throws ServiceException e
     */
    void stopProcess(String processInstanceId, String comment) throws ServiceException;


    /**
     * 查看流程历史记录
     *
     * @param processInstanceId 流程实例
     * @return ResResult
     * @throws ServiceException e
     */
    CheckHistoryDTO history(String processInstanceId) throws ServiceException;


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
    String addOrDeleteMultiInstance(List<String> taskIdList,
                                    Integer addOrDelete,
                                    List<String> assigneeList,
                                    String assignee,
                                    List<String> leaderConfirmUser) throws ServiceException;


    /**
     * 删除部署流程(报错的话可能还有其它的正在使用不能删除)
     *
     * @param deploymentId 部署id
     * @param cascade      是否级联删除
     * @throws ServiceException e
     */
    void deleteDeploymentProcess(String deploymentId, boolean cascade) throws ServiceException;
}
