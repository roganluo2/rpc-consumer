package com.my.rpc.config;

import com.my.rpc.server.proxy.ClientProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Date 2019/7/4 17:04
 * @Created by rogan.luo
 */
@Configuration
public class SpringConfig {

    @Bean
    public ClientProxy clientProxy()
    {
        return new ClientProxy();
    }

}
