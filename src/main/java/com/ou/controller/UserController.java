package com.ou.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ou.common.BaseResponse;
import com.ou.common.ErrorCode;
import com.ou.common.ResultUtils;
import com.ou.exception.BusinessException;
import com.ou.model.domain.User;
import com.ou.model.request.UserLoginRequest;
import com.ou.model.request.UserRegisterRequest;
import com.ou.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ou.constant.UserConstant.ADMIN_ROLE;
import static com.ou.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author Rangers
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 注册接口
     *
     * @param userRegisterRequest
     * @return
     */
    @RequestMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);

    }

    /**
     * 登录接口
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }


        User resultUser = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(resultUser);
    }

    @RequestMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @RequestMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long currentUserId = currentUser.getId();
        User user = userService.getById(currentUserId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 搜索用户
     *
     * @param username
     * @param request
     * @return 返回脱媒用户信息列表
     */
    @RequestMapping("/search")
    public BaseResponse<List<User>> userSearch(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        List<User> collect = userService.list(userQueryWrapper).stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    /**
     * 删除用户接口
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/delete")
    public BaseResponse<Boolean> userDelete(long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 用户权限查询
     *
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
//        仅管理员可查询
        return user != null && user.getUserRole() == ADMIN_ROLE;

    }
}
