package com.livable.server.core.config;

import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActorArgumentResolver;
import com.livable.server.core.util.StringToLocalDateConverter;
import com.livable.server.core.util.StringToRestaurantCategoryConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider tokenProvider;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToRestaurantCategoryConverter());
        registry.addConverter(new StringToLocalDateConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization")
                .allowedOriginPatterns("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginActorArgumentResolver(tokenProvider));
    }
}
