/*
 * Brandeis COSI 12b
 * PA7 - HTML Validator
 * HtmlTag class
 *
 * An HtmlTag object represents an HTML tag, such as <b> or </table>.
 *
 * @version 07/30/2021
 * @author BrandeisCOSI
 *
 * @version 08/10/2022
 * @author Qiuyang Wang
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class HtmlTag {
    // fields
	private String element;
	private boolean isTypeOpen;


    /** Constructs an HTML "opening" tag with the given element (like "table").
      * 
      * @param element String the text inside the tag, such as title or head.
      * @exception NullPointerException Throws a NullPointerException if element is null.*/
    public HtmlTag(String element) {
    	if (element == null) {
			throw new NullPointerException("Element cannot be null!");
		}
       this.element = element;
       this.isTypeOpen = true;
    }

    /** Constructs an HTML tag with the given element (like "table") and type.
      * Self-closing tags like <br /> are considered to be "opening" tags,
      * but return false from the requiresClosingTag method.
      * 
      * @param element   String the text inside the tag, such as title or head.
      * @param isOpenTag boolean a open tag is the tag without "/" in front of
      *                  its element. Self-closing tags are considered open.
      * @exception NullPointerException Throws a NullPointerException if element is null.*/
    public HtmlTag(String element, boolean isOpenTag) {
    	if (element == null) {
			throw new NullPointerException("Element cannot be null!");
		}
        if (isOpenTag) {
        	 this.element = element;
             this.isTypeOpen = true;
        } else {
        	this.element = element;
        	this.isTypeOpen = false;
        }
    }

    /** Returns true if this tag has the same element and type as the given other tag. 
     * 
     * @return boolean*/
    public boolean equals(Object o) {
    	if (o instanceof HtmlTag) {
			HtmlTag other = (HtmlTag) o;
			return (other.element.toLowerCase() == element.toLowerCase()) && (other.isTypeOpen == isTypeOpen);
		} else {
			return false;
		}
    }

    /** Returns this HTML tag's element, such as "table" or "p". 
     * 
     * @return element String the text inside the tag, such as title or head.*/
    public String getElement() {
    	return element;
    }

    /** Returns true if this HTML tag is an "opening" (starting) tag and false
      * if it is a closing tag.
      * Self-closing tags like <br /> are considered to be "opening" tags. 
      * 
      * @return boolean*/
    public boolean isOpenTag() {
        return isTypeOpen;
    }

    /** Returns true if the given other tag is non-null and matches this tag;
      * that is, if they have the same element but opposite types,
      * such as &lt;body&gt; and &lt;/body&gt;. 
      * 
      * @param other HtmlTag another HtmlTag object. 
      * @return boolean*/
    public boolean matches(HtmlTag other) {
        return (other.element != null) && (other.element.toLowerCase().equals(element.toLowerCase())) && (other.isTypeOpen != isTypeOpen);
    }

    /** Returns true if this tag does not requires a matching closing tag,
      * which is the case for certain elements such as br and img. 
      * 
      * @return boolean (true or false)*/
    public boolean isSelfClosing() {
        return SELF_CLOSING_TAGS.contains(element.toLowerCase()) && isTypeOpen;
    }

    /** Returns a string representation of this HTML tag, such as "&lt;/table&gt;". 
     * 
     * @return String in the format of "&lt;" + element +"&gt;".*/
    public String toString() {
    	if (this.isOpenTag()) {
    		return "<" + element + ">";    		
    	} else {
    		return "</" + element + ">";
    	}
    }



    // a set of tags that don't need to be matched (self-closing)
    private static final Set<String> SELF_CLOSING_TAGS = new HashSet<String>(
            Arrays.asList("!doctype", "!--", "!-- --", "area", "base", "basefont",
                          "br", "col", "frame", "hr", "img", "input",
                          "link", "meta", "param"));

    // all whitespace characters; used in text parsing
    private static final String WHITESPACE = " \f\n\r\t";

    /** Reads a string such as "&lt;table&gt;" or "&lt;/p&gt;" and converts it into an HtmlTag,
      * which is returned.
      * 
      * @param tagText String such as "&lt;table&gt;" or "&lt;/p&gt;."
      * @return HtmlTag converted from the input String.
      * @exception NullPointerException Throws a NullPointerException if element is null.*/
    public static HtmlTag parse(String tagText) {
    	if (tagText == null) {
			throw new NullPointerException("tagText cannot be null!");
		}	
    	String newTagText = tagText.substring(1, tagText.length() - 1);
    	if (newTagText.charAt(0) != '/' && newTagText.charAt(newTagText.length() - 1) != '/') {
    		return new HtmlTag(newTagText);
    	} else if (newTagText.charAt(newTagText.length() - 1) == '/') {
    		return new HtmlTag(newTagText.substring(0, newTagText.length()));
    	} else {
    		return new HtmlTag(newTagText.substring(1), false);
    	}
    }

    /** Reads the file or URL given, and tokenizes the text in that file,
      * placing the tokens into the given Queue.
      * Precondition: text != null and no &lt; or &gt; will appear inside tags' attributes.
      * 
      * @param text  String page text that contains the source code of a web or
      *              a HTML file.
      * @return tags LinkedList&lt;&gt; list that contains HtmlTag objects after
      *              processing and transforming from the input page text.*/
    public static LinkedList<HtmlTag> tokenize(String text) {
    	LinkedList<HtmlTag> tags = new LinkedList<HtmlTag>();
    	while(text.contains("<") && text.contains(">")) {
    		int start = text.indexOf("<");
    		int end = text.indexOf(">");
    		String tag = text.substring(start, end + 1);
    		tags.add(nextTag(new StringBuffer(tag)));
    		text = text.substring(end + 1);
    	}
    	return tags;
    }

    /**
     * advances to next tag in input;
     * not a perfect HTML tag tokenizer, but it will do for this PA
     * 
     * @param buf StringBuffer tag text contains elements and attributes.
     * @return HtmlTag objects created after fetching the element from the parameter.
     */
    private static HtmlTag nextTag(StringBuffer buf) {
    	boolean commentsFlag = true;
        int index1 = buf.indexOf("<");
        int index2 = buf.indexOf(">");

        if (index1 >= 0 && index2 > index1) {
            // check for HTML comments: <!-- -->
            if (index1 + 4 <= buf.length() && buf.substring(index1 + 1, index1 + 4).equals("!--")) {
                // a comment; look for closing comment tag -->
                index2 = buf.indexOf("-->", index1 + 4);
                if (index2 < 0) {
                    return null;
                } else {
                	commentsFlag = false;
                    buf.insert(index1 + 4, " ");    // fixes things like <!--hi-->
                    index2 += 3;    // advance to the closing >
                }
            }

            String element = buf.substring(index1 + 1, index2).trim();

            // remove attributes
            for (int i = 0; i < WHITESPACE.length(); i++) {
                int index3 = element.indexOf(WHITESPACE.charAt(i));
                if (index3 >= 0 && commentsFlag) {
                    element = element.substring(0, index3);
                } else if (!commentsFlag) {
                	element = "!-- --";
                	return new HtmlTag(element);
                }
            }

            // determine whether opening or closing tag
            boolean isOpenTag = true;
            if (element.indexOf("/") == 0) {
                isOpenTag = false;
                element = element.substring(1);
            }
            element = element.replaceAll("[^a-zA-Z0-9!-]+", "");

            buf.delete(0, index2 + 1);
            return new HtmlTag(element, isOpenTag);
        } else {
            return null;
        }
    }
}
