package com.qinfengsa.rpc.client;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import com.qinfengsa.rpc.common.dto.RpcRequest;
import com.qinfengsa.rpc.common.dto.RpcResponse;
import com.qinfengsa.rpc.common.serialization.HessianCodec;
import com.qinfengsa.rpc.registry.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Rpc代理，通过代理访问远程服务
 * @author: qinfengsa
 * @date: 2019/7/12 18:10
 */
public class RpcProxy  implements InvocationHandler {

    private String serverAddress;

    private ServiceDiscovery serviceDiscovery = new ServiceDiscovery();


    public <T> T create(Class<?> interfaceClass) {
        Class<?> [] interfaces = interfaceClass.isInterface() ?
                new Class[]{interfaceClass} :
                interfaceClass.getInterfaces();
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                interfaces,this);
    }





    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        String serviceName = method.getDeclaringClass().getName();
        request.setClassName(serviceName);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);


        String address = serviceDiscovery.discovery(serviceName);
        String[] urls = address.split(":");
        String host = urls[0];
        int port = Integer.valueOf(urls[1]);
        RpcCodec rpcCodec = new HessianCodec();
        RpcResponse rpcResponse = new RpcClient(host,port,rpcCodec).send(request);
        return rpcResponse.getResult();
    }
}
