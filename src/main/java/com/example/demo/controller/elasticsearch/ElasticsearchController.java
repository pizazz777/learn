package com.example.demo.controller.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotation.Action;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.customer.CustomerDO;
import com.example.demo.service.elasticsearch.ElasticsearchService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.demo.constant.log.ActionLogEnum.*;

/**
 * @author Administrator
 * @date 2020-06-02 14:31
 */
@RestController
@RequestMapping("/elasticsearch")
public class ElasticsearchController {

    private ElasticsearchService elasticsearchService;

    @Autowired
    public ElasticsearchController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    private static final String CREATE_INDEX = "创建索引";

    @ApiOperation(value = CREATE_INDEX)
    @GetMapping(value = "/create_index")
    @Action(type = SAVE, desc = CREATE_INDEX)
    public String createIndex(@RequestParam("index") String index) throws ServiceException {
        ResResult result = elasticsearchService.createIndex(index);
        return result.getStr(CREATE_INDEX);
    }

    private static final String ADD_DOC = "添加文档";

    @ApiOperation(value = ADD_DOC)
    @PostMapping(value = "/add_doc")
    @Action(type = SAVE, desc = ADD_DOC)
    public String addDoc(@RequestParam("index") String index, @RequestParam("docId") String docId) throws ServiceException {
        CustomerDO customer = CustomerDO.builder()
                .id(1L)
                .username("李四")
                .mobile("18876277055")
                .level(1)
                .sex(1)
                .createTime(LocalDateTime.now())
                .remark("过期手机号码")
                .belongToUserId(1L)
                .updateTime(LocalDateTime.now())
                .build();
        ResResult result = elasticsearchService.addDoc(index, docId, customer);
        return result.getStr(ADD_DOC);
    }


    private static final String UPDATE_DOC = "修改文档";

    @ApiOperation(value = UPDATE_DOC)
    @PostMapping(value = "/update_doc")
    @Action(type = UPDATE, desc = UPDATE_DOC)
    public String updateDoc(@RequestParam("index") String index, @RequestParam("docId") String docId, @RequestParam("jsonString") String jsonString) throws ServiceException {
        ResResult result = elasticsearchService.updateDoc(index, docId, jsonString);
        return result.getStr(UPDATE_DOC);
    }

    private static final String GET_DOC = "获取文档";

    @ApiOperation(value = GET_DOC)
    @GetMapping(value = "/get_doc")
    @Action(type = GET, desc = GET_DOC)
    public String getDoc(@RequestParam("index") String index,
                         @RequestParam("docId") String docId) throws ServiceException {
        if (StringUtils.isBlank(docId)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM).getStr(GET_DOC);
        }
        ResResult result = elasticsearchService.getDoc(index, docId);
        return result.getStr(GET_DOC);
    }


    private static final String DELETE_DOC = "删除文档";

    @ApiOperation(value = DELETE_DOC)
    @PostMapping(value = "/delete_doc")
    @Action(type = DELETE, desc = DELETE_DOC)
    public String deleteDoc(@RequestParam("index") String index,
                            @RequestParam("docId") String docId) throws ServiceException {
        ResResult result = elasticsearchService.deleteDoc(index, docId);
        return result.getStr(DELETE_DOC);
    }


    private static final String SEARCH = "查询";

    @ApiOperation(value = SEARCH)
    @GetMapping(value = "/search")
    @Action(type = GET, desc = SEARCH)
    public String search(@RequestParam("index") String index,
                         @RequestParam("field") String field,
                         @RequestParam("value") String value,
                         @RequestParam("from") Integer from,
                         @RequestParam("size") Integer size) throws ServiceException {
        ResResult result = elasticsearchService.search(index, field, value, from, size);
        return result.getStr(SEARCH);
    }

}
