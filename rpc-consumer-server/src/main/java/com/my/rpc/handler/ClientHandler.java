package com.my.rpc.handler;

import com.my.rpc.entity.RpcRequest;
import com.my.rpc.server.bio.RpcClient;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description TODO
 * @Date 2019/7/3 14:36
 * @Created by rogan.luo
 */
@Data
@AllArgsConstructor
public class ClientHandler implements InvocationHandler {

    private String host ;

    private Integer port ;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParams(args);
        return new RpcClient().sendRequest(rpcRequest);
    }
}
