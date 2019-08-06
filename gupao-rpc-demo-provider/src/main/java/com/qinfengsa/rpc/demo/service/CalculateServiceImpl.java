package com.qinfengsa.rpc.demo.service;

import com.qinfengsa.rpc.annotation.RpcService;
import com.qinfengsa.rpc.demo.api.CalculateService;
import org.springframework.stereotype.Service;

/**
 * @author: qinfengsa
 * @date: 2019/7/14 07:35
 */
@RpcService(CalculateService.class)
public class CalculateServiceImpl implements CalculateService {
    @Override
    public int add(int a, int b) throws Exception {

        return a + b;
    }

    @Override
    public int subtract(int a, int b) throws Exception {
        return a - b;
    }

    @Override
    public int multiply(int a, int b) throws Exception {
        return a * b;
    }

    @Override
    public int divide(int a, int b) throws Exception {
        return a / b;
    }
}
