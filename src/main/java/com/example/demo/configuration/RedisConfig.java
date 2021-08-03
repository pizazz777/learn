package com.example.demo.configuration;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.example.demo.properties.ProjectProperties;
import com.huang.util.container.ContainerUtil;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-04-30 14:40
 */
@EnableCaching
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig extends CachingConfigurerSupport {

    private ProjectProperties projectProperties;
    private RedisProperties redisProperties;
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisConfig(ProjectProperties projectProperties, RedisProperties redisProperties, RedisConnectionFactory redisConnectionFactory) {
        this.projectProperties = projectProperties;
        this.redisProperties = redisProperties;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 缓存 key 生成器
     *
     * @return r
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder string = new StringBuilder();
            string.append(target.getClass().getName()).append(":");
            string.append(method.getName());
            if (ContainerUtil.isNotEmpty(params)) {
                string.append(":");
                for (Object obj : params) {
                    string.append(Objects.nonNull(obj) ? obj : "null");
                    string.append(":");
                }
            }
            return string.toString();
        };
    }

    @Bean
    @Override
    @SuppressWarnings("unchecked")
    public CacheManager cacheManager() {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        //序列化方式
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer());
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        //设置过期时间
        defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofSeconds(3600));
        ////设置默认超过期时间是30秒
        //defaultCacheConfig.entryTtl(Duration.ofSeconds(30));

        //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);

        //设置白名单---非常重要********
        /*
         * 使用fastjson的时候：序列化时将class信息写入，反解析的时候，
         * fastjson默认情况下会开启autoType的检查，相当于一个白名单检查，
         * 如果序列化信息中的类路径不在autoType中，
         * 反解析就会报com.alibaba.fastjson.JSONException: autoType is not support的异常
         * 可参考 https://blog.csdn.net/u012240455/article/details/80538540
         */
        ParserConfig.getGlobalInstance().addAccept(projectProperties.getProjectPackage() + ".");
        return cacheManager;
    }

    /**
     * String key 序列器
     */
    @Bean("keySerializer")
    public RedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer(StandardCharsets.UTF_8);
    }

    /**
     * fastJsonRedis 序列器
     */
    @Bean("valueSerializer")
    public RedisSerializer fastJsonRedisSerializer() {
        // FastJsonConfig config = new FastJsonConfig();
        // config.setDateFormat(DateConst.DEFAULT_DATE_FORMAT);
        // config.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteEnumUsingName);
        // RedisSerializer serializer = new GenericFastJsonRedisSerializer();
        // serializer.setFastJsonConfig(config);
        return new GenericFastJsonRedisSerializer();
    }

    /**
     * redis 操作对象
     *
     * @param connectionFactory 连接工厂
     * @param keySerializer     key序列对象
     * @param valueSerializer   value序列对象
     * @return r
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer keySerializer, RedisSerializer valueSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // key string
        template.setKeySerializer(keySerializer);
        // value - fastJson
        template.setValueSerializer(valueSerializer);

        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);

        return template;
    }

    /**
     * shiro-redis 缓存池配置
     *
     * @return r
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        return jedisPoolConfig;
    }

    /**
     * shiro-redis 缓存池
     *
     * @param jedisPoolConfig redis缓存池配置
     * @return r
     */
    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        return new JedisPool(jedisPoolConfig,
                redisProperties.getHost(),
                redisProperties.getPort(),
                redisProperties.getTimeout().getNano(),
                redisProperties.getPassword(),
                redisProperties.getDatabase());
    }

    /**
     * shiro-redis 缓存管理器
     *
     * @param jedisPool 缓存池
     * @return r
     */
    @Bean
    public IRedisManager redisManager(JedisPool jedisPool) {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
        redisManager.setJedisPool(jedisPool);
        return redisManager;
    }


}
