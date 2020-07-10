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
    public String addDoc(@RequestParam("docId") String docId) throws ServiceException {
        CustomerDO customer = CustomerDO.builder()
                .id(1L)
                .username("李四")
                .mobile("18876277055")
                .level(1)
                .sex(1)
                .createTime(LocalDateTime.now())
                .remark("所有发布的视频中，多为唱歌或与女子的\"合拍\"视频，其中夹杂着生活随手拍，神态轻松面带微笑。从视频配的文字可以看出，" +
                        "张某钢经常在视频中征婚，他曾说\"想找我自己的另一半\"。" +
                        "从时间上来看，张某钢更新动态频率很高，事发前2天还发布了唱歌的视频，12天前还帮老父亲剪了指甲、一起吃了饭。" +
                        "7月7日12时30分，贵州安顺一辆2路车突然冲进水库。有目击者表示：公交车落水后，几乎一瞬间就被淹没了。" +
                        "视频画面显示：公交车冲撞护栏的前49秒中，前45秒都是缓慢行驶，最后4秒钟，车子突然向左转向，穿过5排车道后，" +
                        "直接撞向护栏，随后坠入水中。转向时，左右车道均无来往车辆。" +
                        "监控画面显示：车内并没有人与司机发生过任何肢体冲突，车内乘客也称司机没有什么异常的表现。")
                .belongToUserId(1L)
                .updateTime(LocalDateTime.now())
                .build();
        ResResult result = elasticsearchService.addDoc(docId, customer);
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
