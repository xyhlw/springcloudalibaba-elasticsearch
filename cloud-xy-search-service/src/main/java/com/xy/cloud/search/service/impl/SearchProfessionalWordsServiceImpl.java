package com.xy.cloud.search.service.impl;

import com.xy.cloud.search.dao.SearchProfessionalWordsDao;
import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.dto.SearchWordsSortDto;
import com.xy.cloud.search.result.Result;
import com.xy.cloud.search.service.SearchProfessionalWordsService;
import com.xy.cloud.search.vo.SearchProfessionalWordsVo;
import com.xy.cloud.search.vo.SearchWordsSortVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SearchProfessionalWordsServiceImpl  implements SearchProfessionalWordsService {

	@Autowired
	private SearchProfessionalWordsDao searchProfessionalWordsDao;

	@Override
	public Result<List<SearchProfessionalWordsVo>> list() {
		List<SearchProfessionalWordsVo> list =searchProfessionalWordsDao.list();
		Result result = new Result();
		result.setData(list);
		return result;
	}
}

