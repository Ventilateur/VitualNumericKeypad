package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class Test1 extends JFrame {

	private JPanel contentPane;
	private TreeSet<String> lines;
	private JTextField textField;

	public boolean matchPrefix(String prefix) {
		Set<String> tailSet = lines.tailSet(prefix);
		for (String tail : tailSet) {
			if (tail.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}
	
	public List<String> findCompletions(String prefix) {
	    List<String> completions = new ArrayList<String>();
	    Set<String> tailSet = lines.tailSet(prefix);
	    for (String tail : tailSet) {
	      if (tail.startsWith(prefix)) {
	        completions.add(tail);
	      } else {
	        break;
	      }
	    }
	    return completions;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test1 frame = new Test1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Test1() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textField = new JTextField();
		contentPane.add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		lines = new TreeSet<String>();
		String path = System.getProperty("user.dir");
		String line;
		try {
			File file = new File(path + "\\data\\dictionary.txt");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, Charset.defaultCharset());
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		textField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
			}
			
			@Override
			public void insertUpdate(DocumentEvent evt) {
				if (evt.getLength() == 1) {
					int changePos = evt.getOffset();
					int lastSpacePos;
					for (lastSpacePos = changePos; lastSpacePos >= 0; lastSpacePos--)
						if (textField.getText().charAt(lastSpacePos) == ' ') break;
					if (changePos - lastSpacePos < 1) return;
					String prefix = textField.getText().substring(lastSpacePos + 1).toLowerCase();
					for (String str : findCompletions(prefix)) {
						textArea.append(str + "\r\n");
					}
				}
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
	}

}
