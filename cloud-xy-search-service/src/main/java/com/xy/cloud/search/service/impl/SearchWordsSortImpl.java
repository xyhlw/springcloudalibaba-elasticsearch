package com.xy.cloud.search.service.impl;

import com.xy.cloud.search.dao.SearchWordsSortDao;
import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.dto.SearchWordsSortDto;
import com.xy.cloud.search.result.Result;
import com.xy.cloud.search.service.SearchWordsSortService;
import com.xy.cloud.search.vo.SearchWordsSortVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SearchWordsSortImpl  implements SearchWordsSortService {

	@Autowired
	SearchWordsSortDao searchWordsSortDao;

	@Override
	public Result<List<SearchWordsSortVo>> list(SearchWordsSortDto searchWordsSortDto,String type) {
		SearchWordsSortVo vo = new SearchWordsSortVo();
		vo.setLanguageType(type);
		List<SearchWordsSortVo> list = searchWordsSortDao.list(vo);
		Result result = new Result();
		result.setData(list);
		return result;
	}


	@Override
	public Result save(SearchWordsSortDto searchWordsSortDto) {
		SearchWordsSortVo vo = new SearchWordsSortVo();
		if(searchWordsSortDao.save(vo)>0){
			return	Result.ok();
		}
		return	Result.fail("");
	}

	@Override
	public Result searchWordsSortFlag(SearchWordsSortDto searchWordsSortDto) {
		SearchWordsSortVo vo = new SearchWordsSortVo();
		if(searchWordsSortDao.searchWordsSortFlag(vo)>0){
			return	Result.ok();
		}
		return	Result.fail("");
	}

	@Override
	public Result updateBySearchCount(SearchWordsSortDto searchWordsSortDto) {
		SearchWordsSortVo vo = new SearchWordsSortVo();
		if(searchWordsSortDao.updateBySearchCount(vo)>0){
			return	Result.ok();
		}
		return	Result.fail("");
	}

	@Override
	public Result dataPoints(DirectoryDto dto, String type) {
		try {
			SearchWordsSortVo vo = new SearchWordsSortVo();
			vo.setSearchWordsName(dto.getName());
			vo.setUserId("");
			vo.setUpdateDate(new Date());
			vo.setLanguageType(type);
			vo.setDirId(dto.getParentId());
			int result = searchWordsSortDao.searchWordsSortFlag(vo);
			if(result>0){
				searchWordsSortDao.updateBySearchCount(vo);
			}else{
				vo.setCreateDate(new Date());
				searchWordsSortDao.save(vo);
			}
			return  Result.ok();
		}catch (Exception e){
			e.printStackTrace();
			return  Result.fail(e.getMessage());

		}
	}
}
