package com.ls.service.impl;

import cn.ls.client.LiuAShuoClient;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ls.common.ErrorCode;
import com.ls.enums.InterfaceInfoStatuEnum;
import com.ls.exception.BusinessException;
import com.ls.mapper.InterfaceInfoMapper;
import com.ls.model.dto.interfaceinfo.InterfaceinfoInvokeRequest;
import com.ls.model.entity.InterfaceInfo;
import com.ls.model.entity.User;
import com.ls.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Resource
    private LiuAShuoClient liuAShuoClient;


    /**
     * 有效接口信息
     *
     * @param interfaceInfo 接口信息
     * @param isAdd         是添加
     */
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean isAdd) {
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();

        // 创建时，所有参数必须非空
        if (isAdd) {
            if (StringUtils.isAnyBlank(name, description, url, requestHeader, responseHeader, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }


    /**
     * 下线
     *
     * @param id 同上
     * @return {@link Boolean}
     */
    @Override
    public Boolean offlineInterface(long id) {
        // 判断是否存在
        InterfaceInfo interfaceinfoById = this.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        interfaceinfoById.setId(id);
        interfaceinfoById.setStatus(InterfaceInfoStatuEnum.OFFLINE.getCode());
        return this.updateById(interfaceinfoById);
    }

    /**
     * 发布
     *
     * @param id 同上
     * @return {@link Boolean}
     */
    @Override
    public Boolean publishInterface(Long id) {
        // 判断是否存在
        InterfaceInfo interfaceinfoById = this.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User user = new User();
        user.setUserName("aaaaa");
        String post = liuAShuoClient.ByRestfulPost(user);
        if (StringUtils.isBlank(post)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "请求接口失败");
        }
        interfaceinfoById.setId(id);
        interfaceinfoById.setStatus(InterfaceInfoStatuEnum.PUBLISH.getCode());
        return this.updateById(interfaceinfoById);
    }

    /**
     * 调用接口
     *
     * @param interfaceinfoInvokeRequest interfaceinfo 调用请求
     * @param loginUser                  登录用户
     * @return {@link Object}
     */
    @Override
    public Object invokeInterface(InterfaceinfoInvokeRequest interfaceinfoInvokeRequest, User loginUser) {
        Long id = interfaceinfoInvokeRequest.getId();
        String requestParams = interfaceinfoInvokeRequest.getRequestParams();
        InterfaceInfo info = this.getById(id);
        if(info == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if(info.getStatus()!=InterfaceInfoStatuEnum.PUBLISH.getCode()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口未启用");
        }
        Gson gson = new Gson();
        Object params = gson.fromJson(requestParams, Object.class);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        LiuAShuoClient shuoClient = new LiuAShuoClient(accessKey,secretKey,info.getUrl());
        return shuoClient.ByRestfulPost(params);
    }
}




