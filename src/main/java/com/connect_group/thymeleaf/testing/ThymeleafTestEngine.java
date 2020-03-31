package com.connect_group.thymeleaf.testing;

import com.connect_group.thymesheet.query.HtmlElements;
import com.connect_group.thymesheet.spring3.SpringThymesheetTemplateEngine;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.spring3.context.SpringWebContext;
import org.thymeleaf.templateparser.html.LegacyHtml5TemplateParser;

public class ThymeleafTestEngine extends SpringThymesheetTemplateEngine {

  private ServletContext servletContext;
  private ApplicationContext applicationContext;

  private final LegacyHtml5TemplateParser parser = new LegacyHtml5TemplateParser("LEGACYHTML5", 1);

  public ThymeleafTestEngine() {
    super();
  }

  @Override
  public void setServletContext(final ServletContext ctx) {
    super.setServletContext(ctx);
    this.servletContext = ctx;
  }

  public MockHttpServletRequest getHttpServletRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
    request.setMethod("GET");
    return request;
  }

  public HtmlElements process(final String path) {
    MockHttpServletRequest request = getHttpServletRequest();
    return process(path, request);
  }

  public HtmlElements process(final String path, final MockHttpServletRequest request) {
    String fragment = toString(path, request);

    return convertResultIntoDom(path, fragment);
  }

  public HtmlElements process(final String path, final Map<String, Object> model) {
    MockHttpServletRequest request = getHttpServletRequest();
    if (model != null && !model.isEmpty()) {
      for (String key : model.keySet()) {
        request.setAttribute(key, model.get(key));
      }
    }

    return process(path, request);
  }

  private HtmlElements convertResultIntoDom(final String path, final String htmlString) {
    List<Node> nodes;
    if (isFragment(htmlString)) {
      nodes = parser.parseFragment(this.getConfiguration(), htmlString);
    } else {
      Document doc = parser
          .parseTemplate(this.getConfiguration(), path, new StringReader(htmlString));
      nodes = doc.getChildren();
    }

    Element root = new Element("#document");
    root.setChildren(nodes);

    return new HtmlElements(root);
  }

  private boolean isFragment(final String htmlString) {
    return htmlString != null
        && (!htmlString.contains("<!DOCTYPE")
        && !htmlString.contains("<!doctype")
        && !htmlString.contains("<html")
        && !htmlString.contains("<!HTML"));
  }

  public String toString(final String path, final MockHttpServletRequest request) {
    MockHttpServletResponse response = new MockHttpServletResponse();

    Map<String, Object> model = new HashMap<String, Object>();
    Enumeration<String> enumerator = request.getAttributeNames();
    while (enumerator.hasMoreElements()) {
      String key = enumerator.nextElement();
      Object obj = request.getAttribute(key);
      model.put(key, obj);
    }
    String fragment = this.process(path, getSpringWebContext(request, response, Locale.UK, model));
    return fragment;
  }

  public String toString(final String path) {
    MockHttpServletRequest request = getHttpServletRequest();
    return toString(path, request);
  }

  public String toString(final String path, final Map<String, Object> model) {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = getHttpServletRequest();

    for (String key : model.keySet()) {
      request.setAttribute(key, model.get(key));
    }

    String fragment = this.process(path, getSpringWebContext(request, response, Locale.UK, model));
    return fragment;
  }

  private IWebContext getSpringWebContext(final HttpServletRequest request,
      final HttpServletResponse response, final Locale locale, final Map<String, ?> model) {

    return new SpringWebContext(
        request, response, servletContext, locale, model, applicationContext
    );
  }

  public void setApplicationContext(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

}
