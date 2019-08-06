package com.qinfengsa.rpc.common.serialization;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * MessagePack 序列化
 * @author: qinfengsa
 * @date: 2019/7/15 07:10
 */
@Slf4j
public class MsgPackCodec implements RpcCodec {

    @Override
    public void addHandler(ChannelPipeline pipeline) {
        // 编码器
        pipeline.addLast("encoder",new MsgPackEncoder());
        // 解码器
        pipeline.addLast("decoder",new MsgPackDecoder());

    }

    class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext,
                              ByteBuf byteBuf, List<Object> list) throws Exception {
            final int len = byteBuf.readableBytes();
            final byte[] bytes = new byte[len];

            byteBuf.getBytes(byteBuf.readerIndex(),bytes,0,len);
            MessagePack messagePack = new MessagePack();
            list.add(messagePack.read(bytes));
        }
    }

    class MsgPackEncoder extends MessageToByteEncoder<Object> {


        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext,
                              Object object, ByteBuf byteBuf) throws Exception {
            MessagePack messagePack = new MessagePack();
            byte[] bytes = messagePack.write(object);
            byteBuf.writeBytes(bytes);

        }
    }
}
