package com.ls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.common.model.entity.InterfaceInfo;
import com.ls.common.model.entity.User;
import com.ls.model.dto.interfaceinfo.InterfaceinfoInvokeRequest;



public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean isAdd);

    /**
     * 下线
     *
     * @param id 同上
     * @return {@link Boolean}
     */
    Boolean offlineInterface(long id);

    /**
     * 发布
     *
     * @param id 同上
     * @return {@link Boolean}
     */
    Boolean publishInterface(Long id);

    /**
     * 调用接口
     *
     * @param interfaceinfoInvokeRequest interfaceinfo 调用请求
     * @param loginUser                  登录用户
     * @return {@link Object}
     */
    Object invokeInterface(InterfaceinfoInvokeRequest interfaceinfoInvokeRequest, User loginUser);
}
