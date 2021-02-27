package com.fuhu.demo.client;

import com.fuhu.demo.protobuf.MyBaseProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Service;

@Service("nettyClientHandler")
class NettyClientHandler extends SimpleChannelInboundHandler<MyBaseProto.BaseProto> {

    //接受服务端发来的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyBaseProto.BaseProto msg) throws Exception {
        System.out.println("server response ： " + msg.toString());
    }
    //与服务器建立连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //给服务器发消息

        //发送5次消息
        for (int i = 0; i < 5; i++) {
            MyBaseProto.BaseProto.Builder builder = MyBaseProto.BaseProto.newBuilder();
            builder.setCode(i);
            builder.setMsg("msg" + i);
            MyBaseProto.Body.Builder body = MyBaseProto.Body.newBuilder();
            body.addL(String.valueOf(i * 10));
            body.addL(String.valueOf(i * i));
            MyBaseProto.Type type = MyBaseProto.Type.ONE;
            body.setType(type);
            builder.setResult(body);

            ctx.channel().writeAndFlush(builder.build());
        }
    }
}
