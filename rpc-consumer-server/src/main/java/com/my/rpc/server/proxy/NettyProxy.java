package com.my.rpc.server.proxy;

import com.my.rpc.discovery.IServiceDiscovery;
import com.my.rpc.discovery.ServiceDiscoveryWithZK;
import com.my.rpc.handler.ClientHandler;
import com.my.rpc.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
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

    private IServiceDiscovery serviceDiscovery=new ServiceDiscoveryWithZK();


    public <T>T getProxyClient(Class<T> tClass, String version )
    {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{tClass}, new NettyClientHandler(serviceDiscovery,version));
    }

}
