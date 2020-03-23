package com.jedlab.paymentservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "USER-SERVICE")
@RibbonClient(name = "USER-SERVICE")
@Service
public interface UserProxy
{
    @GetMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
    String getUserList();
}