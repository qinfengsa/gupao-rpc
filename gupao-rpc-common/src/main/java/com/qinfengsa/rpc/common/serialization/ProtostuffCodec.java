package com.qinfengsa.rpc.common.serialization;

import com.qinfengsa.rpc.common.codec.RpcCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.List;

import static io.protostuff.runtime.RuntimeSchema.getSchema;

/**
 * @author: qinfengsa
 * @date: 2019/7/15 10:42
 */
public class ProtostuffCodec implements RpcCodec {


    private Class<?> clazz;

    public ProtostuffCodec(Class<?> clazz) {

        this.clazz = clazz;
    }

    @Override
    public void addHandler(ChannelPipeline pipeline) {

        // 编码器
        pipeline.addLast("encoder",new ProtostufEncoder());
        // 解码器
        pipeline.addLast("decoder",new ProtostufDecoder());

    }

    class ProtostufEncoder extends MessageToByteEncoder<Object> {
        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Object object,
                              ByteBuf byteBuf) throws Exception {


            byte[] bytes = serializer(object);

            byteBuf.writeBytes(bytes);

        }



    }

    class ProtostufDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext,
                              ByteBuf byteBuf, List<Object> list) throws Exception {
            final int len = byteBuf.readableBytes();
            final byte[] bytes = new byte[len];
            byteBuf.readBytes(bytes);
            Object obj = deserializer(bytes,clazz);
            list.add(obj);


        }
    }


    public static <T> byte[] serializer(T o) {
        Schema schema = RuntimeSchema.getSchema(o.getClass());
        return ProtobufIOUtil.toByteArray(o, schema, LinkedBuffer.allocate(256));
    }
    public static <T> T deserializer(byte[] bytes, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            Schema schema = RuntimeSchema.getSchema(obj.getClass());
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
