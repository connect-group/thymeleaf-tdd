package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;

public class HasAttribute extends TypeSafeMatcher<HtmlElement> {

  private final String expectedAttributeName;
  private final String expectedAttributeValue;
  private final boolean testAttributeValue;
  private final boolean testAnyNonNullValue;

  public HasAttribute(String attributeName) {
    this.expectedAttributeName = attributeName;
    this.expectedAttributeValue = null;
    this.testAttributeValue = false;
    this.testAnyNonNullValue = false;
  }

  public HasAttribute(String attributeName, String attributeValue) {
    this.expectedAttributeName = attributeName;
    this.expectedAttributeValue = attributeValue;
    this.testAttributeValue = true;
    this.testAnyNonNullValue = false;
  }

  public HasAttribute(String attributeName, boolean testAnyNonNullValue) {
    this.expectedAttributeName = attributeName;
    this.expectedAttributeValue = null;
    this.testAttributeValue = true;
    this.testAnyNonNullValue = testAnyNonNullValue;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has attribute ").appendValue(expectedAttributeName);
    if (testAttributeValue) {
      if (testAnyNonNullValue) {
        description.appendText(" with any value ");
      } else {
        description.appendText(" with value ").appendValue(expectedAttributeValue);
      }
    }
  }

  @Override
  public boolean matchesSafely(HtmlElement item) {
    Element element = item.getElement();
    Map<String, Attribute> attributeMap = element.getAttributeMap();
    boolean hasNamedAttribute = attributeMap.containsKey(expectedAttributeName);

    if (hasNamedAttribute && testAttributeValue) {
      String actualAttributeValue = attributeMap.get(expectedAttributeName).getValue();
      if (testAnyNonNullValue) {
        return actualAttributeValue != null;
      }
      return expectedAttributeValue == null
          ? actualAttributeValue == null : expectedAttributeValue.equals(actualAttributeValue);
    } else {
      return hasNamedAttribute;
    }
  }
}
