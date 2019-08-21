package com.my.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.my.rpc.entity.RpcReponse;
import com.my.rpc.entity.RpcRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
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
    private Channel channel;
    private String version;

    public NettyClientHandler(Channel channel, String version)
    {
        this.channel = channel;
        this.version = version;
    }

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private static Map<String, Condition> reqMap = new HashMap<String, Condition>();

    private static Map<String, Object> resMap = new HashMap<String, Object>();

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
        lock.lock();

//        new NettyClient(channel).sendRequest(rpcRequest);
        channel.writeAndFlush(rpcRequest);
        try {
            reqMap.put(id, condition);
            condition.await();
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return resMap.get(id);
    }

    public static void doResponse( RpcReponse rpcReponse){
        String id = rpcReponse.getId();
        Condition condition = reqMap.remove(id);
        resMap.put(id, rpcReponse.getObj());
        condition.signal();
    }
}
