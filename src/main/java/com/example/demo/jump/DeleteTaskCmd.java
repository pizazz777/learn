package com.example.demo.jump;

import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;

/**
 * @author Administrator
 * @version 1.0
 * @date 2020/05/08
 * @description: 删除任务
 */
public class DeleteTaskCmd extends NeedsActiveTaskCmd<String> {

    private static final long serialVersionUID = -1953105578890242426L;

    private String deleteReason;

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public DeleteTaskCmd(String taskId, String deleteReason) {
        super(taskId);
        this.deleteReason = deleteReason;
    }

    @Override
    public String execute(CommandContext commandContext, TaskEntity currentTask) {
        //获取所需服务
        TaskEntityManagerImpl taskEntityManager = (TaskEntityManagerImpl) commandContext.getTaskEntityManager();
        //获取当前任务的来源任务及来源节点信息
        ExecutionEntity executionEntity = currentTask.getExecution();
        //删除当前任务,来源任务
        taskEntityManager.deleteTask(currentTask, deleteReason, false, false);
        return executionEntity.getId();
    }

    @Override
    public String getSuspendedTaskException() {
        return "挂起的任务不能跳转";
    }
}