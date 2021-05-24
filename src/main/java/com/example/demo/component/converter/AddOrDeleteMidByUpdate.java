package com.example.demo.component.converter;

import com.google.common.collect.Lists;
import com.huang.util.container.ContainerUtil;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2020-08-25 11:41
 * @description 筛选相对补集
 */
@Getter
public class AddOrDeleteMidByUpdate<T> {

    /**
     * newList的相对补集
     */
    public List<T> addList = Lists.newArrayList();
    /**
     * oldList的相对补集
     */
    public List<T> deleteList = Lists.newArrayList();

    /**
     * 筛选相对补集
     *
     * @param newList 新集合
     * @param oldList 旧集合
     */
    public AddOrDeleteMidByUpdate(List<T> newList, List<T> oldList) {
        if (ContainerUtil.isEmpty(newList)) {
            deleteList.addAll(oldList);
        } else {
            // newList-oldList
            addList.addAll(newList.stream().filter(newObj ->
                    Objects.isNull(oldList.stream().filter(oldObj -> Objects.deepEquals(newObj, oldObj)).findAny().orElse(null))
            ).collect(Collectors.toList()));
            // oldList-newList
            deleteList.addAll(oldList.stream().filter(oldObj ->
                    Objects.isNull(newList.stream().filter(newObj -> Objects.deepEquals(newObj, oldObj)).findAny().orElse(null))
            ).collect(Collectors.toList()));
        }
    }


}
