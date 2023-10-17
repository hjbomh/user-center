package com.ou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ou.common.ErrorCode;
import com.ou.exception.BusinessException;
import com.ou.model.domain.User;
import com.ou.service.UserService;
import com.ou.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ou.constant.UserConstant.USER_LOGIN_STATE;


/**
 * @author Rangers
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-10-11 16:01:28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    //盐值 MD5加密
    private static final String SALT = "omh";
    @Resource
    UserMapper userMapper;

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
//      1、检验

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            // TODO: 2023/10/11  统一改成自定义异常类
            throw  new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userAccount.length() < 4) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"账号过短");
        }
        if (userPassword.length() < 8) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"密码过短");

        }
//      判断密码是否相同
        if (!userPassword.equals(checkPassword)) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"密码和检验密码不同");

        }
//      密码不能包含特殊字符
        String validPatten = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“’。，、？\\\\\\\\]";
        Matcher matcher = Pattern.compile(validPatten).matcher(userPassword);
        if (matcher.find()) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"密码包合特殊字符");
        }
//      检测账号是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"星球账号重复");

        }
//      2、密码加密

        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

//      3、向数据库插入账号数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(md5Password);
        user.setPlanetCode(planetCode);
        boolean save = this.save(user);
        if (!save) {
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"存储数据错误");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //      1、检验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw  new BusinessException(ErrorCode.NULL_ERROR,"数据为空");
        }
        if (userAccount.length() < 4) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度过短");
        }
        if (userPassword.length() < 8) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
//      账号不能包含特殊字符
        String validPatten = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“’。，、？\\\\\\\\]";
        Matcher matcher = Pattern.compile(validPatten).matcher(userAccount);
        if (matcher.find()) {
            throw  new BusinessException(ErrorCode.NULL_ERROR,"账号包合特殊字符");
        }
//       2、 密码加密
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", md5Password);
        User user = userMapper.selectOne(queryWrapper);
//        用户不存在
        if (user == null) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        //       3、 用户脱敏
        User safetyUser = getSafetyUser(user);
//       4、 记录登录用户状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);


        return safetyUser;
    }

    /**
     * 用户信息脱敏
     *
     * @param originUser 用户信息
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw  new BusinessException(ErrorCode.NULL_ERROR,"用户数据为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
//        移除登录Session信息
        request.removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}




