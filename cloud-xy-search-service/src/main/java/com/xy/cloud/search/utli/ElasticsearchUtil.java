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
    public static  Map<String,Object> resultData(ElasticsearchTemplate template,SearchResultMapper searchResultMapper, DirectoryDto directoryDto,QueryBuilder boolQueryBuilder,String type){
        Pageable pageable = new QPageRequest(directoryDto.getPageNum()-1, directoryDto.getPageSize());
        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //查询关键字directoryDto
        queryBuilder.withQuery(boolQueryBuilder);
        //分页查询
        queryBuilder.withPageable(pageable);
        // 构建高亮查询
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //多次段高亮需要设置为false
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field(ES_ENTITY_TITLE).field(ES_ENTITY_CONENT).field(ES_ENTITY_URL).field(ES_ENTITY_HREF)
                .preTags(ES_CLOER_HGITH_START).postTags(ES_CLOER_HGITH_STOP);

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

        queryBuilder.withHighlightBuilder(highlightBuilder);
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
    public static BoolQueryBuilder buildfFlterParam(List<SearchWordsSortVo> list,DirectoryQueryEsVo query){
        // 1.拼接查询条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 2.模糊查询 name、 subtitle、detail含有 搜索关键字
        builder.must(QueryBuilders.multiMatchQuery(query.getName(), ES_ENTITY_URL, ES_ENTITY_CONENT, ES_ENTITY_TITLE));

        if (!StringUtils.isEmpty(query.getUsageScenariosType())) {
            String [] usageScenariosType = query.getUsageScenariosType().split(",");
            if (!list.isEmpty()) {
                List<String> uList = list.stream().map(s -> s.getManualType()).collect(Collectors.toList());
                for (String usageType : usageScenariosType) {
                    uList.add(usageType);
                }
                builder.should(QueryBuilders.matchQuery(ES_USAGESCENARIOSTYPE, uList));
            } else {
                builder.should(QueryBuilders.matchQuery(ES_USAGESCENARIOSTYPE, usageScenariosType));
            }
        }
        if (!StringUtils.isEmpty(query.getParentId())) {
            String [] arrayPrentId = query.getParentId().split(",");
            if (!list.isEmpty()) {
                List<String> pList = list.stream().map(s -> s.getManualType()).collect(Collectors.toList());
                for (String parentId : arrayPrentId) {
                    pList.add(parentId);
                }
                builder.should(QueryBuilders.matchQuery(ES_PARENTID, pList));
            } else {
                builder.should(QueryBuilders.matchQuery(ES_PARENTID, arrayPrentId));

            }
        }
        if (!StringUtils.isEmpty(query.getManualType())) {
            String[] arrayUsageScenariosType = query.getManualType().split(",");
            if (!list.isEmpty()) {
                List<String> mList = list.stream().map(s -> s.getManualType()).collect(Collectors.toList());
                for (String scenariosType : arrayUsageScenariosType) {
                    mList.add(scenariosType);
                }
                builder.should(QueryBuilders.matchQuery(ES_MANUALTYPE, mList));
            } else {
                builder.should(QueryBuilders.matchQuery(ES_MANUALTYPE, arrayUsageScenariosType));
            }
        }
        return builder;
    }
}