package com.my.rpc.server.netty;

import com.alibaba.fastjson.JSONObject;
import com.my.rpc.protocol.RpcRequest;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;


/**
 * @Description TODO
 * @Date 2019/7/10 14:23
 * @Created by rogan.luo
 */
@AllArgsConstructor
public class NettyClient {

    private Channel channel;

    public void sendRequest(RpcRequest rpcRequest){
        String rpcRequestStr = JSONObject.toJSONString(rpcRequest);
        channel.writeAndFlush(rpcRequestStr);
    }
}
