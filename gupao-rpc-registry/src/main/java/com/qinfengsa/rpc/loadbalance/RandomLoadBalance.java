package com.qinfengsa.rpc.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @author: qinfengsa
 * @date: 2019/7/26 16:20
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> addrList) {
        int size = addrList.size();
        Random random = new Random();
        int index = random.nextInt(size);
        return addrList.get(index);
    }
}
