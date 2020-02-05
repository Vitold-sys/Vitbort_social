package com.radkevich.Messenger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.path.avatar}")
    private String uploadPathAvatar;

    @Value("${upload.path.posts}")
    private String uploadPathPost;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file://" + uploadPath + "/")
                .addResourceLocations("file://" + uploadPathAvatar + "/")
                .addResourceLocations("file://" + uploadPathPost + "/");
    }
}
