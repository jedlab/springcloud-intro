package com.omidp.queuer;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/q")
@Slf4j
public class QueuerController
{

    @Autowired
    ArtemisProducer producer;
    
    
    
    @PostMapping(value = "/produce", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> queue(@RequestBody ApiValueHolder body)
    {
        
        try
        {
            ApiResponseVO send = producer.send(body);
            log.info("response {}", send.toString());
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return ResponseEntity.ok("ok");
    }
    
    
   
    
}
