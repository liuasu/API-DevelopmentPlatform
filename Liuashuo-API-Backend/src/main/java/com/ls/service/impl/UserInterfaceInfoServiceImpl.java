package com.ls.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.common.ErrorCode;
import com.ls.common.model.entity.UserInterfaceInfo;
import com.ls.exception.BusinessException;
import com.ls.mapper.UserInterfaceInfoMapper;
import com.ls.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 32093
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2024-06-02 16:15:01
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    @Override
    public void validInterfaceInfo(UserInterfaceInfo interfaceInfo, boolean isAdd) {
        Long userId = interfaceInfo.getUserId();
        Long interfaceInfoId = interfaceInfo.getInterfaceInfoId();
        Integer totalNum = interfaceInfo.getTotalNum();
        // 创建时，所有参数必须非空
        if (isAdd) {
            if (userId == 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (interfaceInfoId == 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (totalNum == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "默认调用次数为空");
        }
    }


    /**
     * 调用计数
     *
     * @param interfaceId 接口 ID
     * @param userId      用户 ID
     * @return {@link Boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean invokeCount(Long interfaceId, Long userId) {
        if (interfaceId == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userId == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return lambdaUpdate()
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceId)
                .eq(UserInterfaceInfo::getUserId, userId)
                .gt(UserInterfaceInfo::getLeftNum, 0)
                .setSql("totalNum = totalNum + 1, leftNum = leftNum - 1")
                .update();
    }
}




