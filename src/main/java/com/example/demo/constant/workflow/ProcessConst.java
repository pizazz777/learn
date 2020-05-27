package com.example.demo.constant.workflow;

/**
 * @author Administrator
 * @date 2020-05-06 17:33
 */
public class ProcessConst {

    /**
     * 默认分支的值
     */
    public static final int DEFAULT_BRANCH_VALUE = 1;
    /**
     * 分支变量的key
     */
    public static final String BRANCH_KEY = "branch";
    /**
     * 待办人列表变量的key
     */
    public static final String CANDIDATE_USER_KEY = "candidateUserList";
    /**
     * 待办人变量key
     */
    public static final String ASSIGNEE_KEY = "assignee";
    /**
     * 审核变量
     */
    public static final String CHECK_KEY = "check";
    /**
     * 多个分支时的类型
     */
    public static final String TYPE_KEY = "type";
    /**
     * 流程的业务key
     */
    public static final String BUSINESS_KEY = "businessKey";
    /**
     * 流程的审批类型key
     */
    public static final String CHECK_TYPE_KEY = "checkType";
    /**
     * 审核-通过
     */
    public static final int CHECK_PASS = 1;
    /**
     * 审核-回退
     */
    public static final int CHECK_ROLLBACK = 2;
    /**
     * 审核-流程失败
     */
    public static final int CHECK_FAILED = 3;
    /**
     * {@link org.activiti.engine.impl.persistence.entity.CommentEntity#TYPE_EVENT}
     * {@link org.activiti.engine.impl.persistence.entity.CommentEntity#TYPE_COMMENT}
     * 审核批注的类型 有activiti的comment,event,及comment-check
     */
    public static final String COMMENT_CHECK = "comment-check";

}
