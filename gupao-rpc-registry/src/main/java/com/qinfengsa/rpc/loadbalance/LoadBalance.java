package com.qinfengsa.rpc.loadbalance;

import java.util.List;

/**
 * 负载均衡接口
 * @author: qinfengsa
 * @date: 2019/7/26 16:13
 */
public interface LoadBalance {

    /**
     * 负载均衡
     * @param addrList
     * @return
     */
    String selectAddr(List<String> addrList);
}
