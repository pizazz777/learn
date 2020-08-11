package com.example.demo.component.response;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author administrator
 * @date 2020/04/17
 * @description: 类描述: 删除接口的响应信息
 **/
@Getter
@Setter
@ToString
public class DelResInfo implements Serializable {
    private static final long serialVersionUID = 4394130564641876091L;
    /**
     * 已删除的 Id 列表
     */
    private List<Serializable> deleted = Lists.newArrayList();
    /**
     * 未删除的 ID 列表
     */
    private List<Serializable> notDelete = Lists.newArrayList();

    /**
     * 新增 id 到删除列表
     *
     * @param id id
     */
    public void addDeleted(Serializable id) {
        deleted.add(id);
    }

    /**
     * 新增 id 到未删除列表
     *
     * @param id id
     */
    public void addNotDelete(Serializable id) {
        notDelete.add(id);
    }
}
