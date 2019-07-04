package com.ict.token.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 *
 * @author Dante
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(TokenProperties.class)
@ConditionalOnProperty("token.includes")
public class TokenAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(TokenFiler.class)
    public FilterRegistrationBean tokenFilter(TokenProperties tokenProperties) {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new TokenFiler(tokenProperties.getExcludes()));
        bean.setUrlPatterns(tokenProperties.getIncludes());
        bean.setEnabled(true);
        return bean;
    }
}
