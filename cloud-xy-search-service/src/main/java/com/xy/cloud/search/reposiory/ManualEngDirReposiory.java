package com.xy.cloud.search.reposiory;

import com.xy.cloud.search.entity.ManualChnDirEntity;
import com.xy.cloud.search.entity.ManualEngDirEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ManualEngDirReposiory extends ElasticsearchRepository<ManualEngDirEntity, Long> {

}
 