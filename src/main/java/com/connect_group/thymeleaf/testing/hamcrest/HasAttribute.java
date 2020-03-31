package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HasAttribute extends TypeSafeMatcher<HtmlElement> {

  private final String expectedAttributeName;
  private final String expectedAttributeValue;
  private final boolean testAttributeValue;

  public HasAttribute(String attributeName) {
    this.expectedAttributeName = attributeName;
    this.expectedAttributeValue = null;
    this.testAttributeValue = false;
  }

  public HasAttribute(String attributeName, String attributeValue) {
    this.expectedAttributeName = attributeName;
    this.expectedAttributeValue = attributeValue;
    this.testAttributeValue = true;
  }

  @Override
  public void describeTo(Description description) {
    if (!testAttributeValue) {
      description.appendText("has attribute ").appendValue(expectedAttributeName);
    } else {
      description.appendText("has attribute ").appendValue(expectedAttributeName)
          .appendText(" with value ").appendValue(expectedAttributeValue);
    }
  }

  @Override
  public boolean matchesSafely(HtmlElement item) {
    boolean hasNamedAttribute = item.getElement().getAttributeMap()
        .containsKey(expectedAttributeName);
    if (hasNamedAttribute && testAttributeValue) {
      String actualAttributeValue = item.getElement().getAttributeMap().get(expectedAttributeName)
          .getValue();
      return ((expectedAttributeValue == null && actualAttributeValue == null) ||
          expectedAttributeValue.equals(actualAttributeValue));
    } else {
      return hasNamedAttribute;
    }

  }

}
