package com.geral.springboot01.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidConfig {
	
	@ConfigurationProperties(prefix="spring.datasource")
	@Bean
	public DataSource druid() 
	{
		return new DruidDataSource();
	}
	
	//配置druid的监控
	//1.配置一个管理后台的servlet
	@Bean
	public ServletRegistrationBean statViewServlet() 
	{
		ServletRegistrationBean bead=new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
		Map<String, String> initParams=new HashMap<String, String>();
		initParams.put("loginUsername", "panpan");
		initParams.put("loginPassword", "panpan");
		initParams.put("allow", "localhost");//默认允许所有("allow", "");
		bead.setInitParameters(initParams);
//		initParams.put("allow", "");
//		initParams.put("deny", "11.11.11.11");//拒绝
		return bead;
	}
//	2.配置一个web监控的filter
	@Bean
	public FilterRegistrationBean webStatFilter() 
	{
		FilterRegistrationBean bean=new FilterRegistrationBean();
		bean.setFilter(new WebStatFilter());
		Map<String,String> initParams=new HashMap<String, String>();
		initParams.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.jsp,/druid/*,/download/*");
		bean.setInitParameters(initParams);
		bean.setUrlPatterns(Arrays.asList("/*"));
		return bean;
	}
}
