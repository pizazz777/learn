package com.example.demo.component.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/02/20
 * @description: 类描述: 响应 Data 为列表时的数据{@link ResResult#setData(Object)} )}
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResList<T> implements Serializable {

    private static final long serialVersionUID = -1169851137828568674L;

    /**
     * 获取实例
     *
     * @param list
     * @param count
     * @param <T>
     * @return
     */
    public static <T> ResList<T> page(Collection<T> list, Number count) {
        return getInstance(list, count);
    }

    /**
     * 获取实例
     * new MethodName : {@link ResList#page(Collection, Number)}
     *
     * @param list
     * @param count
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> ResList<T> getList(Collection<T> list, Number count) {
        return getInstance(list, count);
    }

    /**
     * 获取实例
     * new MethodName : {@link ResList#page(Collection, Number)}
     *
     * @param list
     * @param count
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> ResList<T> getInstance(Collection<T> list, Number count) {
        return new ResList<>(list, count);
    }

    /**
     * 返回的分页列表数据
     */
    private Collection<T> list;

    /**
     * 返回的总行数
     */
    private Number count;
}
