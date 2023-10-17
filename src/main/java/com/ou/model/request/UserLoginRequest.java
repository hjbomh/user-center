package com.ou.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求参数
 * @author Rangers
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1401945551230243922L;
    private  String userAccount;
    private  String userPassword;
}
