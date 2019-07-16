package com.my.rpc.handler;

import com.my.rpc.protocol.RpcReponse;
import com.my.rpc.protocol.RpcRequest;
import com.my.rpc.server.bio.RpcClient;
import com.my.rpc.server.netty.NettyClient;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
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

    public NettyClientHandler(Channel channel)
    {
        this.channel = channel;
    }

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private static Map<String, Condition> reqMap = new HashMap<String, Condition>();

    private static Map<String, Object> resMap = new HashMap<String, Object>();

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParams(args);
        String id = UUID.randomUUID().toString();
        rpcRequest.setId(id);
        new NettyClient(channel).sendRequest(rpcRequest);
        lock.lock();
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

    public static void doResponse(Channel channel, Object obj){
        RpcReponse rpcReponse = (RpcReponse) obj;
        String id = rpcReponse.getId();
        Condition condition = reqMap.remove(id);
        resMap.put(id, rpcReponse.getObj());
        condition.signal();
    }
}
