
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.*;
import java.util.*;
import org.junit.*;


// Remember: you have to add JUnit library to your build path in 
// order for this to work!

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@FixMethodOrder(MethodSorters.DEFAULT)

public class JUnitTestsPA7_students {

	/*
	 * Confirming closing tag interpreted properly
	 */
	@Test
	public void HtmlTag_01_TestClosingTag(){
	
		HtmlValidator validator = new HtmlValidator(HtmlTag.tokenize("</html>"));
		Queue<HtmlTag> tag = validator.getTags();
		assertEquals("</html>", tag.remove().toString());

	}
	/*
	 * Confirming self closing tag interpreted properly
	 */
	@Test
	public void HtmlTag_02_TestSelfClosingTagTrue(){
	
		HtmlValidator validator = new HtmlValidator(HtmlTag.tokenize("<br>"));
		Queue<HtmlTag> tag = validator.getTags();
		assertEquals(true, tag.remove().isSelfClosing());

	}
	/*
	 * Confirming self closing tag interpreted properly
	 */
	@Test
	public void HtmlTag_03_TestSelfClosingTagFlase(){
	
		HtmlValidator validator = new HtmlValidator(HtmlTag.tokenize("<html>"));
		Queue<HtmlTag> tag = validator.getTags();
		assertEquals(false, tag.remove().isSelfClosing());

	}
	
	
	/*
	 * HtmlValidator positively verifies a correct file
	 */
	@Test
	public void HtmlValidator_00_ValidTextPAExampleVerification(){
		String expectedResults= "<!doctype>" + System.lineSeparator() + 
				"<html>" + System.lineSeparator() + 
				"    <!-- -->" + System.lineSeparator() + 
				"    <head>" + System.lineSeparator() + 
				"        <title>" + System.lineSeparator() + 
				"        </title>" + System.lineSeparator() + 
				"    </head>" + System.lineSeparator() + 
				"    <body>" + System.lineSeparator() + 
				"        <p>" + System.lineSeparator() + 
				"            <br>" + System.lineSeparator() + 
				"            <a>" + System.lineSeparator() + 
				"            </a>" + System.lineSeparator() + 
				"        </p>" + System.lineSeparator() + 
				"        <p>" + System.lineSeparator() + 
				"            <img>" + System.lineSeparator() + 
				"        </p>" + System.lineSeparator() + 
				"    </body>" + System.lineSeparator() + 
				"</html>" + System.lineSeparator() ;
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		String text= "<!doctype html>" + System.lineSeparator() +
				"	 <html>" + System.lineSeparator() +
				"		<!-- This is a comment --> " + System.lineSeparator() +
				"		<head>" + System.lineSeparator() + 
				"			<title>Turtles are cool</title>" + System.lineSeparator() +
				"		</head>" + System.lineSeparator() +
				"		<body>" + System.lineSeparator() +
				"			<p><br/>Turtles swim in the <a href=\"ocean.com\">ocean</a>.</p>" + System.lineSeparator() + 
				"			<p>Some turtles are over 100 years old." + System.lineSeparator() + 
				"			Here is a picture of a turtle:" + System.lineSeparator() + 
				"			<img src=\"turtle.jpg\" /> </p>" + System.lineSeparator() +
				"    </body>"+System.lineSeparator() +
				"	</html>"+System.lineSeparator();
		HtmlValidator alpha=new HtmlValidator(HtmlTag.tokenize(text));
		alpha.validate();
		assertEquals(expectedResults, outContent.toString());
	}

	
	
	/*
	 * Confirm the existence and correct function of HtmlValidator's second constructor
	 * Confirming that Empty queue is allowed as a parameter 
	 */
	@Test
	public void HtmlValidator_04_Constructor2_Test2(){
	
		HtmlValidator validator = new HtmlValidator(HtmlTag.tokenize(""));
		Queue<HtmlTag> tag = validator.getTags();
		assertEquals(0, tag.size());

	}
	/*
	 * Confirming comments interpreted properly
	 */
	@Test
	public void HtmlValidator_05_CommentsTest(){
	
		HtmlValidator validator = new HtmlValidator(HtmlTag.tokenize("<!-- test -->"));
		Queue<HtmlTag> tag = validator.getTags();
		assertEquals("<!-- -->", tag.remove().toString());

	}
	
	/*
	 * Confirm that tags are treated as case insensitive without changing the 
	 * actual case of the tags
	 */
	
	@Test
	public void HtmlValidator_10_CaseSensitivityTest(){
		String expectedResults= "<!doctype>" + System.lineSeparator() + 
				"<html>" + System.lineSeparator() + 
				"    <!-- -->" + System.lineSeparator() + 
				"    <head>" + System.lineSeparator() + 
				"        <title>" + System.lineSeparator() + 
				"        </title>" + System.lineSeparator() + 
				"    </head>" + System.lineSeparator() + 
				"    <body>" + System.lineSeparator() + 
				"        <P>" + System.lineSeparator() + 
				"        </p>" + System.lineSeparator() + 
				"    </body>" + System.lineSeparator() + 
				"</html>" + System.lineSeparator(); 
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		String text= "<!doctype html>" + System.lineSeparator() + 
				"	 <html>" + System.lineSeparator() + 
				"		<!-- This is a comment -->" + System.lineSeparator() + 
				"		<head>" + System.lineSeparator() + 
				"			<title>Case sensitivity test</title>" + System.lineSeparator() + 
				"		</head>" + System.lineSeparator() + 
				"		<body>" + System.lineSeparator() + 
				"			<P>Tags of different case sensitivity are matched. Case sensitivity is preserved.</p>" + System.lineSeparator() + 
				"    </body>" + System.lineSeparator() + 
				"	</html>" + System.lineSeparator() + 
				"";
		HtmlValidator alpha=new HtmlValidator(HtmlTag.tokenize(text));
		alpha.validate();
		assertEquals(expectedResults, outContent.toString());
	}
	
}
