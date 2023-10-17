package com.ou.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册请求参数
 *
 * @author Rangers
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -3477075259599865283L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
