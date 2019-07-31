package org.mmall.common;

import org.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Ression
 */
@Component
@Slf4j
public class RedissonManager {

    // redisson config
    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String redis1Pass = PropertiesUtil.getProperty("redis1.pass");

    /**
     * @see PostConstruct 构造器完成调用该方法 (效果等同于static块调用)
     */
    @PostConstruct
    private void init(){
        try {
            config.useSingleServer()
                    .setAddress(redis1Ip + ":" + redis1Port).setPassword(redis1Pass);

            redisson = (Redisson) Redisson.create(config);

            log.info("初始化Redisson结束");
        } catch (Exception e) {
            log.error("redisson init error",e);
        }
    }



}
