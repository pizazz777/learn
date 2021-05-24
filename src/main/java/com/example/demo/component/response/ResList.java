package com.example.demo.component.response;

import com.example.demo.entity.PageBean;
import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author administrator
 * @date 2020/04/30
 * @description: 响应 Data 为列表时的数据{@link ResResult#setData(Object)} )}
 **/
@Data
@AllArgsConstructor
public class ResList<T extends PageBean> implements Serializable {

    private static final long serialVersionUID = -1169851137828568674L;

    /**
     * 返回的分页列表数据
     */
    private Collection<T> list;

    /**
     * 总数量
     */
    private Number totalCount;

    /**
     * 当前页
     */
    private Number currentPage;

    /**
     * 总页数
     */
    private Number totalPage;

    /**
     * 设置分页信息
     *
     * @param list 要处理的数据列表
     * @return {@link ResList<T>}
     */
    public static <T extends PageBean> ResList<T> page(Collection<T> list) {
        Page page = (Page) list;
        return new ResList<>(list, page.getTotal(), page.getPageNum(), page.getPages());
    }

}
