package com.xy.cloud.search.controller;


import com.xy.cloud.search.annotation.DataPointsLog;
import com.xy.cloud.search.config.ApplicationContextProvider;
import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.dto.SearchWordsSortDto;
import com.xy.cloud.search.result.Result;
import com.xy.cloud.search.service.DirectoryService;
import com.xy.cloud.search.service.SearchProfessionalWordsService;
import com.xy.cloud.search.service.SearchWordsSortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Api(value = "搜索中心中文api")
@RestController
@RequestMapping("/es/query")
public class EsQueryController {

    @Autowired
    DirectoryService directoryService;

    @Autowired
    SearchWordsSortService searchWordsSortService;

    @Autowired
    SearchProfessionalWordsService searchProfessionalWordsService;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * @decptions 所有的查询都会进行第一次数据埋点
     * @param directoryDto
     * @param type
     * @return
     */
    @DataPointsLog("第一次数据埋点")
    @PostMapping(value = "/queryName")
    @ApiOperation(value="查询信息",notes="查询信息")
    public Result queryName(@RequestBody DirectoryDto directoryDto, @RequestHeader String type, HttpServletRequest request) {
        DirectoryService directoryAbstrtService = (DirectoryService) ApplicationContextProvider.getBean(type);
        return   directoryAbstrtService.search(directoryDto,type);
    }


    @PostMapping(value = "/dataAssociationList")
    @ApiOperation(value="数据联想",notes="数据联想")
    public Result dataAssociation() {
        return  searchProfessionalWordsService.list();
    }


    @PostMapping(value = "/hotWordRankingList")
    @ApiOperation(value="热词排行",notes="热词排行")
    public Result hotWordRankingList(@RequestBody SearchWordsSortDto searchWordsSortDto, @RequestHeader String type) {
        return  searchWordsSortService.list(searchWordsSortDto,type);
    }

}
