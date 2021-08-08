package com.xy.cloud.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xy.cloud.search.constants.Constants;
import com.xy.cloud.search.dao.SearchProfessionalWordsDao;
import com.xy.cloud.search.dao.SearchWordsSortDao;
import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.entity.ManualChnDirEntity;
import com.xy.cloud.search.entity.ManualEngDirEntity;
import com.xy.cloud.search.reposiory.ManualEngDirReposiory;
import com.xy.cloud.search.reposiory.ManualchnDirReposiory;
import com.xy.cloud.search.result.Result;
import com.xy.cloud.search.service.DirectoryService;
import com.xy.cloud.search.utli.ElasticsearchUtil;
import com.xy.cloud.search.utli.IdUtils;
import com.xy.cloud.search.vo.DirectoryQueryEsVo;
import com.xy.cloud.search.vo.SearchWordsSortVo;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(Constants.MANUAL_ES_ENG)
public   class ManualEngServiceImpl implements DirectoryService {
    private static final Logger LOG = LoggerFactory.getLogger(DirectoryService.class);

    @Autowired
    private ManualEngDirReposiory manualEngDirReposiory;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private SearchProfessionalWordsDao searchProfessionalWordsDao;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SearchWordsSortDao searchWordsSortDao;

    @Autowired
    private SearchResultMapper searchResultMapper;
    @Value("${es.manual_eng.type}")
    private String indexType;

    @Value("${es.manual_eng.index}")
    private String index;
    @Override
    public Result search(DirectoryDto directoryDto,String token) {
        DirectoryQueryEsVo esVo = new DirectoryQueryEsVo();
        BeanUtils.copyProperties(directoryDto,esVo);
        //获取数据专业名词
        Integer result = searchProfessionalWordsDao.searchProfessionalWordsFlag(directoryDto.getName());
        SearchWordsSortVo searchWordsSortVo = new SearchWordsSortVo();
        searchWordsSortVo.setLanguageType(Constants.MANUAL_ES_CHN);
        searchWordsSortVo.setUserId("124");
        //相关性算法查询
        List<SearchWordsSortVo> correlationList = searchWordsSortDao.searchWordsSortCorrelationList(searchWordsSortVo);
        //构建查询参数
        BoolQueryBuilder boolQueryBuilder = ElasticsearchUtil.buildfFlterParam(correlationList,esVo);
        //封装结果参数
        Map<String, Object> resultMap = ElasticsearchUtil.resultData( template, searchResultMapper,directoryDto, boolQueryBuilder,Constants.MANUAL_ES_ENG);
        return Result.ok(resultMap);
    }

    @Override
    public Result batchData(List<DirectoryDto> list) {
        try {
            //1.创建批量导入数据
            BulkRequest bulkRequest = new BulkRequest();
            //2.将数据批量添加
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < list.size (); i++) {
                bulkRequest.add (
                        new IndexRequest(index)
                                //不填id时将会生成随机id
                                .id (""+ IdUtils.ids()+i+"")
                                .source (mapper.writeValueAsString(list.get (i)), XContentType.JSON).type(indexType)
                );
            }
            //3.执行请求
            BulkResponse bulkResponse = restHighLevelClient.bulk (bulkRequest, RequestOptions.DEFAULT);
            //4.响应 判断是否执行成功
            RestStatus status = bulkResponse.status();
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("es add batchupdate data filed{}", e.getMessage());
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Override
    public Result batchUpdate(List<DirectoryDto> list) {
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach(directoryDto -> bulkRequest.add(new UpdateRequest(index,indexType, directoryDto.getId()).doc(JSON.toJSONString(directoryDto), XContentType.JSON)));
        try {
           restHighLevelClient.bulk (bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("es add batchupdate data filed{}", e.getMessage());
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

}