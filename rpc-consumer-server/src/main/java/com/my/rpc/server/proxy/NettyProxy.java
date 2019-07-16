package com.my.rpc.server.proxy;

import com.my.rpc.handler.ClientHandler;
import com.my.rpc.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Proxy;

/**
 * @Description TODO
 * @Date 2019/7/10 14:21
 * @Created by rogan.luo
 */
@Data
public class NettyProxy {

    public NettyProxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private String ip;
    private int port;

    private Channel channel;

    public <T>T getProxyClient(Class<T> tClass)
    {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{tClass}, new NettyClientHandler(channel));
    }

    public void init(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ProcessHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            //等待客户端连接端口关闭
            channel = channelFuture.channel();

//            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
