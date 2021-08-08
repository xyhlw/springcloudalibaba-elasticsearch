package com.xy.cloud.search.controller;


import com.xy.cloud.search.annotation.DataPointsLog;
import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.result.Result;
import com.xy.cloud.search.service.DirectoryService;
import com.xy.cloud.search.service.SearchWordsSortService;
import com.xy.cloud.search.config.ApplicationContextProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *     es提供rpc调用分配给其他服务
 *
 * @author yangchang
 * @since 2021-06-30
 */
@Api(value = "搜索中心")
@RestController
@RequestMapping("/es/rpc")
public class EsFeginController {

    @Autowired
    DirectoryService directoryService;
    @Autowired
    SearchWordsSortService searchWordsSortService;
    /**
     * @decptions 批量写入ES数据
     * @param list
     * @param type 区分中文英文 1中文  2英文
     * @return
     */
    @PostMapping(value = "/batchData")
    @ApiOperation(value="批量添加数据",notes="批量添加数据")
    public Result batchData(@RequestBody List<DirectoryDto> list, @RequestHeader String type) {
        DirectoryService workflowCallBackService = (DirectoryService) ApplicationContextProvider.getBean(type);
        return  workflowCallBackService.batchData(list);
    }

    /**
     * @decptions 批量修改ES数据
     * @param list
     * @param type 区分中文英文 1中文  2英文
     * @return
     */
    @PostMapping(value = "/batchUpdate")
    @ApiOperation(value="批量修改数据",notes="批量修改数据")
    public Result batchUpdate(@RequestBody List<DirectoryDto> list,@RequestHeader String type) {
        DirectoryService workflowCallBackService = (DirectoryService) ApplicationContextProvider.getBean(type);
        return  workflowCallBackService.batchUpdate(list);
    }


    @DataPointsLog("第二次数据埋点")
    @PostMapping(value = "/dataPoints")
    @ApiOperation(value="二次数据埋点",notes="二次数据埋点")
    public Result dataPoints(@RequestBody DirectoryDto directoryDto,@RequestHeader String type) {
        return   searchWordsSortService.dataPoints(directoryDto,type);
    }



}
