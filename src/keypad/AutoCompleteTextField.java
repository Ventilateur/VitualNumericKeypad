package keypad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AutoCompleteTextField extends JTextField implements DocumentListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final char _SPACE = ' ';
	private static final int _MIN_NUMBER_OF_LETTER = 2;
	
	private TreeSet<String> dict;
	private List<String> completions;
	
	public AutoCompleteTextField(String dictFileName) {
		super();
		dict = new TreeSet<String>();
		completions = new ArrayList<String>();
		getDocument().addDocumentListener(this);
	}
	
	public List<String> getCompletions() {
		return completions;
	}
	
	public void updateDict(String word) {
		dict.add(word);
	}
	
	public void updateDict(TreeSet<String> set) {
		dict = set;
	}
	
	private List<String> findCompletions(String prefix) {
	    List<String> completions = new ArrayList<String>();
	    Set<String> tailSet = dict.tailSet(prefix);
	    for (String tail : tailSet) {
	    	if (tail.startsWith(prefix)) completions.add(tail);
	    	else break;
	    }
	    return completions;
	}
	
	@Override
	public void changedUpdate(DocumentEvent evt) {
	}

	@Override
	public void removeUpdate(DocumentEvent evt) {
	}

	@Override
	public void insertUpdate(DocumentEvent evt) {
		if (evt.getLength() == 1) {
			int changePos = evt.getOffset();
			int lastSpacePos;
			for (lastSpacePos = changePos; lastSpacePos >= 0; lastSpacePos--)
				if (getText().charAt(lastSpacePos) == _SPACE) break;
			if (changePos - lastSpacePos < _MIN_NUMBER_OF_LETTER) return;
			String prefix = getText().substring(lastSpacePos + 1).toLowerCase();
			completions = findCompletions(prefix);
		}
	}

	
}
