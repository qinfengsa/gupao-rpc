package com.qinfengsa.rpc.common.serialization;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.qinfengsa.rpc.common.codec.RpcCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Hessian 序列化
 * @author: qinfengsa
 * @date: 2019/7/15 07:43
 */
@Slf4j
public class HessianCodec implements RpcCodec {


    @Override
    public void addHandler(ChannelPipeline pipeline) {

        // 编码器
        pipeline.addLast("encoder",new HessianEncoder());
        // 解码器
        pipeline.addLast("decoder",new HessianDecoder());


    }


    class HessianEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Object object,
                      ByteBuf byteBuf) throws Exception {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream())  {
                HessianOutput output = new HessianOutput(bos);
                output.writeObject(object);

                byte[] bytes = bos.toByteArray();
                byteBuf.writeBytes(bytes);
            }

        }
    }

    class HessianDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext,
                ByteBuf byteBuf, List<Object> list) throws Exception {
            final int len = byteBuf.readableBytes();
            final byte[] bytes = new byte[len];
            byteBuf.readBytes(bytes);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
                HessianInput input = new HessianInput(bis);
                list.add(input.readObject());
            }

        }
    }


}
