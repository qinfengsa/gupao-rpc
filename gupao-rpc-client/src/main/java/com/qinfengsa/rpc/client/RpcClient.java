package com.qinfengsa.rpc.client;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import com.qinfengsa.rpc.common.dto.RpcRequest;
import com.qinfengsa.rpc.common.dto.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端 远程
 * @author: qinfengsa
 * @date: 2019/7/12 18:10
 */
@Slf4j
public class RpcClient {

    private String host;

    private int port;

    private RpcCodec rpcCodec;

    public RpcClient(String host, int port,RpcCodec rpcCodec) {
        this.host = host;
        this.port = port;
        this.rpcCodec = rpcCodec;
    }

    public RpcResponse send(RpcRequest request) {

        // 配置NIO线程池
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        final RpcClientHandler handler = new RpcClientHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>()  {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline =ch.pipeline();

                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

                            pipeline.addLast(new LengthFieldPrepender(4));


                            rpcCodec.addHandler(pipeline);

                            pipeline.addLast("handler",handler);
                        }
                    });
            // connect 发起连接请求 sync 等待连接
            ChannelFuture future = bootstrap.connect(host,port).sync();
            log.debug("{},客户端发起异步连接.........",Thread.currentThread().getName() );

            future.channel().writeAndFlush(request).sync();
            // 等待连接关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }
        return handler.getResponse();
    }
}
