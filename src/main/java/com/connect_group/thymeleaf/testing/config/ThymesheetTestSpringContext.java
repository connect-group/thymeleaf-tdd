package com.connect_group.thymeleaf.testing.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration 
@Import({ThymesheetConfig.class})
public class ThymesheetTestSpringContext extends WebMvcConfigurerAdapter {
	
	@Value("${thymeleaf.template.characterEncoding:src/main/webapp}")
	protected String webappPath;
	
    @Autowired
    StandardEnvironment environment;
        
    @Bean
    @DependsOn("propertyConfigurer")
    public ServletContext servletContext() {
    	MockServletContext context = new MockServletContext(webappPath, new FileSystemResourceLoader());
    	return context;
    }
    
    
	/**
	 * Allow us to use .properties files
	 * @throws IOException 
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigurer() throws IOException {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		
		Resource[] locations = {
				new ClassPathResource("test.properties")
			};
		configurer.setLocations(locations);
		configurer.setIgnoreResourceNotFound(true);
		
		return configurer;
	}
	
	public static String getTestResourcesHtmlPath(String filename) {
		String path;
		URL url = ThymesheetTestSpringContext.class.getResource(filename);
		try {
			path = URLDecoder.decode(url.getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			path="";
			e.printStackTrace();
		}
		return path;
	}
}
