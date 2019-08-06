package com.qinfengsa.rpc.common.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 请求信息
 * @author: qinfengsa
 * @date: 2019/7/12 17:06
 */
@Data
public class RpcRequest implements Serializable {

    /**
     * 注册ID
     */
    private String requestId;

    /**
     * 请求Class 类名
     */
    private String className;

    /**
     * 请求method 方法名
     */
    private String methodName;

    /**
     * 形参列表 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 实参列表 参数列表
     */
    private Object[] parameters;
}
