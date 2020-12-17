package com.example.demo.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.component.response.ResCode;
import com.example.demo.util.container.ContainerUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-27 15:31
 * @description: http请求工具类
 */
@Slf4j
public class HttpUtil {

    private HttpUtil() {
    }

    // 模拟浏览器请求
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
    // 默认请求设置
    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            //设置连接超时时间，单位毫秒
            .setConnectTimeout(5000)
            //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的
            .setConnectionRequestTimeout(1000)
            //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
            .setSocketTimeout(5000)
            .build();

    /**
     * 发送 http get 请求
     *
     * @param url 请求url地址
     * @return Http响应
     */
    public static CloseableHttpResponse sendGet(String url) {
        return sendGet(url, DEFAULT_REQUEST_CONFIG);
    }

    /**
     * 发送 http get 请求
     *
     * @param url      请求url地址
     * @param paramMap 请求参数
     * @return Http响应
     */
    public static CloseableHttpResponse sendGet(String url, Map<String, String> paramMap) {
        return sendGet(url, paramMap, DEFAULT_REQUEST_CONFIG);
    }

    /**
     * 发送 http get 请求
     *
     * @param url           请求url地址
     * @param paramMap      请求参数
     * @param requestConfig 请求设置
     * @return Http响应
     */
    public static CloseableHttpResponse sendGet(String url, Map<String, String> paramMap, RequestConfig requestConfig) {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (ContainerUtil.isNotEmpty(paramMap)) {
            stringBuilder.append("?");
            paramMap.forEach((key, value) -> {
                try {
                    stringBuilder.append(key).append("=").append(URLEncoder.encode(value, Charsets.UTF_8.name())).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return sendGet(stringBuilder.toString(), requestConfig);
    }

    /**
     * 发送 http get 请求
     *
     * @param url           请求地址
     * @param requestConfig 请求设置
     * @return Http响应
     */
    public static CloseableHttpResponse sendGet(String url, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            // GET请求
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            httpGet.addHeader(HTTP.USER_AGENT, USER_AGENT);
            log.debug("\n请求地址为:\n" + url);
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return response;
    }


    /**
     * 发送 http post 请求
     *
     * @param url      请求地址
     * @param paramMap 参数值映射
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url, Map<String, String> paramMap) {
        return sendPost(url, paramMap, DEFAULT_REQUEST_CONFIG);
    }


    /**
     * 发送 http post 请求
     *
     * @param url           请求地址
     * @param paramMap      参数值映射
     * @param requestConfig 请求设置
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url, Map<String, String> paramMap, RequestConfig requestConfig) {
        List<NameValuePair> params = Lists.newArrayList();
        paramMap.forEach((key, value) -> {
            try {
                params.add(new BasicNameValuePair(key, URLEncoder.encode(value, StandardCharsets.UTF_8.name())));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        return sendPost(url, params, requestConfig);
    }


    /**
     * 发送 http post 请求
     *
     * @param url    请求地址
     * @param params 参数值映射
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url, List<NameValuePair> params) {
        return sendPost(url, params, DEFAULT_REQUEST_CONFIG);
    }


    /**
     * 发送 http post 请求
     *
     * @param url           请求地址
     * @param params        参数值映射
     * @param requestConfig 请求设置
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url, List<NameValuePair> params, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(requestEntity);
            //添加请求头
            httpPost.addHeader(HTTP.USER_AGENT, USER_AGENT);
            log.debug("\n请求地址为:\n" + url);
            log.debug("\n请求参数为:\n" + params.toString());
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return response;
    }


    /**
     * 发送 http post 请求
     *
     * @param url  请求地址
     * @param json 请求的json参数
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url, String json) {
        return sendPost(url, json, DEFAULT_REQUEST_CONFIG);
    }

    /**
     * 发送 http post 请求
     *
     * @param url           请求地址
     * @param json          请求的json参数
     * @param requestConfig 请求设置
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url, String json, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(json, StandardCharsets.UTF_8);
            requestEntity.setContentEncoding(StandardCharsets.UTF_8.name());
            requestEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(requestEntity);
            httpPost.addHeader(HTTP.USER_AGENT, USER_AGENT);
            log.debug("\n请求地址为:" + url);
            log.debug("\n请求参数为:" + json);
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return response;
    }


    /**
     * 发送 http post 请求
     *
     * @param url 请求地址
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url) {
        return sendPost(url, DEFAULT_REQUEST_CONFIG);
    }


    /**
     * 发送 http post 请求
     *
     * @param url           请求地址
     * @param requestConfig 请求设置
     * @return Http响应
     */
    public static CloseableHttpResponse sendPost(String url, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
            httpPost.setConfig(requestConfig);
            httpPost.addHeader(HTTP.USER_AGENT, USER_AGENT);
            log.debug("\n请求地址为:\n" + url);
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return response;
    }

    /**
     * 请求结果转为实体
     *
     * @param response 响应对象
     * @param clazz    要转换的类型
     * @return +实体类
     * @throws IOException e
     */
    public static <T> T transformEntity(CloseableHttpResponse response, Class<T> clazz) throws IOException {
        if (Objects.nonNull(response)) {
            int statusCode = response.getStatusLine().getStatusCode();
            log.debug("状态码:  " + statusCode);
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(result);
            if (Objects.nonNull(jsonObject)) {
                Integer code = jsonObject.getInteger("code");
                if (Objects.equals(code, ResCode.OK.getValue())) {
                    return JSONObject.parseObject(jsonObject.getString("data"), clazz);
                }
            }
        }
        return null;
    }


    /**
     * 关闭流
     *
     * @param httpClient 请求
     * @param response   响应流
     */
    private static void close(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        try {
            // 后用先关
            if (null != response) {
                response.close();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
