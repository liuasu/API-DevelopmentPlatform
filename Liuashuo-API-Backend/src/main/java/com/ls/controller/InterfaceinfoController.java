package com.ls.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.annotation.AuthCheck;
import com.ls.common.*;
import com.ls.constant.CommonConstant;
import com.ls.exception.BusinessException;
import com.ls.model.dto.interfaceinfo.InterfaceinfoAddRequest;
import com.ls.model.dto.interfaceinfo.InterfaceinfoInvokeRequest;
import com.ls.model.dto.interfaceinfo.InterfaceinfoQueryRequest;
import com.ls.model.dto.interfaceinfo.InterfaceinfoUpdateRequest;
import com.ls.model.entity.InterfaceInfo;
import com.ls.model.entity.User;
import com.ls.service.InterfaceInfoService;
import com.ls.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/interfaceinfo")
@Slf4j
public class InterfaceinfoController {

    @Resource
    private InterfaceInfoService Interfaceinfo;

    @Resource
    private UserService userService;

    // region 增删改查


    /**
     * 添加 InterfaceInfo
     *
     * @param interfaceinfoAddRequest interfaceinfo 添加请求
     * @param request                 请求
     * @return {@link BaseResponse}<{@link Long}>
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceinfo(@RequestBody InterfaceinfoAddRequest interfaceinfoAddRequest, HttpServletRequest request) {
        if (interfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoAddRequest, interfaceInfo);
        // 校验
        Interfaceinfo.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = Interfaceinfo.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newPostId = interfaceInfo.getId();
        return ResultUtils.success(newPostId);
    }


    /**
     * 删除 interfaceinfo
     *
     * @param deleteRequest 删除请求
     * @param request       请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceinfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo interfaceinfoById = Interfaceinfo.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!interfaceinfoById.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = Interfaceinfo.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 更新 InterfaceInfo
     *
     * @param interfaceinfoUpdateRequest InterfaceInfo 更新请求
     * @param request                    请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceinfo(@RequestBody InterfaceinfoUpdateRequest interfaceinfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceinfoUpdateRequest == null || interfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoUpdateRequest, interfaceInfo);
        // 参数校验
        Interfaceinfo.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceinfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo interfaceinfoById = Interfaceinfo.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!interfaceinfoById.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = Interfaceinfo.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceinfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(Interfaceinfo.getById(id));
    }

    /**
     * 列表接口信息
     *
     * @param interfaceinfoQueryRequest InterfaceInfo 查询请求
     * @return {@link BaseResponse}<{@link List}<{@link InterfaceInfo}>>
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceinfo(InterfaceinfoQueryRequest interfaceinfoQueryRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (interfaceinfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceInfo);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        List<InterfaceInfo> postList = Interfaceinfo.list(queryWrapper);
        return ResultUtils.success(postList);
    }


    /**
     * 按页面列出 InterfaceInfo
     *
     * @param interfaceinfoQueryRequest InterfaceInfo 查询请求
     * @param request                   请求
     * @return {@link BaseResponse}<{@link Page}<{@link InterfaceInfo}>>
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceinfoByPage(InterfaceinfoQueryRequest interfaceinfoQueryRequest, HttpServletRequest request) {
        if (interfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoQueryRequest, interfaceInfo);
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        String sortField = interfaceinfoQueryRequest.getSortField();
        String sortOrder = interfaceinfoQueryRequest.getSortOrder();
        String content = interfaceinfoQueryRequest.getContent();
        // content 需支持模糊搜索
        interfaceinfoQueryRequest.setContent(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> postPage = Interfaceinfo.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(postPage);
    }

    // endregion

    /**
     * 发布接口
     *
     * @param request   请求
     * @param idRequest ID 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/publish")
    public BaseResponse<Boolean> publishInterfaceinfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = Interfaceinfo.publishInterface(id);
        return ResultUtils.success(result);
    }

    /**
     * 停止接口
     *
     * @param request   请求
     * @param idRequest ID 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceinfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        Long id = idRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = Interfaceinfo.offlineInterface(id);
        return ResultUtils.success(result);
    }

    /**
     * 接口调用
     *
     * @param request                    请求
     * @param interfaceinfoInvokeRequest interfaceinfo 调用请求
     * @return {@link BaseResponse}<{@link Object}>
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> interfaceinfoInvoke(@RequestBody InterfaceinfoInvokeRequest interfaceinfoInvokeRequest, HttpServletRequest request) {
        if (interfaceinfoInvokeRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceinfoInvokeRequest.getId();
        String requestParams = interfaceinfoInvokeRequest.getRequestParams();
        if (id == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (requestParams == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Object obj = Interfaceinfo.invokeInterface(interfaceinfoInvokeRequest,loginUser);
        return ResultUtils.success(obj);
    }
}
