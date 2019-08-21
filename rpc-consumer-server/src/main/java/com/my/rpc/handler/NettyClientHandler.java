package com.my.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.my.rpc.entity.RpcReponse;
import com.my.rpc.entity.RpcRequest;
import com.my.rpc.server.proxy.RomoteMsgSend;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TODO
 * @Date 2019/7/3 14:36
 * @Created by rogan.luo
 */
@Data
public class NettyClientHandler implements InvocationHandler {
    private String version;

    private String ip;

    private int port;

    public NettyClientHandler(String version, String ip, int port) {
        this.version = version;
        this.ip = ip;
        this.port = port;
    }





    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        if(method.getName().equals("toString"))
        {
            return args.toString();
        }
        rpcRequest.setParams(args);
        rpcRequest.setVersion(version);
        String id = UUID.randomUUID().toString();
        rpcRequest.setId(id);
        System.out.println(JSONObject.toJSONString(rpcRequest));
       return new RomoteMsgSend(ip, port).send(rpcRequest);
    }


}
