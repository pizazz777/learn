package com.example.demo.configuration;

import com.example.demo.util.SpringContextHandler;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @date 2020-05-06 11:01
 */
@Configuration
public class AutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public SpringContextHandler springContextHandler() {
        return new SpringContextHandler();
    }

}
