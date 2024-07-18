package com.ls.provider;

import com.ls.common.ErrorCode;
import com.ls.common.model.entity.InterfaceInfo;
import com.ls.common.service.InteriorInterfaceInfoService;
import com.ls.exception.BusinessException;
import com.ls.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InteriorInterfaceInfoServiceImpl implements InteriorInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 数据库中查询模拟接口是否存在，以及请求方法是否匹配
     *
     * @param path   路径
     * @param method 方法
     * @return {@link InterfaceInfo}
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyBlank(path, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.lambdaQuery()
                .like(InterfaceInfo::getUrl, path)
                .eq(InterfaceInfo::getMethod, method)
                .one();
    }
}
