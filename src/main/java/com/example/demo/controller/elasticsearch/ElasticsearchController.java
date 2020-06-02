package com.example.demo.controller.elasticsearch;

import com.example.demo.annotation.Action;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.customer.CustomerDO;
import com.example.demo.service.elasticsearch.ElasticsearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.demo.constant.log.ActionLogEnum.GET;
import static com.example.demo.constant.log.ActionLogEnum.SAVE;

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
                .id(2L)
                .username("张三")
                .mobile("18581598259")
                .level(3)
                .sex(1)
                .createTime(LocalDateTime.now())
                .build();
        ResResult result = elasticsearchService.addDoc(index, docId, customer);
        return result.getStr(ADD_DOC);
    }

    private static final String GET_DOC = "获取文档";

    @ApiOperation(value = GET_DOC)
    @GetMapping(value = "/get_doc")
    @Action(type = GET, desc = GET_DOC)
    public String getDoc(@RequestParam("index") String index,
                         @RequestParam("docId") String docId) throws ServiceException {
        ResResult result = elasticsearchService.getDoc(index, docId);
        return result.getStr(GET_DOC);
    }

    private static final String SEARCH = "查询";

    @ApiOperation(value = SEARCH)
    @GetMapping(value = "/search")
    @Action(type = GET, desc = SEARCH)
    public String search(@RequestParam("index") String index,
                         @RequestParam("field") String field,
                         @RequestParam("key") String key,
                         @RequestParam("from") Integer from,
                         @RequestParam("size") Integer size) throws ServiceException {
        ResResult result = elasticsearchService.search(index, field, key, from, size);
        return result.getStr(SEARCH);
    }

}
