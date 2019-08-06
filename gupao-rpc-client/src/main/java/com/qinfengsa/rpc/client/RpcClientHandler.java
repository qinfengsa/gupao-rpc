package com.qinfengsa.rpc.client;

import com.qinfengsa.rpc.common.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端 处理
 * @author: qinfengsa
 * @date: 2019/7/13 07:41
 */
@Slf4j
@Data
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    private RpcResponse response;

    /**
     * 服务端返回信息时，通过channelRead接收返回的信息，
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = (RpcResponse) msg;

        ctx.close();
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
