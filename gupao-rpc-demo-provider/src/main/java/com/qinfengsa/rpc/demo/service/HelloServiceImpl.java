package com.qinfengsa.rpc.demo.service;

import com.qinfengsa.rpc.annotation.RpcService;
import com.qinfengsa.rpc.demo.api.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: qinfengsa
 * @date: 2019/7/14 07:35
 */
@RpcService(HelloService.class)
@Slf4j
public class HelloServiceImpl implements HelloService {


    @Override
    public String sayHello(String name) {
        log.info("调用HelloService ->{}",name);
        return "Hello! " + name;
    }
}
