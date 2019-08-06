package com.qinfengsa.rpc.loadbalance;

import java.util.List;
import java.util.Objects;

/**
 * @author: qinfengsa
 * @date: 2019/7/26 16:16
 */
public  abstract class AbstractLoadBalance implements LoadBalance {


    @Override
    public String selectAddr(List<String> addrList) {
        if (Objects.isNull(addrList) || addrList.size() == 0) {
            return null;
        }
        if (addrList.size() == 1) {
            return addrList.get(0);
        }
        return doSelect(addrList);
    }

    /**
     * 存在多个地址时，负载均衡
     * @param addrList
     * @return
     */
    protected abstract String doSelect(List<String> addrList);
}
