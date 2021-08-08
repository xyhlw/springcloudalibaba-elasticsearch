package com.xy.cloud.search.dao;

import com.xy.cloud.search.result.Result;
import com.xy.cloud.search.vo.SearchProfessionalWordsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *
 * @author yangchang
 * @since 2021-07-02
 */
@Mapper
public interface SearchProfessionalWordsDao {

    int searchProfessionalWordsFlag(String name);

    List<SearchProfessionalWordsVo> list();

}
