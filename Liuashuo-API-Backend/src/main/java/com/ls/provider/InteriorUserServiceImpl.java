package com.ls.provider;

import com.ls.common.ErrorCode;
import com.ls.common.model.entity.User;
import com.ls.common.service.InteriorUserService;
import com.ls.exception.BusinessException;
import com.ls.mapper.UserMapper;
import com.ls.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


@DubboService
public class InteriorUserServiceImpl implements InteriorUserService {

    @Resource
    private UserService userService;

    /**
     * 数据库中查是否已分配给用户秘钥（ak、sk是否合法）
     *
     * @param accessKey 访问密钥
     * @return {@link User}
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.lambdaQuery()
                .eq(User::getAccessKey, accessKey)
                .one();
    }
}
