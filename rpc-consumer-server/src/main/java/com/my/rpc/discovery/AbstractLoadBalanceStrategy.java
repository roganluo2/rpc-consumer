package com.my.rpc.discovery;

import org.springframework.util.CollectionUtils;

import java.util.List;

public  abstract class AbstractLoadBalanceStrategy implements LoadBalanceStrategy {

    @Override
    public String selectHost(List<String> hosts) {
        if(CollectionUtils.isEmpty(hosts))
        {
            return null;
        }
        if(hosts.size() == 1)
        {
            return hosts.get(0);
        }
        return doSelectHost(hosts);
    }

    protected abstract String doSelectHost(List<String> hosts);
}
