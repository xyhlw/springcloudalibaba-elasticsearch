package com.xy.cloud.search.utli;
import com.xy.cloud.search.constants.Constants;
import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.entity.ManualChnDirEntity;
import com.xy.cloud.search.entity.ManualEngDirEntity;
import com.xy.cloud.search.vo.DirectoryQueryEsVo;
import com.xy.cloud.search.vo.SearchWordsSortVo;
import com.querydsl.core.group.GroupBy;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xy.cloud.search.constants.Constants.*;
import static com.xy.cloud.search.constants.Constants.ES_CLOER_HGITH_START;
import static com.xy.cloud.search.constants.Constants.ES_CLOER_HGITH_STOP;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Component
public class ElasticsearchUtil {
    /**
     *
     * @param template   es 查询模板
     * @param directoryDto  搜索参数返回
     * @param searchResultMapper 搜索返回结果
     * @param result 分词搜索区分
     * @param type 中英文分类区分
     * @return Map 封装返回结果
     */
    public static  Map<String,Object> resultData(ElasticsearchTemplate template, SearchResultMapper searchResultMapper,
                                                 DirectoryDto directoryDto, SearchQuery searchQuery, String type){
        AggregatedPage<?> resultPage = null;
        List<?> content = null;
        if( Constants.MANUAL_ES_CHN.equals(type)){
            // 获取查询结果
            resultPage = template.queryForPage(searchQuery, ManualChnDirEntity.class, searchResultMapper);
            // 获取集合数据
            content = resultPage.getContent();
        }else{
            resultPage = template.queryForPage(searchQuery, ManualEngDirEntity.class, searchResultMapper);
            // 获取集合数据
            content = resultPage.getContent();
        }
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        List<DirectoryDto> directoryDtoList = mapperFactory.getMapperFacade().mapAsList(content, DirectoryDto.class);
        Map<String,Object> resultMap =new HashMap<>();
        resultMap.put("pageNum",directoryDto.getPageNum());
        resultMap.put("pageSize",directoryDto.getPageSize());
        resultMap.put("total",resultPage.getTotalElements());
        resultMap.put("rows",directoryDtoList);
        return  resultMap;
    }


    /**
     * 分词查询
     * @param list
     * @param query
     * @return
     */
    public static BoolQueryBuilder buildfFlterParam(List<SearchWordsSortVo> list,DirectoryDto query,DirectoryDto aggregation){
        // 1.拼接查询条件
        BoolQueryBuilder builder = boolQuery();
        // 2.模糊查询 name、 subtitle、detail含有 搜索关键字
        builder.must(QueryBuilders.multiMatchQuery(query.getName(), ES_VERSION, ES_ENTITY_CONENT, ES_ENTITY_TITLE));
        SetList<String> pList = new SetList<>();
        SetList<String> mList = new SetList<>();
        SetList<String> uList = new SetList<>();

        if(!list.isEmpty()&& query.getpList()==null&& query.getuList()==null&& query.getmList()==null){
            for(SearchWordsSortVo searchWordsSortVo:list){
                pList.add(searchWordsSortVo.getDirId());
                uList.add(searchWordsSortVo.getUsageScenariosType());
                mList.add(searchWordsSortVo.getManualType());
            }
            for(String uType:aggregation.getuList()){
                uList.add(uType);
            }
            for (String mType:aggregation.getmList()){
                mList.add(mType);
            }
            for (String parentId:aggregation.getpList()){
                pList.add(parentId);
            }
            builder.filter(QueryBuilders.termsQuery(ES_USAGESCENARIOSTYPE, uList));
            builder.filter(QueryBuilders.termsQuery(ES_MANUALTYPE, mList));
            builder.filter(QueryBuilders.termsQuery(ES_PARENTID, pList));
            return  builder;
        }

        if(!list.isEmpty()){
            for(SearchWordsSortVo searchWordsSortVo:list){
                pList.add(searchWordsSortVo.getDirId());
                uList.add(searchWordsSortVo.getUsageScenariosType());
                mList.add(searchWordsSortVo.getManualType());
            }
        }
        if(query.getpList()!=null && query.getpList().size()>0){
            for(String pId : query.getpList()){
                pList.add(pId);
            }
            builder.filter(QueryBuilders.termsQuery(ES_PARENTID, pList));

        }else {
            if(query.getpList()!=null){
                builder.filter(QueryBuilders.termsQuery(ES_PARENTID, pList));
            }
        }

        if(query.getmList()!=null && query.getmList().size()>0){
            for(String mType : query.getmList()){
                mList.add(mType);
            }
            builder.filter(QueryBuilders.termsQuery(ES_MANUALTYPE, mList));

        }else {
            if(query.getmList()!=null){
                builder.filter(QueryBuilders.termsQuery(ES_MANUALTYPE, mList));
            }
        }

        if(query.getuList()!=null && query.getuList().size()>0){
            for(String uType : query.getuList()){
                uList.add(uType);
            }
            builder.filter(QueryBuilders.termsQuery(ES_USAGESCENARIOSTYPE, uList));

        }else {
            if(query.getuList()!=null){
                builder.filter(QueryBuilders.termsQuery(ES_USAGESCENARIOSTYPE, uList));
            }
        }

        return builder;
    }

    /**
     * 聚合结果
     * @return
     */
    public static SearchQuery getSearchQuery(List<SearchWordsSortVo> list,DirectoryDto query,DirectoryDto aggregation ){
        //设置分页
        Pageable pageable = new QPageRequest(query.getPageNum(),query.getPageSize());
        //构建筛选
        BoolQueryBuilder boolQuery = buildfFlterParam( list,query,aggregation);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = null;
        if(!list.isEmpty()){
            BoolQueryBuilder boolQueryBuilderFilter = new BoolQueryBuilder();
            //设置权重重新打分
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[list.size()];
            for(int i =0;i<list.size();i++){
                int number = i + 1 ;
                int wegitht = (30-number)/number;
                int functionWegith = (10000-number)/number;
                boolQueryBuilderFilter.should(boolQuery().must(queryStringQuery("*"+list.get(i).getDirId()+"*").defaultField(ES_PARENTID).boost(wegitht)));
                boolQueryBuilderFilter.should(boolQuery().must(queryStringQuery("*"+list.get(i).getManualType()+"*").defaultField(ES_MANUALTYPE).boost(wegitht)));
                boolQueryBuilderFilter.should(boolQuery().must(queryStringQuery("*"+list.get(i).getUsageScenariosType()+"*").defaultField(ES_USAGESCENARIOSTYPE).boost(wegitht)));
                filterFunctionBuilders[i]=  new FunctionScoreQueryBuilder.FilterFunctionBuilder(boolQueryBuilderFilter, ScoreFunctionBuilders.weightFactorFunction(functionWegith));
            }
            functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQuery, filterFunctionBuilders);
            queryBuilder.withQuery(functionScoreQueryBuilder);
        }else {
            queryBuilder.withQuery(boolQuery);
        }
        //聚合分组查询数据
        TermsAggregationBuilder termsSourceTypeParentIdGroup = AggregationBuilders.terms("pList")
                .field("parentId").size(1000000);
        TermsAggregationBuilder termsSourceTypeMtGroup = AggregationBuilders.terms("mList")
                .field("manual_type").size(1000000);
        TermsAggregationBuilder termsSourceTypeUSGroup = AggregationBuilders.terms("uList")
                .field("usage_scenarios_type").size(1000000);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field(ES_VERSION).field(ES_ENTITY_CONENT).field(ES_ENTITY_TITLE).preTags("<span style='color:red'>").postTags("</span>");
        queryBuilder.withHighlightBuilder(highlightBuilder);
        queryBuilder.addAggregation(termsSourceTypeParentIdGroup);
        queryBuilder.addAggregation(termsSourceTypeMtGroup);
        queryBuilder.addAggregation(termsSourceTypeUSGroup);
        queryBuilder.withPageable(pageable);
        return  queryBuilder.build();
    }

    /**
     * 聚合结果
     * @return
     */
    public static DirectoryDto getAggregation(ElasticsearchTemplate template, SearchResultMapper searchResultMapper,
                                              DirectoryDto directoryDto, String type){

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 1.拼接查询条件
        BoolQueryBuilder builder = boolQuery();
        // 2.模糊查询 name、 subtitle、detail含有 搜索关键字
        builder.must(QueryBuilders.multiMatchQuery(directoryDto.getName(), ES_VERSION, ES_ENTITY_CONENT, ES_ENTITY_TITLE));
        //聚合分组查询数据
        TermsAggregationBuilder termsSourceTypeParentIdGroup = AggregationBuilders.terms("pList")
                .field("parentId").size(1000000);
        TermsAggregationBuilder termsSourceTypeMtGroup = AggregationBuilders.terms("mList")
                .field("manual_type").size(1000000);
        TermsAggregationBuilder termsSourceTypeUSGroup = AggregationBuilders.terms("uList")
                .field("usage_scenarios_type").size(1000000);
        queryBuilder.addAggregation(termsSourceTypeParentIdGroup);
        queryBuilder.addAggregation(termsSourceTypeMtGroup);
        queryBuilder.addAggregation(termsSourceTypeUSGroup);
        queryBuilder.withQuery(builder);
        NativeSearchQuery build = queryBuilder.build();
        AggregatedPage<?> resultPage = null;
        List<?> content = null;
        if( Constants.MANUAL_ES_CHN.equals(type)){
            // 获取查询结果
            resultPage = template.queryForPage(build, ManualChnDirEntity.class, searchResultMapper);
            // 获取集合数据
            content = resultPage.getContent();
        }else{
            resultPage = template.queryForPage(build, ManualEngDirEntity.class, searchResultMapper);
            // 获取集合数据
            content = resultPage.getContent();
        }
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        List<DirectoryDto> directoryDtoList = mapperFactory.getMapperFacade().mapAsList(content, DirectoryDto.class);
        if(!directoryDtoList.isEmpty()){
            return  directoryDtoList.get(directoryDtoList.size()-1);
        }
        return  null;
    }

}