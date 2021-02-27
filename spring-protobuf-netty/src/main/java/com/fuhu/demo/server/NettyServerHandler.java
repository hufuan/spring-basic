package com.fuhu.demo.server;

import com.fuhu.demo.protobuf.MyBaseProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Service;

@Service("nettyServerHandler")
class NettyServerHandler extends SimpleChannelInboundHandler<MyBaseProto.BaseProto> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyBaseProto.BaseProto msg) throws Exception {
        System.out.println("RESPONSE--------" + msg.toString());

        MyBaseProto.BaseProto.Builder builder = MyBaseProto.BaseProto.newBuilder(msg);
        builder.setCode(msg.getCode() + 100);
        ctx.writeAndFlush(builder.build());
    }
}