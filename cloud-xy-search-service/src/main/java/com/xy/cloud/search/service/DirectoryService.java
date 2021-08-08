package com.xy.cloud.search.service;

import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DirectoryService {

	 Result search(DirectoryDto directoryDto,String token);

	 Result batchData(List<DirectoryDto> list);

	 Result batchUpdate(List<DirectoryDto> list);




}
