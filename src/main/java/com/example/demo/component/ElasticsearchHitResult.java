package com.example.demo.component;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-08 10:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticsearchHitResult<T> implements Serializable {

    private static final long serialVersionUID = 6403621669247285115L;

    private T object;

    private Map<String, Object> highlightMap;

}
