package com.ls.common.service;


import com.ls.common.model.entity.InterfaceInfo;

public interface InteriorInterfaceInfoService {

    /**
     * 数据库中查询模拟接口是否存在，以及请求方法是否匹配
     *
     * @param path   路径
     * @param method 方法
     * @return {@link InterfaceInfo}
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
