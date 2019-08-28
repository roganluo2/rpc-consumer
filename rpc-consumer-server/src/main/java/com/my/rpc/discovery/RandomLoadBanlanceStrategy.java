package com.my.rpc.discovery;

import java.util.List;
import java.util.Random;

public class RandomLoadBanlanceStrategy extends AbstractLoadBalanceStrategy {


    @Override
    protected String doSelectHost(List<String> hosts) {
        int length=hosts.size();
        Random random=new Random(); //从repos的集合内容随机获得一个地址
        return hosts.get(random.nextInt(length));    }
}
