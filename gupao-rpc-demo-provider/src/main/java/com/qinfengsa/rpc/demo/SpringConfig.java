package com.qinfengsa.rpc.demo;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import com.qinfengsa.rpc.common.serialization.HessianCodec;
import com.qinfengsa.rpc.server.RpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: qinfengsa
 * @date: 2019/7/25 17:00
 */
@Configuration
@ComponentScan(basePackages = "com.qinfengsa.rpc")
public class SpringConfig {

    @Bean(name="rpcServer")
    public RpcServer rpcServer(){
        RpcCodec rpcCodec = new HessianCodec();
        return new RpcServer(8080,rpcCodec);
    }
}
