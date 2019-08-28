package com.my.rpc.zk;

import com.google.common.collect.Lists;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ZKDemo {
     static    String CONNECT_STR = "120.79.226.150:2181";


    public CuratorFramework getCuratorFramework()
    {
        CuratorFramework curatorDemo = CuratorFrameworkFactory.builder().connectString(CONNECT_STR).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("curatorDemo")
                .build();
        curatorDemo.start();
        return curatorDemo;
    }


    public static void main(String[] args) throws Exception {

//        addNode();
//        deleteNode();
//        updateNode();
//        queryNode();
//        setAclNode();
//         addPathChildCache();
           addNodeCache();
    }

    private static void addNodeCache() throws Exception {

        CuratorFramework curatorFramework = new ZKDemo().getCuratorFramework();
        final NodeCache nodeCache = new NodeCache(curatorFramework, "/watch", false);
        //指定回调动作
        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("receive Node Changed");
                System.out.println(nodeCache.getCurrentData().getPath()+"---"+new String(nodeCache.getCurrentData().getData()));

            }
        };

        //注册监听
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
        System.in.read();
    }

    //实现服务注册中心的时候，可以针对服务做动态感知
    private static void addPathChildCache() throws Exception {
        CuratorFramework curatorFramework = new ZKDemo().getCuratorFramework();
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework, "/watch", true);
        //指定回调动作
        PathChildrenCacheListener nodeCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                System.out.println(event.getType() + ">>>>" + new String (event.getData().getData()));
            }
        };
        //注册监听
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);
        System.in.read();
    }


    private static void setAclNode() throws Exception {

        List<ACL> acls = Lists.newArrayList();
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("u1:us"));
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("u2:us"));
        acls.add(new ACL(ZooDefs.Perms.ALL, id1));
        acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.READ, id2));
        new ZKDemo().getCuratorFramework().create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).withACL(acls).forPath("/auth/root","sc".getBytes());

        AuthInfo authInfo = new AuthInfo("digest","u2:us".getBytes());
        List<AuthInfo>  authInfos = Lists.newArrayList(authInfo);
        CuratorFramework curatorDemo = CuratorFrameworkFactory.builder().connectString(CONNECT_STR).sessionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .authorization(authInfos).namespace("curatorDemo")
                .build();
        curatorDemo.start();
        System.out.println(new String (curatorDemo.getData().forPath("/auth/root")));
//        System.out.println(curatorDemo.setData().forPath("/auth/root","TEST2".getBytes()));


    }

    private static void queryNode() throws Exception {
        byte[] bytes = new ZKDemo().getCuratorFramework().getData().forPath("/project/instance");
        System.out.println(new String(bytes));
    }

    private static void updateNode() throws Exception {
        //更新node node不存在，抛出异常
        new ZKDemo().getCuratorFramework().setData().forPath("/project/instance", "test".getBytes());

    }

    private static void deleteNode() throws Exception {
        //删除节点，当节点不存在，会抛出异常
        new ZKDemo().getCuratorFramework().delete().deletingChildrenIfNeeded().forPath("/project");

    }

    private static void addNode() throws Exception {
        //添加节点
        new ZKDemo().getCuratorFramework().create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath("/project/watcher");
    }




}
