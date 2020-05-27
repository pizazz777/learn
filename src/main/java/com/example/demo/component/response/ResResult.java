package com.example.demo.component.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/01/30
 * @description: 类描述: 响应结果类
 **/
@Slf4j
@Data
@NoArgsConstructor
public class ResResult<D> implements Serializable {
    private static final long serialVersionUID = -2475788606428077906L;

    /**
     * 正常响应中无响应数据
     */
    private static final String NO_DATA = "\"undefined\"";

    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private D data;
    /**
     * 响应代码
     */
    private ResCode resCode;

    /**
     * 成功
     */
    public static <D> ResResult<D> success() {
        return response(ResCode.OK, null, null);
    }

    /**
     * 成功
     */
    public static <D> ResResult<D> success(D data) {
        return response(ResCode.OK, null, data);
    }

    /**
     * 成功
     */
    public static <D> ResResult<D> success(String msg, D data) {
        return response(ResCode.OK, msg, data);
    }

    /**
     * 失败
     */
    public static <D> ResResult<D> fail() {
        return response(ResCode.FAILED, null, null);
    }

    /**
     * 失败
     */
    public static <D> ResResult<D> fail(ResCode resCode) {
        return response(resCode, null, null);
    }

    /**
     * 失败
     */
    public static <D> ResResult<D> fail(String msg) {
        return response(ResCode.FAILED, msg, null);
    }

    /**
     * 失败
     */
    public static <D> ResResult<D> fail(ResCode resCode, String msg) {
        return response(resCode, msg, null);
    }

    /**
     * 响应
     */
    public static <D> ResResult<D> response(ResCode resCode, String msg, D data) {
        ResResult<D> result = new ResResult<>();
        result.resCode = resCode;
        result.msg = msg;
        result.data = data;
        return result;
    }

    /**
     * 获取字符串
     */
    @JSONField(serialize = false, deserialize = false)
    public String getStr() {
        return getStr("");
    }

    /**
     * 获取字符串
     */
    public String getStr(String description) {
        // 如果没有设定 msg 则根据方法描述与 ResCode 描述生成 msg
        if (Objects.isNull(this.msg)) {
            if (Objects.nonNull(this.resCode)) {
                description = StringUtils.isBlank(description) ? "" : description + ":";
                this.msg = description + this.resCode.getDescription();
            }
        }
        printLog();
        String dataString = this.data == null ? NO_DATA : JSON.toJSONStringWithDateFormat(this.data, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        return "{" +
                "\"code\":" + this.resCode.getValue() + "," +
                "\"msg\":\"" + this.msg + "\"," +
                "\"data\":" + dataString +
                "}";
    }

    /**
     * 打印日志
     */
    private void printLog() {
        if (Objects.equals(this.resCode, ResCode.OK)) {
            log.info(msg);
        } else {
            log.error(msg);
        }
    }

    @Override
    public String toString() {
        return getStr();
    }
}
