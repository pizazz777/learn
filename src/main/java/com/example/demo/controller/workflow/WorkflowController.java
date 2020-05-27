package com.example.demo.controller.workflow;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.service.workflow.WorkflowService;
import com.example.demo.util.container.ContainerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-05-07 17:16
 */
@Api(tags = "调用工作流模块")
@RequestMapping("/workflow")
@RestController
public class WorkflowController {

    private WorkflowService workflowService;

    @Autowired
    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }


    private static final String DEPLOYMENT_ZIP_FILE = "上传流程压缩包进行部署";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = DEPLOYMENT_ZIP_FILE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "流程部署的文件,一般用.zip压缩文件", required = true, dataType = "file")
    })
    @PostMapping(value = "/deploy")
//    @Action(type = ActionLogEnum.SAVE, desc = DEPLOYMENT_ZIP_FILE)
    public String deploymentFile(@RequestParam("name") String name,
                                 @RequestParam("file") MultipartFile file) throws ServiceException {
        ResResult result = workflowService.deploymentAct(name, file);
        return result.getStr(DEPLOYMENT_ZIP_FILE);
    }


    private static final String SUSPEND_DEPLOYMENT = "挂起部署流程";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = SUSPEND_DEPLOYMENT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "suspensionDateTime", value = "挂起时间", dataType = "string")
    })
    @PostMapping(value = "/suspend")
//    @Action(type = ActionLogEnum.DELETE, desc = DEL_DEPLOYMENT)
    public String suspendDeployment(@RequestParam("deploymentId") String deploymentId,
                                    @RequestParam(value = "suspensionDateTime", required = false) LocalDateTime suspensionDateTime) throws ServiceException {
        ResResult result = workflowService.suspendDeployment(deploymentId, suspensionDateTime);
        return result.getStr(SUSPEND_DEPLOYMENT);
    }


    private static final String ACTIVATION_DEPLOYMENT = "激活部署流程";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = ACTIVATION_DEPLOYMENT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "activationDateTime", value = "激活时间", dataType = "string")
    })
    @PostMapping(value = "/activation")
//    @Action(type = ActionLogEnum.UPDATE, desc = ACTIVATION_DEPLOYMENT)
    public String activationDeploy(@RequestParam("deploymentId") String deploymentId,
                                   @RequestParam(value = "activationDateTime", required = false) LocalDateTime activationDateTime) throws ServiceException {
        ResResult result = workflowService.activationDeploy(deploymentId, activationDateTime);
        return result.getStr(ACTIVATION_DEPLOYMENT);
    }


    private static final String START_DEPLOYMENT = "启动部署流程";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = START_DEPLOYMENT)
    @ApiImplicitParam(name = "deploymentId", value = "部署id", required = true, dataType = "string")
    @PostMapping(value = "/start")
//    @Action(type = ActionLogEnum.UPDATE, desc = ACTIVATION_DEPLOYMENT)
    public String start(@RequestParam("deploymentId") String deploymentId) throws ServiceException {
        ResResult result = workflowService.start(deploymentId);
        return result.getStr(START_DEPLOYMENT);
    }


    private static final String COMPLETE_PROJECT = "完成开始项目";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = COMPLETE_PROJECT)
    @PostMapping(value = "/complete_project")
//    @Action(type = ActionLogEnum.UPDATE, desc = ACTIVATION_DEPLOYMENT)
    public String completeProject(@RequestParam("processInstanceId") String processInstanceId,
                                  @RequestParam("type") Integer type,
                                  @RequestParam("comment") String comment) throws ServiceException {
        ResResult result = workflowService.completeProject(processInstanceId, type, comment);
        return result.getStr(COMPLETE_PROJECT);
    }


    private static final String COMPLETE = "完成任务";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = COMPLETE)
    @PostMapping(value = "/complete")
//    @Action(type = ActionLogEnum.UPDATE, desc = ACTIVATION_DEPLOYMENT)
    public String complete(@RequestParam("processInstanceId") String processInstanceId,
                           @RequestParam("taskDefinitionKey") String taskDefinitionKey,
                           @RequestParam("check") Integer check,
                           @RequestParam("comment") String comment) throws ServiceException {
        ResResult result = workflowService.complete(processInstanceId, taskDefinitionKey, check, comment);
        return result.getStr(COMPLETE);
    }


    private static final String DELETE_DEPLOYMENT = "删除部署流程";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = DELETE_DEPLOYMENT)
    @ApiImplicitParam(name = "deploymentId", value = "部署id", required = true, dataType = "string")
    @PostMapping(value = "/delete_deployment")
//    @Action(type = ActionLogEnum.DELETE, desc = DEL_DEPLOYMENT)
    public String deleteDeploymentProcess(@RequestParam("deploymentId") String deploymentId) throws ServiceException {
        ResResult result = workflowService.deleteDeploymentProcess(deploymentId);
        return result.getStr(DELETE_DEPLOYMENT);
    }


    private static final String LIST_HISTORY_ID = "查看流程历史记录";

    @ApiOperation(value = LIST_HISTORY_ID)
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例", required = true, dataType = "string")
    @GetMapping(value = "/list_history_id")
//    @Action(type = ActionLogEnum.LIST, desc = LIST_HISTORY_ID)
    public String history(@RequestParam("processInstanceId") String processInstanceId) throws ServiceException {
        ResResult result = workflowService.history(processInstanceId);
        return result.getStr(LIST_HISTORY_ID);
    }


    private static final String LIST_CURRENT_TASK = "查看当前任务节点";

    @ApiOperation(value = LIST_CURRENT_TASK)
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例", required = true, dataType = "string")
    @GetMapping(value = "/list_current_task")
//    @Action(type = ActionLogEnum.LIST, desc = LIST_HISTORY_ID)
    public String listCurrentTask(@RequestParam("processInstanceId") String processInstanceId) throws ServiceException {
        ResResult result = workflowService.listCurrentTask(processInstanceId);
        return result.getStr(LIST_CURRENT_TASK);
    }


    private static final String LIST_TREE_DEPLOYMENT = "获取部署的流程树结构";

    @ApiOperation(value = LIST_TREE_DEPLOYMENT)
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例", required = true, dataType = "string")
    @GetMapping(value = "/list_tree_deployment")
//    @Action(type = ActionLogEnum.LIST, desc = LIST_TREE_DEPLOYMENT)
    public String treeByDeployment(@RequestParam("processInstanceId") String processInstanceId) throws ServiceException {
        ResResult result = workflowService.treeByDeployment(processInstanceId);
        return result.getStr(LIST_TREE_DEPLOYMENT);
    }


    private static final String ADD_CANDIDATE_USER = "添加代办人";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = ADD_CANDIDATE_USER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userIdList", value = "用户id集合", required = true, dataType = "long")
    })
    @PostMapping(value = "/add_candidate_user")
//    @Action(type = ActionLogEnum.SAVE, desc = ADD_CANDIDATE_USER)
    public String addCandidateUser(String taskId, List<Long> userIdList) throws ServiceException {
        String legalCandidateUserParam = this.legalCandidateUserParam(taskId, userIdList);
        if (Objects.nonNull(legalCandidateUserParam)) {
            return ResResult.fail(legalCandidateUserParam).getStr(ADD_CANDIDATE_USER);
        }
        ResResult result = workflowService.addCandidateUser(taskId, userIdList);
        return result.getStr(ADD_CANDIDATE_USER);
    }


    private static final String DELETE_CANDIDATE_USER = "删除代办人";

    //    @RequiresPermissions(PermissionConst.WORKFLOW_DEPLOYMENT)
    @ApiOperation(value = DELETE_CANDIDATE_USER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userIdList", value = "用户id集合", required = true, dataType = "long")
    })
    @PostMapping(value = "/delete_candidate_user")
    public String deleteCandidateUser(String taskId, List<Long> userIdList) throws ServiceException {
        String legalCandidateUserParam = this.legalCandidateUserParam(taskId, userIdList);
        if (Objects.nonNull(legalCandidateUserParam)) {
            return ResResult.fail(legalCandidateUserParam).getStr(DELETE_CANDIDATE_USER);
        }
        ResResult result = workflowService.deleteCandidateUser(taskId, userIdList);
        return result.getStr(DELETE_CANDIDATE_USER);
    }


    private String legalCandidateUserParam(String taskId, List<Long> userIdList) {
        if (StringUtils.isNotBlank(taskId)) {
            return "请指定当前任务!";
        }
        if (ContainerUtil.isNotEmpty(userIdList)) {
            return "请指定用户!";
        }
        return null;
    }

}
