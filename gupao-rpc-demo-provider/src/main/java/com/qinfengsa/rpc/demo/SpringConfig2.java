package com.qinfengsa.rpc.demo;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import com.qinfengsa.rpc.common.serialization.HessianCodec;
import com.qinfengsa.rpc.server.RpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: qinfengsa
 * @date: 2019/7/26 17:34
 */
@Configuration
@ComponentScan(basePackages = "com.qinfengsa.rpc")
public class SpringConfig2 {

    @Bean(name="rpcServer")
    public RpcServer rpcServer(){
        RpcCodec rpcCodec = new HessianCodec();
        return new RpcServer(8081,rpcCodec);
    }
}
