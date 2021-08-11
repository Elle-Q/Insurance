package com.fintech.insurance.micro.thirdparty.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(this.redisProperties.getPool().getMaxIdle());
        jedisPoolConfig.setMinIdle(this.redisProperties.getPool().getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(this.redisProperties.getPool().getMaxWait());

        return new JedisPool(jedisPoolConfig, this.redisProperties.getHost(), this.redisProperties.getPort(), this.redisProperties.getTimeout(), StringUtils.isEmpty(this.redisProperties.getPassword()) ? null : this.redisProperties.getPassword(), this.redisProperties.getDatabase());
    }
}
