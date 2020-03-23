package com.jedlab.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class GatewayController
{
    
    
    PaymentProxy pp;
    
    

    public GatewayController(PaymentProxy pp)
    {
        this.pp = pp;
    }



    @GetMapping("/test")
    public String test()
    {
        return pp.getUserList();
    }
    
    
}
