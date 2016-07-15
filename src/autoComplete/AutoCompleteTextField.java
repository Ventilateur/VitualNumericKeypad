package autoComplete;

import dictionaryManager.Words;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
	
	private TreeSet<Words> dict;
	private List<Words> completions;
	private int minNbOfLetter;
	
	public AutoCompleteTextField(int nb) {
		super();
		dict = new TreeSet<>();
		completions = new ArrayList<>();
		minNbOfLetter = nb;
	}
	
	public List<Words> getCompletions() {
		return completions;
	}
	
	public int getNbOfCompletions() {
		return completions.size();
	}

	public void updateDict(TreeSet<Words> set) {
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
	}
	
	private List<Words> findCompletions(String prefix) {
	    List<Words> completions = new ArrayList<>();
	    Set<Words> tailSet = dict.tailSet(new Words(prefix, 0));
	    for (Words tail : tailSet) {
	    	if (tail.getWord().startsWith(prefix)) completions.add(tail);
	    	else break;
	    }
	    return completions;
	}

}
