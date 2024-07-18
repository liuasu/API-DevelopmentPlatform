package com.ls.provider;

import com.ls.common.service.InteriorUserInterfaceInfoService;
import com.ls.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InteriorUserInterfaceInfoServiceImpl implements InteriorUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 调用计数
     *
     * @param interfaceId
     * @param userId
     * @return
     */
    @Override
    public Boolean invokeCount(Long interfaceId, Long userId) {
        return userInterfaceInfoService.invokeCount(interfaceId, userId);
    }
}
