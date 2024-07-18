package com.ls.common.service;

public interface InteriorUserInterfaceInfoService {

    /**
     * 调用计数
     *
     * @param interfaceId 接口 ID
     * @param userId      用户 ID
     * @return {@link Boolean}
     */
    Boolean invokeCount(Long interfaceId, Long userId);
}
