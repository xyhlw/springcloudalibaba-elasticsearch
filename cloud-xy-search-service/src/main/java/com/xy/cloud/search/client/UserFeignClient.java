package com.xy.cloud.search.client;


import org.springframework.cloud.openfeign.FeignClient;



@FeignClient(value = "")
public interface UserFeignClient {

//    @PostMapping("/es/query/batchData")
//    Result batchData(@RequestBody List<DirectoryInfo> list);
}
