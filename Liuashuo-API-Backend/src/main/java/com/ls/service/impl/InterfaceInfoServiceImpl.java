package com.ls.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.common.ErrorCode;
import com.ls.exception.BusinessException;
import com.ls.mapper.InterfaceInfoMapper;
import com.ls.model.entity.InterfaceInfo;
import com.ls.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

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
            if (StringUtils.isAnyBlank(name, description, url, requestHeader, responseHeader, method) ) {
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
}




