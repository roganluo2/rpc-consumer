package com.my.rpc.server;

import com.my.rpc.entity.User;
import com.my.rpc.server.proxy.ClientProxy;
import com.my.rpc.service.UserService;

/**
 * @Description TODO
 * @Date 2019/7/3 19:37
 * @Created by rogan.luo
 */
public class ClientApp {

    public static void main(String[] args) {

        String ip = "127.0.0.1";
        int port = 8080;
        UserService userService = new ClientProxy<UserService>(ip, port).getProxyClient(UserService.class);
        User user = new User();
        user.setName("NIXK");
        String byId = userService.save(user);
        System.out.println("请求结束：" + byId);

    }

}
