package com.example.demo.util.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/01/19
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZipFile {

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * 大小
     */
    private Long size;
    /**
     * 文件
     */
    private byte[] fileBytes;

}
