package com.ict.token.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author Dante
 * @since 1.1.1
 */
@ConfigurationProperties(prefix = "token")
@Validated
public class TokenProperties {
    /**
     * 是否开启token功能,默认为false
     */
    private boolean enable = false;
    /**
     * 需要token验证的路径，使用过滤器的urlPattern的写法，
     * 在配置了enable参数为true时必须配置此项
     */
    @NotNull
    private List<String> includes;
    /**
     * 不需要token验证的路径，如果此路径不包含在includes中的话就不要配置
     */
    private List<String> excludes;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

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
