package org.mmall.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mmall.common.Const;
import org.mmall.common.RedissonManager;
import org.mmall.service.IOrderService;
import org.mmall.util.PropertiesUtil;
import org.mmall.util.RedisShardedPoolUtil;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * TODO Spring Schedule
 *
 * 定时关闭订单，用分布式锁控制：同一时刻只有一个服务器执行关单业务
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

//    /**
//     * 当使用shutdown温柔的关闭 tomcat，则执行此方法，
//     * 配合V2可以确保锁释放，但 kill 则依然不行！且会延迟关闭时间。
//     */
//    @PreDestroy
//    public void delLock(){
//        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//    }

//    /**
//     * 简单示例 : 仅支持单机
//     */
//    @Scheduled(cron="0 */1 * * * ?")//每1分钟(每个1分钟的整数倍)
//    public void closeOrderTaskV1(){
//        log.info("关闭订单定时任务启动");
//        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
//        iOrderService.closeOrder(hour);
//        log.info("关闭订单定时任务结束");
//    }

//    /**
//     * 支持分布式
//     *
//     * bug : 存在死锁可能（设置分布式锁后立即关闭，锁有效期永久）
//     */
//    @Scheduled(cron="0 */1 * * * ?")
//    public void closeOrderTaskV2(){
//        log.info("关闭订单定时任务启动");
//        // 设置分布锁
//        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
//        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
//                String.valueOf(System.currentTimeMillis()+lockTimeout));
//
//        if(setnxResult != null && setnxResult.intValue() == 1){
//            //如果返回值是1，代表设置成功，获取锁
//            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        }else{
//            log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        }
//        log.info("关闭订单定时任务结束");
//    }

    /**
     * 原生分布式
     *
     * 双重防死锁，不会发生死锁
     */
    @Scheduled(cron="0 */1 * * * ?")
    public void closeOrderTaskV3(){
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                String.valueOf(System.currentTimeMillis()+lockTimeout));

        if(setnxResult != null && setnxResult.intValue() == 1){
            // 获得锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            // 锁消失或失效
            if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                // 再次获取旧值（防止集群修改），设置新值
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                        String.valueOf(System.currentTimeMillis()+lockTimeout));
                 // 锁消失或锁没被使用
                if(getSetResult == null || StringUtils.equals(lockValueStr, getSetResult)){
                    //真正获取到锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else{
                    log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else{
                log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单定时任务结束");
    }

    /**
     * SpringSchedule + Redisson 实现分布式锁
     */
//    @Scheduled(cron="0 */1 * * * ?")
    public void closeOrderTaskV4(){
        int lockTimeout = Integer.parseInt(PropertiesUtil.getProperty("lock.timeout","5000"));
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            // 尝试获取锁 : （等待时间，自动解锁时间，时间单位）
            // FIXME waitTime为业务预估时间，如果不能确认，请设置为0，否则可能尊重多个应用获得锁
            if(getLock = lock.tryLock(0, lockTimeout, TimeUnit.MILLISECONDS)){
                log.info("Redisson获取到分布式锁:{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
                // 关闭订单
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
                iOrderService.closeOrder(hour);
            }else{
                log.info("Redisson没有获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson分布式锁获取异常",e);
        } finally {
            if(!getLock){
                return ;
            }
            // 释放锁 (key 会直接被删除)
            lock.unlock();
            log.info("Redisson分布式锁释放锁");
        }
    }



    /**
     * 设置分布式锁的有效期，关闭订单，释放锁
     * @param lockName
     */
    private void closeOrder(String lockName){
        // 设置有效期，防止死锁
        int lockTimeout = Integer.parseInt(PropertiesUtil.getProperty("lock.timeout","5000"));
        RedisShardedPoolUtil.expire(lockName, lockTimeout);
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());

        // 关闭订单
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);

        // 释放锁
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("===============================");
    }




}
