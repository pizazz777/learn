package com.example.demo.entity.upload;

import com.example.demo.component.exception.UploadException;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author administrator
 * @date 2020/07/16
 * @description: 类描述: base64拆分对象
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Base64StringDO implements Serializable {
    private static final long serialVersionUID = 4623612013631327035L;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 内容
     */
    private String content;

    /**
     * 通过base64字符串获取对象
     *
     * @param base64String base64
     * @return r
     */
    public static Base64StringDO getByBase64(String base64String) throws UploadException {
        String[] dataArray = base64String.split("base64,");
        /*
         * base64被分成两部分,一部分前缀是指明文件类型,后面部分是文件内容的 base64编码
         */
        int defaultLength = 2;
        if (Objects.equals(defaultLength, dataArray.length)) {
            return Base64StringDO.builder()
                    .prefix(dataArray[0])
                    .content(dataArray[1])
                    .build();
        }
        throw new UploadException("获取Base64对象失败,数据不合法");
    }
}
