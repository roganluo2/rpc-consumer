package com.my.rpc.server.proxy;

import com.my.rpc.handler.ClientHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Proxy;

/**
 * @Description TODO
 * @Date 2019/7/3 19:39
 * @Created by rogan.luo
 */
@Data
@AllArgsConstructor
public class ClientProxy {

    public <T>T getProxyClient(Class<T> tClass, String ip, int port)
    {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{tClass}, new ClientHandler(ip, port));
    }

}
