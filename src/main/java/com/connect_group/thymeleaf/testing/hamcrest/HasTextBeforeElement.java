package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.thymeleaf.dom.Comment;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

import java.util.List;

/**
 * Originally created by Ruth Mills
 */
public class HasTextBeforeElement extends TypeSafeMatcher<HtmlElement> {
    private final String value;

    public HasTextBeforeElement(String value) {
        this.value = value;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has text ").appendValue(value).appendValue(" before tag");

    }

    @Override
    public boolean matchesSafely(HtmlElement item) {
        StringBuilder text = new StringBuilder();
        boolean fail = true;
        boolean elementFound = false;
        boolean textFound = false;

        Element element = item.getElement();
        if(element!=null && element.hasChildren()) {
            List<Node> children = element.getChildren();

            for(Node child : children) {
                if(child instanceof Text) {
                    text.append(((Text)child).getContent());
                    if (elementFound || textFound) {
                        fail = true;
                        break;
                    }
                    textFound = true;
                } else if(!(child instanceof Comment)) {
                    elementFound = true;
                    if (textFound) {
                        fail = false;
                    }
                }
            }
        }

        return !fail && text.toString().equals(value);
    }
}
