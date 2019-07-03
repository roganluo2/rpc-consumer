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
public class ClientProxy<T> {
    private String ip;
    private int port;


    public T getProxyClient(Class<T> tClass)
    {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{tClass}, new ClientHandler(ip, port));
    }

}
