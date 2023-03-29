/**
* HtmlValidator Class is used for manipulating HtmlTag objects from
* its Queue and printing the tags with error handling.
* <p>
* This class was written for Programming Assignments 7
* in COSI 12B
* 
* @author Qiuyang Wang
* @version 0810
 */
import java.util.*;

public class HtmlValidator {
	//fields
	private Queue<HtmlTag> tags;
	
	/**
	 * Constructs a new HtmlValidator object with a empty Queue.
	 */
	public HtmlValidator() {
		tags = new LinkedList<HtmlTag>();
	}
	
	/**
	 * Constructs a new HtmlValidator object operating the given Queue.
	 * Precondition: there is no tag after &lt;/html&gt; tag.
	 * 
	 * @param tags Queue&lt;HtmlTag&gt; Queue contains HtmlTag objects.
	 * @exception IllegalArgumentException Throws a IllegalArgumentException if the parameter is null.
	 */
	public HtmlValidator(Queue<HtmlTag> tags) {
		if (tags == null) {
			throw new IllegalArgumentException("Requires valid parameter!");
		}
		this.tags = tags;
	}
	
	/**
	 * Adds a HtmlTag object into the end of this object's Queue tags.
	 * 
	 * @param tag HtmlTag the HtmlTag object wanted to be added in this object's Queue.
	 * @exception IllegalArgumentException Throws a IllegalArgumentException if the parameter is null.
	 */
	public void addTag(HtmlTag tag) {
		if (tag == null) {
			throw new IllegalArgumentException("Requires valid parameter!");
		}
		tags.add(tag);
	}
	
	/**
	 * Obtains this object's Queue tags which contains HtmlTag objects.
	 * 
	 * @return tags  Queue&lt;HtmlTag&gt;
	 */
	public Queue<HtmlTag> getTags() {
		return tags;
	}
	
	/**
	 * Removes all the HtmlTag objects with certain elements from this object's Queue tags.
	 * 
	 * @param element String targeted elements that will be removed
	 * @exception IllegalArgumentException Throws a IllegalArgumentException if the parameter is null.
	 */
	public void removeAll(String element) {
		if (element == null) {
			throw new IllegalArgumentException("Requires valid parameter!");
		}
		int originalSize = tags.size();
		for(int i = 0; i < originalSize; i++) {
			HtmlTag checkTag = tags.remove();
			if (!checkTag.getElement().equals(element)) {
				tags.add(checkTag);
			}
		}
	}
	
	/**
	 * Prints an indented text representation of the HTML tags in this object's queue
	 * and also prints error messages if encounters.
	 * Precondition: there is no tag after &lt;/html&gt; tag.
	 */
	public void validate() {
		//restores the Queue tags from being removed while processing.
		//Ensures the functionality of getTags() method.
		Queue<HtmlTag> cloneTags = new LinkedList<HtmlTag>();
		int size = tags.size();
		int count = 0;
		while(count != size) {
			HtmlTag temp = tags.remove();
			cloneTags.add(temp);
			tags.add(temp);
			count++;
		}
		
		//Start of printing tags and handling errors.
		Stack<HtmlTag> closeTag = new Stack<HtmlTag>();
		HtmlTag tag = cloneTags.remove();
		//avoid closeTag is empty
		while(closeTag.size() != 1) {
			if (tag.isSelfClosing()) {
				System.out.println(tag);
				tag = cloneTags.remove();
			} else {
				if (tag.isOpenTag()) {
					closeTag.push(tag);
					System.out.println(tag);								
				} else {
					System.out.println("ERROR unexpected tag: " + tag);
					tag = cloneTags.remove();
				}
			}			
		}
		//algorithm
		while (!cloneTags.isEmpty()) {
			tag = cloneTags.remove();
			if (tag.isSelfClosing()) {
				for (int i = 0; i < closeTag.size(); i++) {
					System.out.print("    ");
				}
			} else if (tag.isOpenTag()) {
				closeTag.push(tag);
				for (int i = 0; i < closeTag.size() - 1; i++) {
					System.out.print("    ");
				}
			} else {
				if (tag.matches(closeTag.peek())) {
					for (int i = 0; i < closeTag.size() - 1; i++) {
						System.out.print("    ");
					}
					closeTag.pop();
				} else {
					System.out.print("ERROR unexpected tag: ");
				}
			}
			System.out.println(tag);
		}
		if (closeTag.size() != 0) {
			while(!closeTag.isEmpty()) {				
				System.out.println("ERROR unclosed tag: " + closeTag.pop());
			}
		}
	}
}
