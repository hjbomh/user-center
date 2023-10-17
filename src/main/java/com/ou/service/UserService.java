package com.ou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ou.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rangers
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-10-11 16:01:28
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 检验密码
     * @return 用户ID
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request
     * @return 用户脱敏后信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return 用户脱敏后信息
     */
    User getSafetyUser(User originUser);

    /**
     * 登录注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
