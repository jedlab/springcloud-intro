package com.jedlab.paymentservice;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "UserPaymentController", description = "API for Payment Management")
public class UserServiceController
{

    UserProxy userProxy;
    
    
    
    
    public UserServiceController(UserProxy userProxy)
    {
        this.userProxy = userProxy;
    }


    @GetMapping("/pay")    
    @HystrixCommand(fallbackMethod="defaultList",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
                  })
    public ResponseEntity<String> getUserList(HttpServletRequest req)
    {
        return ResponseEntity.ok(userProxy.getUserList());
    }
    
    public ResponseEntity<String> defaultList(HttpServletRequest req)
    {
        return ResponseEntity.ok("jedlab user list");
    }
}
