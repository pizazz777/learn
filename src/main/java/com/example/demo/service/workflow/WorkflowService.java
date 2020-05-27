package com.example.demo.service.workflow;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-05-07 17:16
 */
public interface WorkflowService {

    /**
     * 部署资源文件压缩包
     *
     * @param name    流程名称
     * @param zipFile 流程压缩包
     * @return ResResult
     * @throws ServiceException e
     */
    ResResult deploymentAct(String name, MultipartFile zipFile) throws ServiceException;


    /**
     * 挂起部署流程
     *
     * @param deploymentId  部署id
     * @param localDateTime 挂起时间
     * @return ResResult
     * @throws ServiceException e
     */
    ResResult suspendDeployment(String deploymentId, LocalDateTime localDateTime) throws ServiceException;


    /**
     * 激活流程
     *
     * @param deploymentId       部署id
     * @param activationDateTime 激活时间
     * @return ResResult
     * @throws ServiceException e
     */
    ResResult activationDeploy(String deploymentId, LocalDateTime activationDateTime) throws ServiceException;


    /**
     * 启动部署流程
     *
     * @param deploymentId 部署id
     * @return r
     * @throws ServiceException e
     */
    ResResult start(String deploymentId) throws ServiceException;


    /**
     * 删除部署流程
     *
     * @param deploymentId 部署id
     * @return r
     * @throws ServiceException e
     */
    ResResult deleteDeploymentProcess(String deploymentId) throws ServiceException;


    /**
     * 获取当前节点任务
     *
     * @param processInstanceId 流程实例id
     * @return r
     * @throws ServiceException e
     */
    ResResult listCurrentTask(String processInstanceId) throws ServiceException;


    /**
     * 查看流程历史记录
     *
     * @param processInstanceId 流程实例
     * @return ResResult
     * @throws ServiceException e
     */
    ResResult history(String processInstanceId) throws ServiceException;


    /**
     * 获取该流程的流程图的列表树级结构
     *
     * @param processInstanceId 流程实例
     * @return ResResult
     * @throws ServiceException e
     */
    ResResult treeByDeployment(String processInstanceId) throws ServiceException;


    /**
     * 添加代办人
     *
     * @param taskId     任务id
     * @param userIdList 用户id
     * @return ResResult
     * @throws ServiceException e
     */
    ResResult addCandidateUser(String taskId, List<Long> userIdList) throws ServiceException;


    /**
     * 删除代办人
     *
     * @param taskId     任务id
     * @param userIdList 用户id
     * @return ResResult
     * @throws ServiceException e
     */
    ResResult deleteCandidateUser(String taskId, List<Long> userIdList) throws ServiceException;


    /**
     * 完成开始项目
     *
     * @param processInstanceId 流程id
     * @param type              类型
     * @param comment           意见
     * @return r
     * @throws ServiceException e
     */
    ResResult completeProject(String processInstanceId, Integer type, String comment) throws ServiceException;


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
    ResResult complete(String processInstanceId, String taskDefinitionKey, Integer check, String comment) throws ServiceException;

}
