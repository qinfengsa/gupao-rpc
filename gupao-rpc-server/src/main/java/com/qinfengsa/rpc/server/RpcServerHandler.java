package com.qinfengsa.rpc.server;

import com.qinfengsa.rpc.common.dto.RpcRequest;
import com.qinfengsa.rpc.common.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * Rpc 请求 处理类
 * @author: qinfengsa
 * @date: 2019/7/12 18:13
 */
@Slf4j
public class RpcServerHandler extends ChannelInboundHandlerAdapter {


    private final Map<String, Object> handlerMap ;

    public RpcServerHandler(Map<String, Object> handlerMap ) {
        this.handlerMap = handlerMap;
    }

    /**
     * 收到客户端消息，自动触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();

        // rpc 请求
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;
            Class clazz = Class.forName(request.getClassName());
            // 获取 method
            Method method = clazz.getMethod(request.getMethodName(),request.getParameterTypes());
            // 调用方法
            //method.invoke()
            Object instance = handlerMap.get(request.getClassName());
            if (Objects.nonNull(instance)) {
                result = method.invoke(instance,request.getParameters());
            }
        }
        RpcResponse response = new RpcResponse();
        response.setResult(result);
        ctx.writeAndFlush(response);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 发生异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(),cause);
        ctx.close();
    }
}
