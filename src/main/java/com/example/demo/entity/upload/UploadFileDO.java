package com.example.demo.entity.upload;

import lombok.*;
import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/07/16
 * @description: 类描述: 文件上传表 Model
 **/
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileDO implements Serializable {

    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_BAN = 2;

    private static final long serialVersionUID = 970683114502255953L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("访问地址")
    private String url;

    @ApiModelProperty("文件服务器路径")
    private String path;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("文件类型:1.目录；2-n：各种文件类型")
    private Integer type;

    @ApiModelProperty("文件大小")
    private Long size;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("文件状态：1.正常；2.禁用")
    private Integer status;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    /* ------------------ 非数据库数据分割线 ------------------ */

}
