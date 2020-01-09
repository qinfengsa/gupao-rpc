package com.qinfengsa.rpc.server;

import com.qinfengsa.rpc.annotation.RpcService;
import com.qinfengsa.rpc.common.codec.RpcCodec;
import com.qinfengsa.rpc.common.serialization.HessianCodec;
import com.qinfengsa.rpc.common.serialization.MsgPackCodec;
import com.qinfengsa.rpc.common.serialization.ProtostuffCodec;
import com.qinfengsa.rpc.common.util.RpcConfig;
import com.qinfengsa.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


/**
 * Rpc Server 服务端
 * @author: qinfengsa
 * @date: 2019/7/12 18:12
 */
@Slf4j
public class RpcServer implements ApplicationContextAware, InitializingBean {

    /**
     * 端口号
     */
    private int port;

    private Map<String, Object> handlerMap = new HashMap<>(16);


    private Map<String,RpcCodec> rpcCodecMap = new HashMap<>(16);
    {
        rpcCodecMap.put("Hessian",new HessianCodec());
        rpcCodecMap.put("MsgPack",new MsgPackCodec());
        //rpcCodecMap.put("Protostuff",new ProtostuffCodec());
    }

    /**
     * 编解码器
     */
    private RpcCodec rpcCodec;


    /**
     * 注册
     */
    private ServiceRegistry serviceRegistry = new ServiceRegistry();


    public RpcServer(int port) {
        this.port = port;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Environment environment = context.getEnvironment();
        String codecType = environment.getProperty(RpcConfig.CODEC_NAME);
        this.rpcCodec = this.rpcCodecMap.get(codecType);
        Map<String, Object> serviceBeanMap = context.getBeansWithAnnotation(RpcService.class);

        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {

                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                handlerMap.put(serviceName,serviceBean);
                rpcServiceRegistry(serviceName);
            }
        }
    }

    /**
     * 服务注册
     * @param serviceName 服务名
     */
    private void rpcServiceRegistry(String serviceName) {
        String serviceAddress = getAddress() + ":"  + this.port;
        serviceRegistry.registry(serviceName,serviceAddress);
    }


    /**
     * 获取本机的ip地址
     * @return 本机的ip地址
     */
    private String getAddress() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddress.getHostAddress();
    }


    /**
     * Spring初始化后调用
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 配置服务端的 NIO 线程池,用于网络事件处理，实质上他们就是 Reactor 线程组

        // BOSS线程 用于服务端接受客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Worker线程 用于进行 SocketChannel 网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap 是 Netty 用于启动 NIO 服务端的辅助启动类，用于降低开发难度
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 针对主线程的配置 分配线程最大数量 128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 针对子线程的配置 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer () {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();

                            // LengthFieldBasedFrameDecoder LengthFieldPrepender
                            // 解决TCP的粘包和半包问题
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

                            pipeline.addLast(new LengthFieldPrepender(4));

                            rpcCodec.addHandler(pipeline);

                            pipeline.addLast(new RpcServerHandler(handlerMap));
                        }
                    });
            // bind绑定端口，sync同步等待
            ChannelFuture future = bootstrap.bind(port).sync();
            if (log.isInfoEnabled()) {
                log.info("{},服务器开始监听端口，等待客户端连接",Thread.currentThread().getName() );
            }
            // 等待端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);

        } finally {
            // 关闭线程池
            // bossGroup.shutdownGracefully();
            // workerGroup.shutdownGracefully();
        }
    }
}
