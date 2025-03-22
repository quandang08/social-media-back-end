package com.socialmedia.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gemini.api")
@EnableConfigurationProperties(GeminiConfig.class)
public class GeminiConfig  {
    private String key;
    private String url;

}
