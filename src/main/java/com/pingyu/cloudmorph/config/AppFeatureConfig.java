package com.pingyu.cloudmorph.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用扩展能力配置。
 */
@Configuration
@ConfigurationProperties(prefix = "app.feature")
@Data
public class AppFeatureConfig {

    /**
     * 是否启用封面图自动生成。
     */
    private boolean enableCoverGeneration = false;

    /**
     * 封面图存储地址前缀，当前先留空或指向本地静态服务。
     */
    private String coverBaseUrl = "";
}
