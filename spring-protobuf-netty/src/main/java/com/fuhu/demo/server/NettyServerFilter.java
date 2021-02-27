package com.fuhu.demo.server;

import com.fuhu.demo.protobuf.MyBaseProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
  * 
 * @Title: HelloServerInitializer
 * @Description: Netty 服务端过滤器
 * @Version:1.0.0  
 * @author pancm
 * @date 2017年10月8日
  */
@Component
public class NettyServerFilter extends ChannelInitializer<SocketChannel> {
	
	@Autowired
	private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //获取管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(10000, 0, 4, 0, 4));
        pipeline.addLast(new ProtobufDecoder(MyBaseProto.BaseProto.getDefaultInstance()));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new ProtobufEncoder());
        //处理类
        pipeline.addLast(new NettyServerHandler());
    }
 }
