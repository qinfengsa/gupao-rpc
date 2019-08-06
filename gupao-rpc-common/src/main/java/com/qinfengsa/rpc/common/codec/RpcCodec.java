package com.qinfengsa.rpc.common.codec;

import io.netty.channel.ChannelPipeline;

/**
 * @author: qinfengsa
 * @date: 2019/7/25 16:35
 */
public interface RpcCodec {

    /**
     * 为Netty添加编码和解码
     * @param pipeline
     */
    void addHandler(ChannelPipeline pipeline);
}
