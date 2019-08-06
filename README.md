## 模块
### gupao-rpc-common 
common包封装请求RpcRequest，响应RpcResponse和序列化方法
### gupao-rpc-registry
registry包封装服务的注册以及发现，通过ZooKeeper实现
### gupao-rpc-server
server包封装服务的实际调用过程和服务发布，基于Netty实现通信
### gupao-rpc-client
client包封装服务的请求和代理方法，基于Netty实现通信


## 参考
[RPC框架实现思路浅析](https://www.cnblogs.com/chenliangcl/p/7488098.html)