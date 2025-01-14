package com.openclassrooms.payMyBuddy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.openclassrooms.payMyBuddy.interceptors.LoginInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoginInterceptor loginInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**").addPathPatterns("**")
				.excludePathPatterns("/login").excludePathPatterns("/logout").excludePathPatterns("/registration")
				.excludePathPatterns("/css/**").excludePathPatterns("/h2-console").excludePathPatterns("/favicon.ico");
	}

}
