package com.my.rpc.server.proxy;

import com.my.rpc.entity.RpcReponse;
import com.my.rpc.handler.NettyClientHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.rmi.Remote;

/**
 * @Description TODO
 * @Date 2019/7/10 16:22
 * @Created by rogan.luo
 */
public class ProcessHandler extends SimpleChannelInboundHandler<RpcReponse> {

    /*public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String str = new String(bytes,"utf-8");
        NettyClientHandler.doResponse(channel, JSONObject.parseObject(str, RpcReponse.class));

    }*/

    @Override
    protected void messageReceived(ChannelHandlerContext chx, RpcReponse rpcReponse) throws Exception {
//        NettyClientHandler.doResponse(rpcReponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
