package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.thymeleaf.dom.Comment;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

/**
 * Originally created by Ruth Mills
 */
public class HasTextAfterElement extends TypeSafeMatcher<HtmlElement> {

  private final String value;

  public HasTextAfterElement(String value) {
    this.value = value;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has text ").appendValue(value).appendText(" after tag");

  }

  @Override
  public boolean matchesSafely(HtmlElement item) {
    StringBuilder text = new StringBuilder();
    boolean fail = true;
    boolean elementFound = false;
    boolean textFound = false;

    Element element = item.getElement();
    if (element != null && element.hasChildren()) {
      List<Node> children = element.getChildren();

      for (Node child : children) {
        if (child instanceof Text) {
          text.append(((Text) child).getContent());
          if (textFound) {
            fail = true;
            break;
          }
          textFound = true;
          if (elementFound) {
            fail = false;
          }
        } else if (!(child instanceof Comment)) {
          if (textFound) {
            fail = true;
            break;
          }
          elementFound = true;
        }
      }
    }

    return !fail && text.toString().equals(value);
  }
}
