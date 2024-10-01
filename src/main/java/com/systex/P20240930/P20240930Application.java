package com.systex.P20240930;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.systex.P20240930.filter.AuthFilter;

@SpringBootApplication
public class P20240930Application {

	public static void main(String[] args) {
		SpringApplication.run(P20240930Application.class, args);
	}
	
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(){
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter());
        registrationBean.addUrlPatterns("/*"); // 設定過濾器的作用範圍
        return registrationBean;
    }
}
