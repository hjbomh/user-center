package com.ou.service.impl;

import com.ou.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    UserService service;

    @Test
    void userRegister(){
        String userAccount ="omhq";
        String userPassword="";
        String checkPassword="123456";
        String planetCode="1";
        Long result = service.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="omh";
         userPassword="123456";
         checkPassword="123456";
         result = service.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="omha";
        userPassword="123456";
        checkPassword="123456";
        result = service.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="omh";
        userPassword="1234568^";
        checkPassword="123456";
        result = service.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="omh";
        userPassword="12345678";
        checkPassword="12345665";
        result = service.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount ="omhj";
        userPassword="123456789";
        checkPassword="123456789";
        result = service.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertTrue(result>0);
    }

}