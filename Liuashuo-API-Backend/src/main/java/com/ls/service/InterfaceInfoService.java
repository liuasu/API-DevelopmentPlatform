package com.ls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.model.entity.InterfaceInfo;


public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean isAdd);
}
