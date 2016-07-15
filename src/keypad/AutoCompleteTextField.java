package keypad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

/*
 * This class will not automatically do auto-complete, 
 * so when implemented, user must call:
 * <code>textField.getDocument().addDocumentListener()</code>  
 * then override 3 methods removeUpdate(), changedUpdate() and insertUpdate()
 * 
 * Example:
 * <code>
 * textField.getDocument().addDocumentListener(new DocumentListener() {
 *			@Override
 *			public void removeUpdate(DocumentEvent e) {}
 *			@Override
 *			public void changedUpdate(DocumentEvent e) {}
 *			@Override
 *			public void insertUpdate(DocumentEvent e) {
 *				textEdit.updateCompletions(e);
 *				for (String s : textEdit.getCompletions()) System.out.println(s);
 *			}
 *		});
 *	</code>	
 */
public class AutoCompleteTextField extends JTextField {
	
	private static final long serialVersionUID = 1L;
	
	private static final String _SPACE = " ";
	
	private TreeSet<String> dict;
	private List<String> completions;
	private int minNbOfLetter;
	
	public AutoCompleteTextField(String dictFileName, int nb) {
		super();
		dict = new TreeSet<>();
		completions = new ArrayList<>();
		minNbOfLetter = nb;
	}
	
	public List<String> getCompletions() {
		return completions;
	}
	
	public int getNbOfCompletions() {
		return completions.size();
	}
	
	public void updateDict(String word) {
		dict.add(word);
	}
	
	public void updateDict(TreeSet<String> set) {
		dict = set;
	}
	
	public void updateCompletions(DocumentEvent evt) {
		String text = getText();
		int changePos = evt.getOffset();
		String prefix;
		int lastSpacePos;
		if (text.contains(_SPACE)) {
			lastSpacePos = text.lastIndexOf(_SPACE);
			prefix = text.substring(lastSpacePos + 1);
		} else {
			lastSpacePos = -1;
			prefix = text;
		}
		if (changePos + evt.getLength() - lastSpacePos >= minNbOfLetter) completions = findCompletions(prefix);
		else completions.clear();
		System.out.println(changePos + evt.getLength() - lastSpacePos);
	}
	
	private List<String> findCompletions(String prefix) {
	    List<String> completions = new ArrayList<>();
	    Set<String> tailSet = dict.tailSet(prefix);
	    for (String tail : tailSet) {
	    	if (tail.startsWith(prefix)) completions.add(tail);
	    	else break;
	    }
	    return completions;
	}
}
