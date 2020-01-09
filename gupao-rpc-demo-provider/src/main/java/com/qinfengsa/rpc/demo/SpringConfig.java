package com.qinfengsa.rpc.demo;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import com.qinfengsa.rpc.common.serialization.HessianCodec;
import com.qinfengsa.rpc.server.RpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * @author: qinfengsa
 * @date: 2019/7/25 17:00
 */
@Configuration
@PropertySource("classpath:RpcConfig.properties")
@ComponentScan(basePackages = "com.qinfengsa.rpc.demo.service")
public class SpringConfig {

    @Bean(name="rpcServer")
    public RpcServer rpcServer(){
        return new RpcServer(8080 );
    }
}
