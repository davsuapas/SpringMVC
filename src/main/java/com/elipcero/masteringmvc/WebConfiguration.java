package com.elipcero.masteringmvc;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;

import java.time.LocalDate;

@Configuration
@EnableConfigurationProperties({PictureUploadProperties.class})
public class WebConfiguration extends WebMvcConfigurerAdapter {
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldType(LocalDate.class, new USLocalDateFormatter());
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	//  Matriz de variables en el controlador
	//	By default, Spring MVC removes every character following a semicolon in a URL.
	//	The first thing we need to do to enable matrix variables in our application is to turn
	//	off this behavior.
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);
	}	
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return container -> container.addErrorPages(new	ErrorPage(MultipartException.class, "/uploadError"));
	}	
}