package com.my.rpc.server.proxy;

import com.alibaba.fastjson.JSONObject;
import com.my.rpc.handler.NettyClientHandler;
import com.my.rpc.protocol.RpcReponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description TODO
 * @Date 2019/7/10 16:22
 * @Created by rogan.luo
 */
public class ProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String str = new String(bytes,"utf-8");
        NettyClientHandler.doResponse(channel, JSONObject.parseObject(str, RpcReponse.class));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
