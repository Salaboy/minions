package org.minions.demo;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("worker")
public interface Worker {

    @RequestLine("POST /work/{minion}")
    public void work(@Param("minion") String minion);
}
