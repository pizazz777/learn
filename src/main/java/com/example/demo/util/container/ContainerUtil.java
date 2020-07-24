package com.example.demo.util.container;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author administrator
 * @date 2020/05/15
 * @description: 类描述: 容器工具
 **/
public class ContainerUtil {

    private ContainerUtil() {
    }

    /**
     * 不为 Null 或 空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        if (null != array && array.length > 0) {
            List<T> collect = Stream.of(array).filter(Objects::nonNull).collect(Collectors.toList());
            return collect.size() > 0;
        }
        return false;
    }

    /**
     * 为 Null 或 空
     */
    public static <T> boolean isEmpty(T[] array) {
        return !isNotEmpty(array);
    }

    /**
     * 不为 Null 或 空
     */
    public static <E> boolean isNotEmpty(Collection<E> collection) {
        if (null != collection && !collection.isEmpty()) {
            List<E> collect = collection.stream().filter(Objects::nonNull).collect(Collectors.toList());
            return collect.size() > 0;
        }
        return false;
    }

    /**
     * 为 Null 或 空
     */
    public static <E> boolean isEmpty(Collection<E> collection) {
        return !isNotEmpty(collection);
    }

    /**
     * 不为 Null 或 空
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return null != map && !map.isEmpty();
    }

    /**
     * 为 Null 或 空
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return !isNotEmpty(map);
    }

    /**
     * 将 Long 列表转为 Integer 列表
     */
    public static List<Integer> transformToInt(List<Long> list) {
        return list.stream().map(Long::intValue).collect(Collectors.toList());
    }

    /**
     * 将 Integer 列表转为 Long 列表
     */
    public static List<Long> transformToLong(List<Integer> list) {
        return list.stream().map(Integer::longValue).collect(Collectors.toList());
    }


    /**
     * 取出集合中连续的子集合
     *
     * @param list
     * @return
     */
    public static List<List<Integer>> getOrderList(List<Integer> list) {
        if (ContainerUtil.isNotEmpty(list)) {
            List<List<Integer>> lists = Lists.newArrayList();
            Collections.sort(list);
            Stack<Integer> stack = new Stack<>();
            for (Integer num : list) {
                if (!stack.isEmpty()) {
                    Integer peek = stack.peek();
                    if (Objects.equals(peek + 1, num)) {
                        stack.push(num);
                    } else {
                        lists.add(stack);
                        stack = new Stack<>();
                        stack.push(num);
                    }
                } else {
                    stack.push(num);
                }
            }
            // 最后一次是连续的情况
            lists.add(stack);
            return lists;
        }
        return null;
    }
}
