thymeleaf-tdd
=============

Test Driven Development framework for Spring with Thymeleaf and Thymesheet.

## Maven

Include the latest release from Maven,

		<dependency>
			<groupId>com.connect-group</groupId>
			<artifactId>thymeleaf-tdd</artifactId>
			<version>1.0.4</version>
			<scope>test</scope>
		</dependency>

Thymeleaf-TDD makes use of the full Hamcrest suite (version 1.3), and JUnit 4.11; these will be pulled in as dependancies when you include thymeleaf-tdd.


## What is Thymeleaf?
Thymeleaf is a Java library. It is an XML / XHTML / HTML5 template engine (extensible to other formats) that can work both in web and non-web environments, best suited for serving XHTML/HTML5.

It provides an optional module for integration with Spring MVC, so that you can use it as a complete substitute of JSP in your applications made with this technology, even with HTML5.

The main goal of Thymeleaf is to provide an elegant and well-formed way of creating powerful natural templates that can be correctly displayed by browsers and therefore work also as static prototypes. 

For more detail, see http://www.thymeleaf.org/

## What about Thymeleaf Testing?
* https://github.com/thymeleaf/thymeleaf-testing
Thymeleaf Testing is an alternative (and the original) method of testing Thymeleaf.  It will also work with Thymesheet, if required.
For Test Driven Development however it was not quite flexible enough: it will allow you to generate an HTML page and perform a "diff" against an expected outcome, which is perfect for many scenarios.  However it does not fit well with TDD where we run a series of independant tests.  

So for occassions where the standard test package is not appropriate, or you would prefer to go with a more traditional JUnit approach, thymeleaf-tdd may be a candidate.

## What is TDD?
Test Driven Development simply put involves writing a test, then writing code that causes the test to pass.  

1. RED BAR: Write, and run, a behaviour specification (test) that fails.
2. GREEN BAR: Code just enough to meet the specification. Code and test until it passes.
3. Refactor, Retest

Note that writing some code, then adding a test, is NOT TDD.

TDD is a best practice in software development.

For more information, see

* http://www.jamesshore.com/Agile-Book/test_driven_development.html
* http://dannorth.net/introducing-bdd/

## What can you test?
Using the Thymeleaf-TDD project you can develop and test Thymeleaf custom attribute processors, and Thymeleaf HTML pages.  This provides a way to use TDD to develop HTML with Thymeleaf attributes without running Tomcat or any other servlet container.  It also provides support for TDD of Thymesheet - where the Thymeleaf attributes are separated into an external CSS3 style file.



## Example
First, you must create a JUnit test class which runs with SpringJUnit4ClassRunner, and uses the default SpringContext supplied with thymeleaf-tdd.  You can create your own Spring configuration required - if you need to scan for beans, for example or customise the spring environment for any reason.  The default Spring configuration uses LEGACYHTML5 but this is easily changed by adding a property to /src/test/resources/test.properties - more on that later.

So an empty JUnit test class appears as follows,

    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
    import com.connect_group.thymeleaf.testing.config.ThymesheetTestSpringContext;
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = { ThymesheetTestSpringContext.class })
    public class SampleUnitTest {
        @Autowired
        private ThymeleafTestEngine testEngine;
        
        @Autowired
        private TestMessageSource messageSource;

    }


To begin with, imagine you have been supplied a HTML file by your frontend development team.  (Or that you have developed it yourself).

    <html>
      <head>
        <title>Lorem Ipsum</title>
      </head>
      <body>
        <h1>A heading</h1>
        <p>Some text</p>
        <p>Some more text</p>
        
        <h2>A sub-heading</h2>
        <p>Sub text</p>
        <p>More sub text</p>
        <a href="http://www.example.com/">A Link</a>
        
        <pre>Some i18n copy goes here</pre>
      </body>
    </html>

This can be placed in your webapp - wherever you would normally place your HTML files.  For example, /src/main/webapp/html/test.html




### Should Set Title To Expected Value
The first thing we need to do is change the HTML page title "Lorem Ipsum", to something dynamic, perhaps supplied by a CMS for example.  In a Spring controller we would add a key/value pair to the ModelAndView object.  But we are testing in isolation of the controller, so we have to create our own Map<String,?> to represent the model.

So our first test would look something like this,

    @Test
    public void shouldSetTitleToExpectedValue() throws Exception {
      Map<String,Object> model = new HashMap<String,Object>();
      model.put("pageTitle", "expected title");
      HtmlElements tags = testEngine.process("/html/test.html", model);
      assertThat(tags.matching("title"), isSingleElementThat(hasOnlyText("expected title")));
    }

Now run the tests; e.g. if your IDE is Eclipse, use the JUnit view to run the test; or from the command line type "mvn test".

The test will fail because we have not added the Thymeleaf tag to the HTML yet!  Great - we got a "Red Bar".

Now we have to update the title by adding in a data-th-text attribute.

        <title data-th-text="${pageTitle}">Lorem Ipsum</title>

Save the file, run the test: Green bar!  We have written our first unit test.

### Should Set Heading To Expected Value
Similarly the "H1" can be set by first writing a test to set the heading, 

    @Test
    public void shouldSetHeadingToExpectedValue() throws Exception {
      Map<String,Object> model = new HashMap<String,Object>();
      model.put("heading1", "expected heading 1");
      HtmlElements tags = testEngine.process("/html/test.html", model);
      assertThat(tags.matching("h1"), isSingleElementThat(hasOnlyText("expected heading 1")));		
    }

... this will fail when run.  Then add in the attribute for the heading,

        <h1 data-th-text="${heading1}">A heading</h1>

... run the test again, and you get a Green bar.  Success!

Normally in TDD you should refactor after each test.  In this case there is very little to refactor; however, the tests themselves should be tidied up as we are creating a HashMap in every test, and using the same path "/html/test.html".

### Should Create Three Paragraphs Under Heading 1
Next lets add in something a little more difficult: We will create paragraphs based upon a list of strings.  For those familiar with Thymeleaf, this means using the data-th-each attribute.

    @Test
    public void shouldCreateThreeParagraphsUnderHeading1() throws Exception {
      Map<String,Object> model = new HashMap<String,Object>();
      model.put("section1Paragraphs", Arrays.asList("para 1", "para 2", "para 3"));
      HtmlElements tags = testEngine.process("/html/test.html", model);
      assertThat(tags.matching("h1~p").size() - tags.matching("h2~p").size(), equalTo(3)); 		
    }

The test above finds tags matching "h1 ~ p".  This means, find all "P" tags which are siblings of H1 and which come after the H1 element.  Similarly, "h2 ~ p" means find all "P" tags which are siblings of H2 and which follow the H2 tag.  We can calculate the number of P tags after the H1 but before the H2 by subtracting.

So the test above creates a list of 3 paragraphs, then tests to see if 3 paragraphs are output.

Run the test - and as expected we get a red bar.  The error message reads, "Expected 3 but was 2".

OK - we havent yet added in the code, so lets do that.

        <p data-th-each="para : ${section1Paragraphs}">Some text</p>
        <p>Some more text</p>

Run the test - and we get another red bar.  This time the error message reads, "Expected 3 but was 4".  If you are stuck at this point you could 'cheat' -- take a look at the HTML that is being created.  There are a couple of ways to do this.

1. String output = testEngine.toString("/html/test.html", model); System.out.println(output);
2. System.out.println(tags);

So what is being output? Lets take a look...

    <html>
      <head>
        <title></title>
      </head>
      <body>
        <h1></h1>
        <p>Some text</p>
        <p>Some text</p>
        <p>Some text</p>
        <p>Some more text</p>
        
        <h2>A sub-heading</h2>
        <p>Sub text</p>
        <p>More sub text</p>
        <a href="http://www.example.com/">A Link</a>
        
        <pre>Some i18n copy goes here</pre>
      </body>
    </html>

We have 3 copies of "Some text", plus the "Some more text" which was in the original file from our HTML author.  So we need to remove that last P.  Easy enough - add in a data-th-remove="all" attribute.

        <p data-th-each="para : ${section1Paragraphs}">Some text</p>
        <p data-th-remove="all">Some more text</p>

Run the test again: Green bar!

### Should Create Three Paragraphs Under Heading 1 With Expected Content
That's great but as we saw above, all we are testing for is how many paragraphs are output.  We have not tested for the CONTENT of each paragraph.  So lets add a test in for that now.

    @Test
    public void shouldCreateThreeParagraphsUnderHeading1WithExpectedContent() throws Exception {
      Map<String,Object> model = new HashMap<String,Object>();
      model.put("section1Paragraphs", Arrays.asList("para 1", "para 2", "para 3"));
      HtmlElements tags = testEngine.process("/html/test.html", model);
      assertThat(tags.matching("body > p:nth-of-type(-n+3)"), hasOnlyText("para 1", "para 2", "para 3"));
    }

Some more CSS3 here: we are asking for the first 3 "p" tags.  The p:nth-of-type(-n+3) will select them for us.
Now when we run the test it fails and we get the error message, 

    Expected 3 elements with text ["para 1", "para 2", "para 3"] but: ["some text", "Some text", "Some text"]

OK so we can add the text in now - with the data-th-text tag.

        <p data-th-each="para : ${section1Paragraphs}" data-th-text="${para}">Some text</p>

Run the test again - Green bar! The test passes.

### Should Use Resource Bundle Text in Pre Section
Often the copy on the website will come from i18n resource bundles.  These are usually handled by Spring MessageSource configurations.

Within Thymeleaf-TDD 1.0.4 you can test for these as well.  As always in TDD, the test comes first.

    @Test
    public void shouldUseTextFromResourceBundle_WhenPreTagRefersToMessageSourceKey() throws Exception {
        messageSource.givenMessageWithKey("my_resource_message", "Expected i18n copy");
        HtmlElements tags = testEngine.process(HTML_PATH_IN_TEST_RESOURCES_FOLDER, model);
        assertThat(tags.matching("body > pre"), isSingleElementThat(hasOnlyText("Expected i18n copy")));
    }

Run the test, and it should fail because the original HTML copy is being returned 

		<pre>Some i18n copy goes here</pre>
		
Now we can add the Thymeleaf attribute to the HTML source.

	<pre data-th-text="#{my_resource_message}">Some i18n copy goes here</pre>
	
Now when we run the test we get a 'green bar' - test has passed.

## A note on Hamcrest
These test assertions have been designed around the Hamcrest assertion model. For more information see http://hamcrest.org/JavaHamcrest/ and for a handy quick reference see http://www.marcphilipp.de/downloads/posts/2013-01-02-hamcrest-quick-reference/Hamcrest-1.3.pdf

Thymeleaf-TDD adds the following assertions, for testing against the Thymeleaf DOM,

    import static com.connect_group.thymeleaf.testing.hamcrest.ThymeleafMatchers.*;
    
* exists() returns true if any elements were found
* hasAttribute("href") returns true if the matched element has the named attribute
* hasAttribute("href", "value") returns true if the matched element has the named attribute with the given value
* hasChildren() returns true if the matched element has children
* hasClasses("class1 class2") returns true if the matched element has the named class or classes
* hasComment() returns true if the matched element has a HTML comment as a child
* hasCommentWithText("expected text") returns true if the matched element has a comment containing the expected text
* hasOnlyText("text") returns true if the matched element only contains Text and the text is as expected
* hasOnlyText("text1", "text2", ...) returns true if all of the matched elements contain only text, and the text is the sequence supplied
* isSingleElementThat() checks that the result is exactly one element and allows you to chain additional matchers.  For example, isSingleElementThat(hasClasses("myclass"))
* occursOnce() returns true if exactly one element was found
* hasTextBefore() tests the text at the beginning of an element, prior to its first child.
* hasTextAfter() tests the text at the end of an element, following its last child

## Thymesheet TDD
Up to now we have been testing Thymeleaf and that is the most common usage of Thymeleaf.  However Thymeleaf-TDD can also be used to test Thymesheet variants of Thymeleaf by including an external TSS stylesheet.

There are various methods of including the external file but one simple method is to locate a TSS file within the /src/main/webapp - e.g.

        <link rel="thymesheet" href="thymesheet.tss" />

So we get some HTML as follows,

    <html>
      <head>
        <title>Lorem Ipsum</title>
        <link rel="thymesheet" href="thymesheet.tss" />
      </head>
      <body>
        <h1>A heading</h1>
        <p>Some text</p>
        <p>Some more text</p>
        
        <h2>A sub-heading</h2>
        <p>Sub text</p>
        <p>More sub text</p>
        <a href="http://www.example.com/">A Link</a>
      </body>
    </html>

The same tests as described previously can still be applied, but instead of editing the HTML directly, we add rules to thymesheet.tss.

### Should Set Title To Expected Value
title { th-text: "${pageTitle}"; }

### Should Set Heading To Expected Value
h1 { th-text: "${heading1}"; }

### Should Create Three Paragraphs Under Heading 1
h1 + p { th-each: "para : ${section1Paragraphs}"; }
h1 + p + P { th-remove: "all"; }

### Should Create Three Paragraphs Under Heading 1 With Expected Content
h1 + p { th-each: "para : ${section1Paragraphs}"; th-text: "${para}";}


## Configuration
If your tests require properties you can create a /src/test/resources/test.properties file which will be picked up by the default Spring configuration supplied with thymeleaf-tdd.

Some specific properties are also supported in the file as follows,

* thymeleaf.template.webappPath -- defaults to src/main/webapp
* thymeleaf.template.cache.enabled -- defaults to true
* thymeleaf.template.mode -- defaults to LEGACYHTML5
* thymeleaf.template.characterEncoding -- defaults to UTF-8
