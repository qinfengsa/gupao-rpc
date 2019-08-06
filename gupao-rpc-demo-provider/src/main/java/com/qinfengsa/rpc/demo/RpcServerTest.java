package com.qinfengsa.rpc.demo;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import com.qinfengsa.rpc.common.serialization.HessianCodec;
import com.qinfengsa.rpc.server.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * @author: qinfengsa
 * @date: 2019/7/14 08:47
 */
@Slf4j
public class RpcServerTest {

    public static void main(String[] args) {
        /*ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");


        RpcCodec rpcCodec = new HessianCodec();

        RpcServer server = new RpcServer(8080,rpcCodec);

        server.setApplicationContext(context);*/

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        context.start();
        log.info("application start");
        //server.start();
    }
}
