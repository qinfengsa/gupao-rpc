package com.qinfengsa.rpc.demo.api;

/**
 * 服务端 service接口
 * @author: qinfengsa
 * @date: 2019/7/14 07:26
 */
public interface HelloService {

    /**
     * 服务端 api
     * @param name
     * @return
     */
    String sayHello(String name);
}
