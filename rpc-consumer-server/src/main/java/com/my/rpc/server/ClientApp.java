package com.my.rpc.server;

import com.my.rpc.entity.User;
import com.my.rpc.server.proxy.ClientProxy;
import com.my.rpc.server.proxy.NettyProxy;
import com.my.rpc.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description
 * @Date 2019/7/3 19:37
 * @Created by rogan.luo
 */
@ComponentScan(basePackages = "com.my.rpc")
public class ClientApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientApp.class);
        NettyProxy nettyProxy = applicationContext.getBean("nettyProxy", NettyProxy.class);
        UserService userService = nettyProxy.getProxyClient(UserService.class, "v1");
//        User user = new User();
//        user.setName("NIXK");
//        String byId = userService.save(user);
//        System.out.println("请求结束：" + byId);
        for(int i=0; i < 100 ; i++) {
            System.out.println(userService.getById(i));
        }
        /*String ip = "127.0.0.1";
        int port = 8080;
        UserService userService = new ClientProxy<UserService>(ip, port).getProxyClient(UserService.class);
        User user = new User();
        user.setName("NIXK");
        String byId = userService.save(user);
        System.out.println("请求结束：" + byId);*/

    }

}
