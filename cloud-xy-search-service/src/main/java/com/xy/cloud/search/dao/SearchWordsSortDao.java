package com.xy.cloud.search.dao;


import com.xy.cloud.search.vo.SearchWordsSortVo;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface SearchWordsSortDao {


	int save(SearchWordsSortVo searchWordsSortVo);

	int searchWordsSortFlag(SearchWordsSortVo searchWordsSortVo);

	int updateBySearchCount(SearchWordsSortVo searchWordsSortVo);

	List<SearchWordsSortVo> list(SearchWordsSortVo searchWordsSortVo);

	List<SearchWordsSortVo> searchWordsSortCorrelationList(SearchWordsSortVo searchWordsSortVo);



}
