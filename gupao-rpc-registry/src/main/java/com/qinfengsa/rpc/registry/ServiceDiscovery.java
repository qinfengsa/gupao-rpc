package com.qinfengsa.rpc.registry;

import com.qinfengsa.rpc.loadbalance.LoadBalance;
import com.qinfengsa.rpc.loadbalance.RandomLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 负责服务的查找，返回
 * @author: qinfengsa
 * @date: 2019/7/12 21:24
 */
@Slf4j
public class ServiceDiscovery {

    /**
     * 注册地址
     */
    private String registryAddress;

    /**
     * 服务地址的本地缓存
     */
    private Map<String,List<String>> serviceListMap = new HashMap<>(16);




    CuratorFramework client = null;

    {
        //初始化zookeeper的连接， 会话超时时间是5s，衰减重试
        client = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.CONNECTION_STR)
                .sessionTimeoutMs(500000)
                .connectionTimeoutMs(30000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ZkConfig.PARENT_PATH)
                .build();
        client.start();


    }





    private void addWatcher() throws Exception {
        // 监控 Node结点
        TreeCache cache = new TreeCache(client, "/") ;
        cache.start() ;
        // 监控目录下的所有结点
        cache.getListenable().addListener((framework, event)-> {
            log.info("123");
            switch (event.getType()) {
                case NODE_ADDED:
                    log.info("node_added:{}",event.getData().getPath());
                    break;
                case NODE_UPDATED:
                    log.info("node_updated:{}" ,event.getData().getPath());
                    break;
                case NODE_REMOVED:
                    log.info("node_removed:{}",event.getData().getPath());
                    break;
                default:
                    break;
            }
        });
    }


    /**
     * 服务发现
     * @return
     */
    public String discovery(String serviceName) {
        String path = "/" + serviceName;
        List<String> addrList = null;
        if (serviceListMap.containsKey(serviceName)) {
            addrList = serviceListMap.get(serviceName);
        } else {
            try {
                addrList = client.getChildren().forPath(path);
                serviceListMap.put(serviceName,addrList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LoadBalance balance = new RandomLoadBalance();

        return balance.selectAddr(addrList);
    }
}
