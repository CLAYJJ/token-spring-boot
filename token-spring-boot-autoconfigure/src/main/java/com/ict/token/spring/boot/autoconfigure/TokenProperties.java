package com.ict.token.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 *
 * @author Dante
 * @since 1.1.1
 */
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    /**
     * 需要token验证的路径
     */
    private List<String> includes;
    /**
     * 不需要token验证的路径，如果此路径不包含在includes中的话就不要配置
     */
    private List<String> excludes;

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }
}
