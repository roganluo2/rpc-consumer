package com.my.rpc.server.proxy;

import com.my.rpc.entity.RpcReponse;
import com.my.rpc.entity.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RomoteMsgSend extends SimpleChannelInboundHandler<RpcReponse>  {

    private String ip;
    private int port;

    public RomoteMsgSend(String ip, int port) {

        this.ip = ip;
        this.port = port;
    }

    private Object result;


    public Object send(RpcRequest rpcRequest){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingResolver(null)));
                        pipeline.addLast(this);
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            //等待客户端连接端口关闭
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcRequest).sync();
            if(rpcRequest != null) {
                channel.closeFuture().sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
        return result;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RpcReponse rpcReponse) throws Exception {
        this.result=rpcReponse;
    }
}
