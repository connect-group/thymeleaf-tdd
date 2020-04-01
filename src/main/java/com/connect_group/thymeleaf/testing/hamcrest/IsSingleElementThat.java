package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import com.connect_group.thymesheet.query.HtmlElements;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsSingleElementThat extends TypeSafeMatcher<HtmlElements> {

  private final Matcher<HtmlElement> individualMatcher;

  public IsSingleElementThat(Matcher<HtmlElement> individualMatcher) {
    this.individualMatcher = individualMatcher;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("is single element that ");
    individualMatcher.describeTo(description);
  }

  @Override
  public boolean matchesSafely(HtmlElements elements) {
    return elements.size() == 1 && individualMatcher.matches(elements.get(0));
  }

  @Override
  protected void describeMismatchSafely(HtmlElements elements, Description mismatchDescription) {
    if (elements.size() == 1) {
      individualMatcher.describeMismatch(elements.get(0), mismatchDescription);
    } else {
       mismatchDescription.appendText("was not a single element ");
       individualMatcher.describeMismatch(elements, mismatchDescription);
    }
  }
}
