package com.connect_group.thymeleaf.testing;

import static com.connect_group.thymeleaf.testing.hamcrest.ThymeleafMatchers.exists;
import static com.connect_group.thymeleaf.testing.hamcrest.ThymeleafMatchers.hasAttribute;
import static com.connect_group.thymeleaf.testing.hamcrest.ThymeleafMatchers.hasOnlyText;
import static com.connect_group.thymeleaf.testing.hamcrest.ThymeleafMatchers.isSingleElementThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.connect_group.thymeleaf.testing.config.ThymesheetTestSpringContext;
import com.connect_group.thymesheet.query.HtmlElements;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ThymesheetTestSpringContext.class })
public class SampleUnitTest {

	public static final String HTML_PATH_IN_TEST_RESOURCES_FOLDER=ThymesheetTestSpringContext.getTestResourcesHtmlPath("/sampleUnitTest.html");
	
	private Map<String,Object> model;
	
	@Autowired
	private ThymeleafTestEngine testEngine;

	@Before
	public void setup() {
		model = new HashMap<String,Object>();
	}
	
	@Test
	public void shouldSetTitleToExpectedValue() throws Exception {
		model.put("pageTitle", "expected title");
		HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
		assertThat(tags.matching("title"), isSingleElementThat(hasOnlyText("expected title")));
	}
	
	@Test
	public void shouldSetHeadingToExpectedValue() throws Exception {
		model.put("heading1", "expected heading 1");
		HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
		assertThat(tags.matching("h1"), isSingleElementThat(hasOnlyText("expected heading 1")));		
	}
	
	@Test
	public void shouldCreateThreeParagraphsUnderHeading1() throws Exception {
		model.put("section1Paragraphs", Arrays.asList("para 1", "para 2", "para 3"));
		HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
		assertThat(tags.matching("h1~p").size() - tags.matching("h2~p").size(), equalTo(3)); 		
	}
	
	@Test
	public void shouldCreateThreeParagraphsUnderHeading1WithExpectedContent() throws Exception {
		model.put("section1Paragraphs", Arrays.asList("para 1", "para 2", "para 3"));
		HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
		assertThat(tags.matching("body > p:nth-of-type(-n+3)"), hasOnlyText("para 1", "para 2", "para 3"));
	}
	
	@Test
	public void shouldCreateFourParagraphsUnderHeading2WithExpectedContent() throws Exception {
		model.put("section2Paragraphs", Arrays.asList("para b1", "para b2", "para b3", "para b4"));
		HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
		assertThat(tags.matching("body > h2~p"), hasOnlyText("para b1", "para b2", "para b3", "para b4"));
	}
	
	@Test
	public void shouldNotOutputAnyParagraphs_WhenNoTextInModel() throws Exception {
		HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
		assertThat(tags.matching("body > p"), not(exists()));		
	}
	
	@Test
	public void shouldSpecifyExpectedUrlInLink() throws Exception {
		model.put("href", "http://expected/target/url");
		HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
		assertThat(tags.matching("body > a"), isSingleElementThat(hasAttribute("href", "http://expected/target/url")));		
		
	}
}
