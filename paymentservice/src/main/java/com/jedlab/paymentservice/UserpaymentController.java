package com.jedlab.paymentservice;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "UserPaymentController", description = "API for Payment Management")
@Slf4j
public class UserpaymentController
{

    UserProxy userProxy;
    QProxy qproxy;
    
    

    


    public UserpaymentController(UserProxy userProxy, QProxy qproxy)
    {
        this.userProxy = userProxy;
        this.qproxy = qproxy;
    }

    @GetMapping("/pay")    
    @HystrixCommand(fallbackMethod="defaultList",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
                  })
    public ResponseEntity<String> getUserList(HttpServletRequest req)
    {
//        return ResponseEntity.ok(userProxy.getUserList());
        String queue2 = qproxy.queue(new ApiValueHolder("user-service", "{\"username\" :\"omid\"}", "/api/v1/users", HttpMethod.POST));
        log.info("omid rsp : {}", queue2);
        CompletableFuture.runAsync(()->{
            log.info("this operation takes 3 seconds, check artemis consumer");
            String queue = qproxy.queue(new ApiValueHolder("user-service", "{\"username\" :\"mehdi\"}", "/api/v1/users", HttpMethod.POST));
            log.info("mehdi rsp : {}", queue);
        });
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String queue = qproxy.queue(new ApiValueHolder("user-service", "{\"username\" :\"aram\"}", "/api/v1/users", HttpMethod.POST));
        log.info("aram rsp : {}", queue);
        
        return ResponseEntity.ok("ok");
    }
    
    public ResponseEntity<String> defaultList(HttpServletRequest req)
    {
        return ResponseEntity.ok("jedlab user list");
    }
}
