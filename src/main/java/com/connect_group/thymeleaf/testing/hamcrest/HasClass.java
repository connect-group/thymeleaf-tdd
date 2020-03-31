package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.thymeleaf.dom.Element;

public class HasClass extends TypeSafeMatcher<HtmlElement> {

  private final Set<String> expectedClassNames;

  public HasClass(String className) {
    expectedClassNames = toSet(className);
  }

  @Override
  public boolean matchesSafely(HtmlElement item) {
    Element element = item.getElement();
    String classes = element.getAttributeValue("class");
    Set<String> classNames = toSet(classes);

    return classNames.containsAll(expectedClassNames);
  }

  private Set<String> toSet(String classNames) {
    Set<String> set = new HashSet<String>();

    if (classNames == null || classNames.length() == 0) {
      set = Collections.emptySet();
    } else {
      set = new HashSet<String>();
      String[] names = classNames.split("\\s+");
      for (String name : names) {
        set.add(name);
      }
    }
    return set;
  }

  @Override
  public void describeTo(Description description) {
    if (expectedClassNames.size() > 1) {
      description.appendText("has classes ");
    } else {
      description.appendText("has class ");
    }

    for (String className : expectedClassNames) {
      description.appendText(" ").appendValue(className);
    }

  }


}
