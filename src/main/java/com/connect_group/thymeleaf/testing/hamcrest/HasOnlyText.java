package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.thymeleaf.dom.Comment;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

public class HasOnlyText extends TypeSafeMatcher<HtmlElement> {

  private final String value;

  public HasOnlyText(String value) {
    this.value = value;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has text ").appendValue(value);

  }

  @Override
  public boolean matchesSafely(HtmlElement item) {
    StringBuilder text = new StringBuilder();
    boolean fail = false;

    Element element = item.getElement();
    if (element != null && element.hasChildren()) {
      List<Node> children = element.getChildren();

      for (Node child : children) {
        if (child instanceof Text) {
          text.append(((Text) child).getContent());
        } else if (!(child instanceof Comment)) {
          fail = true;
          break;
        }
      }
    }

    return !fail && text.toString().equals(value);
  }

}
