package com.example.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/01/30
 * @description: 类描述: 分页类,若需要分页的对象(model 包内的 XxxDO 对象)可继承此类
 **/
public class PageBean {
    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE = 1;
    /**
     * 默认每页条数
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 最少每页条数
     */
    private static final int MIN_PAGE_SIZE = 1;

    /**
     * 第几页
     */
    @JSONField(serialize = false)
    private int page;
    /**
     * 每页记录数
     */
    @JSONField(serialize = false)
    private int pageSize;

    public PageBean() {
        page = DEFAULT_PAGE;
        pageSize = DEFAULT_PAGE_SIZE;
    }

    public PageBean(int page, int pageSize) {
        setPageBean(page, pageSize);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return (page - 1) * pageSize;
    }

    /**
     * 设置默认 pageBean
     *
     * @param page     页码
     * @param pageSize 行数
     */
    public void setPageBean(Integer page, Integer pageSize) {
        if (Objects.isNull(page) || page < PageBean.DEFAULT_PAGE) {
            // 默认第一页
            page = PageBean.DEFAULT_PAGE;
        }
        if (Objects.isNull(pageSize) || pageSize < PageBean.MIN_PAGE_SIZE) {
            // 默认每页10条数据
            pageSize = PageBean.MIN_PAGE_SIZE;
        }
        this.page = page;
        this.pageSize = pageSize;
    }
}
