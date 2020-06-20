package com.jedlab.userservice;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "UserController", description = "API for User Management")
public class UserController
{

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserList()
    {
        return "user list";
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "ایجاد کاربر", notes = "برای ایجاد کاربر استفاده میشود")
    
    public String createUser(@RequestBody  UserVO vo)
    {
        return "user created " + vo.getUsername();
    }

    @ApiModel
    public static class UserVO
    {
        private String username;
        private String password;
        private String phoneNo;

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

    }

}
