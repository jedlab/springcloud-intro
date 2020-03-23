package com.jedlab.gateway;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "PAYMENT-SERVICE")
@RibbonClient(name = "PAYMENT-SERVICE")
@Service
public interface PaymentProxy
{
    @GetMapping(value = "/api/v1/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    String getUserList();
}