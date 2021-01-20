package com.example.demo.util.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class ZipDirectory {

    /**
     * 目录名称
     */
    private String name;
    /**
     * 该目录的所有文件
     */
    private List<ZipFile> zipFileList;
    /**
     * 子目录
     */
    private List<ZipDirectory> childZipDirectory;

}
