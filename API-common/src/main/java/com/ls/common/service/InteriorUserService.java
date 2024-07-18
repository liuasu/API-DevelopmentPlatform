package com.ls.common.service;

import com.ls.common.model.entity.User;

public interface InteriorUserService {
    /**
     * 数据库中查是否已分配给用户秘钥（ak、sk是否合法）
     *
     * @param accessKey 访问密钥
     * @return {@link Boolean}
     */
    User getInvokeUser(String accessKey);
}
