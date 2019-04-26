package com.harystolho.adexchange.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.harystolho.adexchange.filters.AuthFilter;

@Configuration
public class FilterConfig {

	private AuthFilter authFilter;

	@Autowired
	public FilterConfig(AuthFilter authFilter) {
		this.authFilter = authFilter;
	}
	
	@Bean
	public FilterRegistrationBean<AuthFilter> authRegistrationBean() {
		FilterRegistrationBean<AuthFilter> filter = new FilterRegistrationBean<>();

		filter.setFilter(authFilter);
		filter.addUrlPatterns("/dashboard/*", "/api/*");

		return filter;
	}

}
