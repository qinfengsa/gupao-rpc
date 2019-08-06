package com.qinfengsa.rpc.demo;

import com.qinfengsa.rpc.demo.api.CalculateService;
import com.qinfengsa.rpc.demo.api.HelloService;
import com.qinfengsa.rpc.client.RpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author: qinfengsa
 * @date: 2019/7/14 07:21
 */
@Slf4j
public class RpcClientTest {

    public static void main(String[] args) {
        HelloService helloService = new RpcProxy().create(HelloService.class);

        String result = helloService.sayHello("World");
        System.out.println("HelloService :"+result);
        log.info("HelloService : {}",result);

        CalculateService calculateService = new RpcProxy().create(CalculateService.class);

        try {
            int sum = calculateService.add(1,2);
            log.info("sum:{}",sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
