package com.qinfengsa.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 服务注册
 * @author: qinfengsa
 * @date: 2019/7/12 21:24
 */
@Slf4j
public class ServiceRegistry {

    /**
     * 注册地址
     */
    private String registryAddress;



    CuratorFramework curatorFramework = null;

    {
        //初始化zookeeper的连接， 会话超时时间是5s，衰减重试
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.CONNECTION_STR)
                .sessionTimeoutMs(500000)
                .connectionTimeoutMs(30000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ZkConfig.PARENT_PATH)
                .build();
        curatorFramework.start();
    }


    /**
     * 服务注册
     * @param serviceName
     * @param serviceAddress
     */
    public void registry(String serviceName, String serviceAddress) {
        String servicePath = "/" + serviceName;
        try {
            // 判断节点是否存在
            if(curatorFramework.checkExists().forPath(servicePath) == null){
                curatorFramework.create().creatingParentsIfNeeded().
                        withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            // serviceAddress: ip:port
            String addressPath = servicePath + "/" + serviceAddress;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            log.debug("服务注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
