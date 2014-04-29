package com.connect_group.thymeleaf.testing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration 
@Import({ThymesheetConfig.class})
public class ThymesheetTestSpringContext extends ExtendableTestSpringContext {}
