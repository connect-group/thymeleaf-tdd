package com.connect_group.thymeleaf.testing.config;

import com.connect_group.thymeleaf.testing.ThymeleafTestEngine;
import com.connect_group.thymesheet.templatemode.ThymesheetStandardTemplateModeHandlers;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.thymeleaf.cache.StandardCacheManager;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
public class ThymesheetConfig {

  @Value("${thymeleaf.template.cache.enabled:true}")
  protected boolean templateCacheEnabled;

  @Value("${thymeleaf.template.mode:LEGACYHTML5}")
  protected String templateMode;

  @Value("${thymeleaf.template.characterEncoding:UTF-8}")
  protected String characterEncoding;

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  ServletContext servletContext;

  @Bean
  public StandardCacheManager thymeleafCacheManager() {
    return new StandardCacheManager();
  }

  public Set<ITemplateModeHandler> templateModeHandlers() {
    HashSet<ITemplateModeHandler> set = new HashSet<ITemplateModeHandler>();
    ITemplateModeHandler handler = getHandler();
    set.add(handler);
    return set;
  }

  @Bean
  @DependsOn("propertyConfigurer")
  public ThymeleafViewResolver thymeleafViewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setCharacterEncoding(characterEncoding);
    viewResolver.setTemplateEngine(testEngine());
    viewResolver.setOrder(0);
    return viewResolver;
  }

  @Bean
  public UrlBasedViewResolver urlBasedViewResolver() {
    UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();

    viewResolver.setOrder(2);
    viewResolver.setViewClass(org.springframework.web.servlet.view.InternalResourceView.class);

    return viewResolver;
  }

  @Bean
  @DependsOn("propertyConfigurer")
  public ThymeleafTestEngine testEngine() {
    ThymeleafTestEngine engine = new ThymeleafTestEngine();

    engine.setCacheManager(thymeleafCacheManager());
    engine.setTemplateModeHandlers(templateModeHandlers());
    engine.setAdditionalDialects(additionalDialects());

    Set<ITemplateResolver> templateResolvers = new HashSet<ITemplateResolver>();
    templateResolvers.add(servletContextTemplateResolver());
    templateResolvers.add(fileTemplateResolver());
    engine.setTemplateResolvers(templateResolvers);

    engine.setServletContext(servletContext);
    engine.setApplicationContext(applicationContext);
    return engine;
  }

  /**
   * To be overridden if you require additional dialects.
   */
  protected Set<IDialect> additionalDialects() {
    Set<IDialect> additionalDialects = new HashSet<IDialect>();
    return additionalDialects;
  }

  protected ITemplateModeHandler getHandler() {
    ITemplateModeHandler handler = ThymesheetStandardTemplateModeHandlers.LEGACYHTML5;
    for (ITemplateModeHandler stdHandler : ThymesheetStandardTemplateModeHandlers.ALL_TEMPLATE_MODE_HANDLERS) {
      if (stdHandler.getTemplateModeName().equals(templateMode)) {
        handler = stdHandler;
      }
    }
    return handler;
  }


  private FileTemplateResolver fileTemplateResolver() {
    FileTemplateResolver resolver = new FileTemplateResolver();
    resolver.setCharacterEncoding("UTF-8");
    resolver.setTemplateMode("LEGACYHTML5");
    resolver.setCacheable(templateCacheEnabled);
    resolver.setOrder(2);
    return resolver;
  }

  private ServletContextTemplateResolver servletContextTemplateResolver() {
    ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
    resolver.setCharacterEncoding(characterEncoding);
    resolver.setTemplateMode(templateMode);
    resolver.setCacheable(templateCacheEnabled);
    resolver.setOrder(1);
    return resolver;
  }

}
