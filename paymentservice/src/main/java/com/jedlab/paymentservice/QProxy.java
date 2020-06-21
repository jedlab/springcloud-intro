package com.jedlab.paymentservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Q-SERVICE")
@RibbonClient(name = "Q-SERVICE")
@Service
public interface QProxy
{
    @PostMapping(value = "/api/v1/q/produce", produces = MediaType.APPLICATION_JSON_VALUE)
    public String queue(@RequestBody ApiValueHolder body);
}