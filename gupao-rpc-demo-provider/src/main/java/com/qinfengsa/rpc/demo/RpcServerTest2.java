package com.qinfengsa.rpc.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: qinfengsa
 * @date: 2019/7/26 17:35
 */
@Slf4j
public class RpcServerTest2 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig2.class);
        context.start();
        log.info("application start");
    }
}
