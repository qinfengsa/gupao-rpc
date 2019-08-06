package com.qinfengsa.rpc.demo.api;

/**
 * 计算服务
 * @author: qinfengsa
 * @date: 2019/7/14 07:28
 */
public interface CalculateService {

    /**
     * 加法
     * @param a 参数1
     * @param b 参数2
     * @return
     * @throws Exception
     */
    int add(int a, int b) throws Exception;

    /**
     * 减法
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    int subtract(int a, int b) throws Exception;

    /**
     * 乘法
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    int multiply(int a, int b) throws Exception;

    /**
     * 除法
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    int divide(int a, int b) throws Exception;
}
