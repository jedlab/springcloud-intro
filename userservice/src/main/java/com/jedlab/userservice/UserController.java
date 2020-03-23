package com.jedlab.userservice;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "UserController", description = "API for User Management")
public class UserController
{

    
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserList() {
        return "user list";
    }
    
    
}
