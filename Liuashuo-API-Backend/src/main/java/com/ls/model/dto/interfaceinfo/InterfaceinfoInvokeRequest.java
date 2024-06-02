package com.ls.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求实体
 */
@Data
public class InterfaceinfoInvokeRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 接口调用请求参数
     */
    private String requestParams;


    private static final long serialVersionUID = 1L;
}