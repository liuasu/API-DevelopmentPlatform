package com.ls.enums;

import lombok.Getter;

/**
 * 接口状态
 *
 * @author liuashuo
 */
@Getter
public enum InterfaceInfoStatuEnum {

    OFFLINE(0, "关闭"),
    PUBLISH(1, "启用");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    InterfaceInfoStatuEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
