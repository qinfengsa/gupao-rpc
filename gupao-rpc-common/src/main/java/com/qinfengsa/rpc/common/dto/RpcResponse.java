package com.qinfengsa.rpc.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应信息
 * @author: qinfengsa
 * @date: 2019/7/12 17:07
 */
@Data
public class RpcResponse implements Serializable {

    /**
     * 注册ID
     */
    private String requestId;


    /**
     * 返回结果
     */
    private Object result;
}
