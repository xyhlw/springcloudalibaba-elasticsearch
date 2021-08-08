package com.xy.cloud.search.service;

import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.dto.SearchWordsSortDto;
import com.xy.cloud.search.result.Result;
import com.xy.cloud.search.vo.SearchProfessionalWordsVo;
import com.xy.cloud.search.vo.SearchWordsSortVo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface SearchProfessionalWordsService {

	Result<List<SearchProfessionalWordsVo>> list();


}

