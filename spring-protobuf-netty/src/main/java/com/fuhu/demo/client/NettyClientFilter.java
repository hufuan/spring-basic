package com.fuhu.demo.client;

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
* @Title: NettyClientFilter
* @Description: Netty客户端 过滤器
* @Version:1.0.0  
* @author pancm
* @date 2017年10月8日
 */
@Component
public class NettyClientFilter extends ChannelInitializer<SocketChannel> {

	@Autowired
	private NettyClientHandler nettyClientHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //获取管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(10000, 0, 4, 0, 4));
        pipeline.addLast(new ProtobufDecoder(MyBaseProto.BaseProto.getDefaultInstance()));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new ProtobufEncoder());
        //处理类
        pipeline.addLast(new NettyClientHandler());
    }
}
