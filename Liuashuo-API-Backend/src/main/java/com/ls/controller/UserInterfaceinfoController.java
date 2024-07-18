package com.ls.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.annotation.AuthCheck;
import com.ls.common.BaseResponse;
import com.ls.common.DeleteRequest;
import com.ls.common.ErrorCode;
import com.ls.common.ResultUtils;
import com.ls.common.model.entity.User;
import com.ls.common.model.entity.UserInterfaceInfo;
import com.ls.constant.CommonConstant;
import com.ls.exception.BusinessException;
import com.ls.model.dto.userInterfaceinfo.UserInterfaceinfoAddRequest;
import com.ls.model.dto.userInterfaceinfo.UserInterfaceinfoQueryRequest;
import com.ls.model.dto.userInterfaceinfo.UserInterfaceinfoUpdateRequest;
import com.ls.service.UserInterfaceInfoService;
import com.ls.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户调用接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/user/interfaceinfo")
@Slf4j
public class UserInterfaceinfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceinfo;

    @Resource
    private UserService userService;


    /**
     * 添加 InterfaceInfo
     *
     * @param interfaceinfoAddRequest interfaceinfo 添加请求
     * @param request                 请求
     * @return {@link BaseResponse}<{@link Long}>
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/add")
    public BaseResponse<Long> addUserInterfaceinfo(@RequestBody UserInterfaceinfoAddRequest interfaceinfoAddRequest, HttpServletRequest request) {
        if (interfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoAddRequest, userInterfaceInfo);
        // 校验
        userInterfaceinfo.validInterfaceInfo(userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceinfo.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newPostId = userInterfaceInfo.getId();
        return ResultUtils.success(newPostId);
    }


    /**
     * 删除 interfaceinfo
     *
     * @param deleteRequest 删除请求
     * @param request       请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserInterfaceinfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo interfaceinfoById = userInterfaceinfo.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!interfaceinfoById.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceinfo.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 更新 InterfaceInfo
     *
     * @param interfaceinfoUpdateRequest InterfaceInfo 更新请求
     * @param request                    请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUserInterfaceinfo(@RequestBody UserInterfaceinfoUpdateRequest interfaceinfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceinfoUpdateRequest == null || interfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceinfo.validInterfaceInfo(userInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceinfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo interfaceinfoById = userInterfaceinfo.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!interfaceinfoById.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceinfo.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id 同上
     * @return {@link BaseResponse}<{@link UserInterfaceInfo}>
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceinfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userInterfaceinfo.getById(id));
    }

    /**
     * 列表接口信息
     *
     * @param interfaceinfoQueryRequest InterfaceInfo 查询请求
     * @return {@link BaseResponse}<{@link List}<{@link UserInterfaceInfo}>>
     */

    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceinfo(UserInterfaceinfoQueryRequest interfaceinfoQueryRequest) {
        UserInterfaceInfo interfaceInfo = new UserInterfaceInfo();
        if (interfaceinfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceInfo);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        List<UserInterfaceInfo> postList = userInterfaceinfo.list(queryWrapper);
        return ResultUtils.success(postList);
    }


    /**
     * 按页面列出 InterfaceInfo
     *
     * @param interfaceinfoQueryRequest InterfaceInfo 查询请求
     * @param request                   请求
     * @return {@link BaseResponse}<{@link Page}<{@link UserInterfaceInfo}>>
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list/page")
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceinfoByPage(UserInterfaceinfoQueryRequest interfaceinfoQueryRequest, HttpServletRequest request) {
        if (interfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo interfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceInfo);
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        String sortField = interfaceinfoQueryRequest.getSortField();
        String sortOrder = interfaceinfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> postPage = userInterfaceinfo.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(postPage);
    }

}
