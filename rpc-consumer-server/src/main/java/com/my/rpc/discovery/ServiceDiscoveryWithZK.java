package com.my.rpc.discovery;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ServiceDiscoveryWithZK implements IServiceDiscovery{
    private List<String> registryRepos = Lists.newArrayList();

    private CuratorFramework curatorFramework;

    {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZKConfig.CONNECT_STR).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("zkRegistry")
                .build();
        curatorFramework.start();
    }


    @Override
    public String discovery(String serviceName) {
        String path = "/" + serviceName;
        if(CollectionUtils.isEmpty(registryRepos))
        {
            try {
                registryRepos = curatorFramework.getChildren().forPath(path);
                registryWatch(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LoadBalanceStrategy loadBalanceStrategy = new RandomLoadBanlanceStrategy();
        return loadBalanceStrategy.selectHost(registryRepos);
    }

    private  void registryWatch(String servicePath) throws Exception {
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework, servicePath, true);
        //指定回调动作
        PathChildrenCacheListener nodeCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                System.out.println(event.getType() + ">>>>" + new String (event.getData().getData()));
                registryRepos = curatorFramework.getChildren().forPath(servicePath);

            }
        };
        //注册监听
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);
    }
}
