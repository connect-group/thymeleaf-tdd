package com.connect_group.thymeleaf.testing.config;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import org.springframework.context.support.AbstractMessageSource;

public class TestMessageSource extends AbstractMessageSource {

  HashMap<String, String> messageOverrides = new HashMap<String, String>();

  public void reset() {
    messageOverrides = new HashMap<String, String>();
  }

  public void givenMessageWithKey(String code, String message) {
    messageOverrides.put(code, message);
  }

  @Override
  protected MessageFormat resolveCode(String key, Locale locale) {
    if (messageOverrides.containsKey(key)) {
      return new MessageFormat(messageOverrides.get(key), locale);
    } else {
      return new MessageFormat("??" + key + "_" + locale.toString() + "??", locale);
    }
  }

  @Override
  protected String resolveCodeWithoutArguments(String code, Locale locale) {
    if (messageOverrides.containsKey(code)) {
      return messageOverrides.get(code);
    } else {
      return super.resolveCodeWithoutArguments(code, locale);
    }
  }
}
