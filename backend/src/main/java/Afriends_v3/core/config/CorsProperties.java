package Afriends_v3.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * 跨域配置属性
 *
 * @author xiongxiaoyang
 * @date 2022/5/17
 */
@ConfigurationProperties(prefix = "novel.cors")
public record CorsProperties(List<String> allowOrigins) {
    // 手动定义方法，兼容Spring调用，避免record自动生成方法的兼容问题
    public List<String> getAllowOrigins() {
        // 若allowOrigins为null，返回空列表，避免后续空指针
        return allowOrigins == null ? Collections.emptyList() : allowOrigins;
    }
}
