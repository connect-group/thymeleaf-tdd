package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import com.connect_group.thymesheet.query.HtmlElements;
import java.util.Collection;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public abstract class ThymeleafMatchers {

  @Factory
  public static Matcher<Object> exists() {
    return new Exists();
  }

  @Factory
  public static Matcher<HtmlElement> hasAttribute(String attributeName) {
    return new HasAttribute(attributeName);
  }

  @Factory
  public static Matcher<HtmlElement> hasAttributeWithAnyNonNullValue(String attributeName) {
    return new HasAttribute(attributeName, true);
  }

  @Factory
  public static Matcher<HtmlElement> hasAttribute(String attributeName, String attributeValue) {
    return new HasAttribute(attributeName, attributeValue);
  }

  @Factory
  public static Matcher<HtmlElement> hasChildren() {
    return new HasChildren();
  }

  @Factory
  public static Matcher<HtmlElement> hasClass(String className) {
    return new HasClass(className);
  }

  @Factory
  public static Matcher<HtmlElement> hasClasses(String classNames) {
    return new HasClass(classNames);
  }

  @Factory
  public static Matcher<HtmlElement> hasComment() {
    return new HasComment();
  }

  @Factory
  public static Matcher<HtmlElement> hasCommentWithText(String text) {
    return new HasComment(text);
  }

  @Factory
  public static Matcher<HtmlElement> hasOnlyText(String text) {
    return new HasOnlyText(text);
  }

  @Factory
  public static Matcher<HtmlElements> hasOnlyText(String... text) {
    return new HasTextInOrder(text);
  }

  @Factory
  public static Matcher<HtmlElements> isSingleElementThat(Matcher<HtmlElement> matches) {
    return new IsSingleElementThat(matches);
  }

  @Factory
  public static Matcher<Collection<?>> occursOnce() {
    return new OccursOnce();
  }

  @Factory
  public static Matcher<HtmlElement> hasTextBeforeElement(String text) {
    return new HasTextBeforeElement(text);
  }

  @Factory
  public static Matcher<HtmlElement> hasTextAfterElement(String text) {
    return new HasTextAfterElement(text);
  }
}
