package com.ls.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.common.model.entity.UserInterfaceInfo;

/**
* @author 32093
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-06-02 16:15:01
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo interfaceInfo, boolean b);

    /**
     * 调用计数
     *
     * @param interfaceId 接口 ID
     * @param userId      用户 ID
     * @return {@link Boolean}
     */
    Boolean invokeCount(Long interfaceId,Long userId);
}
