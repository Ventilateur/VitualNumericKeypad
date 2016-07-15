package keypad;

import autoComplete.AutoCompleteTextField;
import dictionaryManager.GetDictionary;
import dictionaryManager.Words;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Keypad extends JFrame {

	/// Class's constants ///
	// some additional constants
	private static final long serialVersionUID = 1L;
	private static final String _SPACE = " ";
	private static final int _AUTO_COMPLETE_TRIGGER_NB = 1;
	
	// dictionary's path
	private static final String _DICT_PATH = "\\data\\20k_most_common.txt";

	// screen's size
	private static final int _SCREEN_WIDTH  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int _SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	// window's size
	private static final int _WIN_WIDTH  = 600;
	private static final int _WIN_HEIGHT = 600;
	
	// gap between buttons and window's borders
	private static final int _UPPER_GAP = 10;
	private static final int _LEFT_GAP = 100;

	// gap between 2 main buttons
	private static final int _H_GAP = 5;
	private static final int _V_GAP = 5;
	
	// text fields' sizes
	private static final int _TEXT_EDIT_W = 220;
	private static final int _TEXT_EDIT_H = 20;
	private static final int _TEXT_DISP_W = 220;
	private static final int _TEXT_DISP_H = 150;
	
	// suggestion labels' sizes
	private static final int _SUGGESTION_LABEL_W = 100;
	private static final int _SUGGESTION_LABEL_H = 26;
	
	// main buttons' sizes
	private static final int _BUTTON_W  = 70;
	private static final int _BUTTON_H = 70;
	private static final int _BUTTON_ENTER_W = _BUTTON_W;
	private static final int _BUTTON_ENTER_H = 2 * _BUTTON_H + _V_GAP;
	private static final int _BUTTON_BACKSPACE_W = _BUTTON_W;
	private static final int _BUTTON_BACKSPACE_H = 2 * _BUTTON_H + _V_GAP;
	private static final int _BUTTON_SW_W = _BUTTON_W;
	private static final int _BUTTON_SW_H = 2 * _BUTTON_H + _V_GAP;
	private static final int _BUTTON_OK_W = _BUTTON_W;
	private static final int _BUTTON_OK_H = 2 * _BUTTON_H + _V_GAP; 
	
	// pop-up buttons' size
	private static final int _BUTTON_OP_W  = 100;
	private static final int _BUTTON_OP_H = 100;
	
	// number of main buttons and pop-up buttons
	private static final int _NB_OF_BUTTONS  = 12;
	private static final int _MAX_NB_OF_OP_BUTTONS = 4;
	private static final int _BUTTONS_PER_ROW = 3;
	private static final int _MAX_NB_OF_SUGGESTIONS = 5;
	
	// displayed texts for buttons
	private static final String b0Disp = "0. ,";
	private static final String b1Disp = "1!?-";
	private static final String b2Disp = "2abc";
	private static final String b3Disp = "3def";
	private static final String b4Disp = "4ghi";
	private static final String b5Disp = "5jkl";
	private static final String b6Disp = "6mno";
	private static final String b7Disp = "7pqrs";
	private static final String b8Disp = "8tuv";
	private static final String b9Disp = "9wxyz";
	private static final String bSharpDisp = "##";
	private static final String bAsteriskDisp = "**";
	private static final String[] buttonsTexts = {b1Disp, b2Disp, b3Disp, b4Disp, 
										   		  b5Disp, b6Disp, b7Disp, b8Disp, 
										   		  b9Disp, bAsteriskDisp, b0Disp, bSharpDisp};
	
	/// Class's constants ///


	private JLayeredPane layeredPane;

	private JTextArea textDisp;
	private AutoCompleteTextField textEdit;
	
	private JButton btnEnter;
	private JButton btnBackSpace;
	private JButton btnSW;
	private JButton btnOK;
	private List<JButton> buttons;
	private List<JButton> buttonsOnPress;
	private List<JLabel> suggestionLabels;
	
	private JButton btnOnPressChosen;
	private int labelNumberChosen;
    private int numberCompletions;
	
	private Rectangle btnEnterRect, btnBackSpaceRect, btnSWRect, btnOKRect;
	private Rectangle textDispRect, textEditRect;
	/// Class's properties
	

	/**
	 * Create the frame.
	 */
	public Keypad() {
		super("Virtual keypad v1.1");
		
		// setup main frame
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds((_SCREEN_WIDTH  - _WIN_WIDTH ) / 2, 
				  (_SCREEN_HEIGHT - _WIN_HEIGHT) / 2, 
				   _WIN_WIDTH, _WIN_HEIGHT);
		
		// initialize lists
		buttons = new ArrayList<>();
		buttonsOnPress = new ArrayList<>();
		suggestionLabels = new ArrayList<>();
		
		// initialize contentPane
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setLayout(new CardLayout(0, 0));
		
		// initialize layeredPane
		layeredPane = new JLayeredPane();
		layeredPane.setLayout(null);
		
		// setup components' positions and event handlers
		computeRectangles();
		setupButtonsPositions();
		setupButtonsTexts();
		setupButtonsHandlers();
		setupLayeredPaneHandler();
		setupSuggestionLabels();

        // initialize other properties
        btnOnPressChosen = null;
        labelNumberChosen = 0;
        numberCompletions = 0;

		// setup text fields
		setupTextFields();
		
		// setup dictionary and auto-completions
		textEdit.updateDict(GetDictionary.getCommonDictFromFile(_DICT_PATH));
		setupTextEditAutoComplete();
		
		// add all to main frame
		contentPane.add(layeredPane);
		setContentPane(contentPane);
	}

	private void setupTextFields() {
		// setup text edit field
		textEdit = new AutoCompleteTextField(_AUTO_COMPLETE_TRIGGER_NB);
		textEdit.setBounds(textEditRect);
		textEdit.setColumns(10);
		layeredPane.add(textEdit);
		layeredPane.moveToBack(textEdit);

		// setup text display field
		textDisp = new JTextArea();
		textDisp.setEditable(false);
		textDisp.setBounds(textDispRect);
		
		// setup scroll panel for containing text display
		JScrollPane scrollPane = new JScrollPane(textDisp);
		scrollPane.setViewportBorder(new CompoundBorder());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(textDispRect);
		layeredPane.add(scrollPane);
		layeredPane.moveToBack(scrollPane);
	}
	
	private void setupButtonsPositions() {
		int x = (int)btnSWRect.getMaxX() + _H_GAP;
		int y = (int)textEditRect.getMaxY() + _V_GAP;
		
		// setup number buttons' positions and sizes
		for (int i = 0; i < _NB_OF_BUTTONS; i++) {
			buttons.add(new JButton());
			buttons.get(i).setBounds(x, y, _BUTTON_W, _BUTTON_H);
			layeredPane.add(buttons.get(i));
			layeredPane.moveToBack(buttons.get(i));
			if ((i + 1) % _BUTTONS_PER_ROW == 0) {
				x = (int)btnSWRect.getMaxX() + _H_GAP;
				y += _V_GAP + _BUTTON_H;
			} else x += _H_GAP + _BUTTON_W;
		}
		
		// setup on-press buttons' sizes
		for (int i = 0; i < _MAX_NB_OF_OP_BUTTONS; i++) {
			buttonsOnPress.add(new JButton());
			buttonsOnPress.get(i).setSize(_BUTTON_OP_W, _BUTTON_OP_H);
		}
		
		// setup enter button position and size
		btnEnter = new JButton();
		btnEnter.setBounds(btnEnterRect);
		layeredPane.add(btnEnter);
		layeredPane.moveToBack(btnEnter);
		
		// setup backspace button position and size
		btnBackSpace = new JButton();
		btnBackSpace.setBounds(btnBackSpaceRect);
		layeredPane.add(btnBackSpace);
		layeredPane.moveToBack(btnBackSpace);
		
		// setup change button position and size
		btnSW = new JButton();
		btnSW.setBounds(btnSWRect);
		layeredPane.add(btnSW);
		layeredPane.moveToBack(btnSW);
		
		// setup validate button position and size
		btnOK = new JButton();
		btnOK.setBounds(btnOKRect);
		layeredPane.add(btnOK);
		layeredPane.moveToBack(btnOK);
	}
	
	private void setupButtonsTexts() {
		for (int i = 0; i < _NB_OF_BUTTONS; i++) {
			String txt = "<html><center>" + buttonsTexts[i].charAt(0) + "<br>" + buttonsTexts[i].substring(1) + "</center></html>";
			buttons.get(i).setText(txt);
		}
		btnEnter.setText("<html><center>ENTER</center></html>");
		btnBackSpace.setText("<html><center>BACK<br>SPACE</center></html>");
		btnSW.setText("<html><center>SW</center></html>");
		btnOK.setText("<html><center>OK</center></html>");
	}
	
	private void setupSuggestionLabels() {
		for (int i = 0; i < _MAX_NB_OF_SUGGESTIONS; i++) {
			suggestionLabels.add(new JLabel());
			suggestionLabels.get(i).setBounds((int)textDispRect.getMaxX() + _H_GAP,
											  _UPPER_GAP + i * (_SUGGESTION_LABEL_H + _V_GAP),
											  _SUGGESTION_LABEL_W,
											  _SUGGESTION_LABEL_H);
			suggestionLabels.get(i).setBackground(Color.MAGENTA);
			suggestionLabels.get(i).setOpaque(false);
			layeredPane.add(suggestionLabels.get(i));
			layeredPane.moveToBack(suggestionLabels.get(i));
		}
	}
	
	private void setupTextEditAutoComplete() {
		textEdit.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent evt) {}
			@Override
			public void changedUpdate(DocumentEvent evt) {}
			@Override
			public void insertUpdate(DocumentEvent evt) {
				labelNumberChosen = 0;
				textEdit.updateCompletions(evt);
				numberCompletions = textEdit.getNbOfCompletions() <= _MAX_NB_OF_SUGGESTIONS ?
                                textEdit.getNbOfCompletions() : _MAX_NB_OF_SUGGESTIONS;
				if (numberCompletions != 0) {
				    List<Words> suggestionList = textEdit.getCompletions();
					for (int i = 0; i < _MAX_NB_OF_SUGGESTIONS; i++) {
						if (i < numberCompletions) {
						    Words mostCommonWord = getMostCommonWord(suggestionList);
                            suggestionList.remove(mostCommonWord);
							String text = mostCommonWord.getWord();
							suggestionLabels.get(i).setText(text);
							suggestionLabels.get(i).setOpaque(false);
						} else suggestionLabels.get(i).setText(null);
					}
					suggestionLabels.get(labelNumberChosen).setOpaque(true);
				} else for (JLabel label : suggestionLabels) {
					label.setText(null);
					label.setOpaque(false);
				}
				// System.out.println(textEdit.getNbOfCompletions());
			}
		});
	}

	private Words getMostCommonWord(List<Words> list) {
	    int max = Integer.MAX_VALUE;
        Words mostCommonWord = list.get(0);
        for (Words word : list) {
            if (word.getRank() < max) {
                max = word.getRank();
                mostCommonWord = word;
            }
        }
        return mostCommonWord;
    }
	
	private void setupLayeredPaneHandler() {
		layeredPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) btnOK.doClick();
			}
		});
	}
	
	private void setupButtonsHandlers() {
		// number buttons
		for (JButton button : buttons) {
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {}

				// on-press event handler
				@Override
				public void mousePressed(MouseEvent evt) {
					if (evt.getButton() == MouseEvent.BUTTON1) {
						JButton btn = (JButton) evt.getSource();
						btn.setVisible(false);
						// some mathematical shits, the goal is to centralize all pop-up buttons
						int numberOfButtons = buttonsTexts[buttons.indexOf(btn)].length() - 1;
						Point center = new Point(btn.getLocation().x + (_BUTTON_W - _BUTTON_OP_W) / 2,
												 btn.getLocation().y + (_BUTTON_H - _BUTTON_OP_H) / 2);
						double firstX = center.x - (numberOfButtons - 1) * _BUTTON_OP_W / 2;
						// display on-press buttons and their texts
						for (int i = 0; i < numberOfButtons; i++) {
							JButton btnTemp = buttonsOnPress.get(i);
							String txt = String.valueOf(buttonsTexts[buttons.indexOf(btn)].charAt(i + 1));
							btnTemp.setText(txt);
							double x = firstX + i * _BUTTON_OP_W;
							double y = center.y;
							btnTemp.setLocation((int) x, (int) y);
							layeredPane.add(btnTemp);
							layeredPane.moveToFront(btnTemp);
						}
						validate();
						repaint();
					}
				}

				// on-release event handler
				@Override
				public void mouseReleased(MouseEvent evt) {
					JButton btn = (JButton)evt.getSource();
					btn.setVisible(true);
					if (btnOnPressChosen != null) {
						textEdit.setText(textEdit.getText() + btnOnPressChosen.getText());
					}
					for (JButton btnTemp : buttonsOnPress) layeredPane.remove(btnTemp);
					validate(); repaint();
				}
			});
		}
		
		// on-press buttons
		for (JButton button_op : buttonsOnPress) {
			button_op.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {}
				// on-hover event handlers
				@Override
				public void mouseEntered (MouseEvent evt) {
					btnOnPressChosen = (JButton)evt.getSource();
				}
				@Override
				public void mouseExited (MouseEvent evt) {
					btnOnPressChosen = null;
				}
			});
		}
		
		// enter button
		btnEnter.addActionListener(listener -> {
            textDisp.append(textEdit.getText() + "\r\n");
            textEdit.setText(null);
        });
		
		// backspace button
		btnBackSpace.addActionListener(listener -> {
            String text = textEdit.getText();
            if (text.length() == 1) textEdit.setText(null);
            else if (text.length() > 1) {
                text = text.substring(0, text.length() -1);
                textEdit.setText(text);
            }
        });
		
		// switch button
		btnSW.addActionListener(listener -> {
            if (labelNumberChosen < numberCompletions - 1) labelNumberChosen++;
            else labelNumberChosen = 0;
            for (JLabel label : suggestionLabels) label.setOpaque(false);
            suggestionLabels.get(labelNumberChosen).setOpaque(true);
            validate(); repaint();
        });
		
		// OK button
		btnOK.addActionListener(listener -> {
            String text = textEdit.getText();
            // get the latest word after space and fill it
            if (text.contains(_SPACE) && suggestionLabels.get(labelNumberChosen).getText() != null) {
                text = text.substring(0, text.lastIndexOf(_SPACE) + 1);
                text += suggestionLabels.get(labelNumberChosen).getText();
            } else text = suggestionLabels.get(labelNumberChosen).getText();
            // update text field
            textEdit.setText(text);
            for (JLabel label : suggestionLabels) label.setOpaque(false);
            suggestionLabels.get(labelNumberChosen).setOpaque(true);
            validate(); repaint();
        });
	}

	// compute sizes and positions of components, just for readability
	private void computeRectangles() {

		textDispRect = new Rectangle(_LEFT_GAP + _BUTTON_W + _H_GAP, 
							   	     _UPPER_GAP, 
							   	     _TEXT_DISP_W, 
							   	     _TEXT_DISP_H);

		textEditRect = new Rectangle(_LEFT_GAP + _BUTTON_W + _H_GAP, 
							   	     (int)textDispRect.getMaxY() + _V_GAP, 
							   	     _TEXT_EDIT_W, 
							   	     _TEXT_EDIT_H);

		btnBackSpaceRect = new Rectangle(_LEFT_GAP + _BUTTONS_PER_ROW * (_BUTTON_W + _H_GAP) + _BUTTON_SW_W + _H_GAP,
								   	     (int)textEditRect.getMaxY() + _V_GAP,
								   	     _BUTTON_BACKSPACE_W, 
								   	     _BUTTON_BACKSPACE_H);
		
		btnEnterRect = new Rectangle(_LEFT_GAP + _BUTTONS_PER_ROW * (_BUTTON_W + _H_GAP) + _BUTTON_OK_W + _H_GAP,
							   		 (int)btnBackSpaceRect.getMaxY() + _V_GAP,
							   		 _BUTTON_ENTER_W, 
							   		 _BUTTON_ENTER_H);
		
		
		btnSWRect = new Rectangle(_LEFT_GAP,
								  (int)textEditRect.getMaxY() + _V_GAP,
								  _BUTTON_SW_W,
								  _BUTTON_SW_H);
		
		btnOKRect = new Rectangle(_LEFT_GAP,
								  (int)btnSWRect.getMaxY() + _V_GAP,
								  _BUTTON_OK_W,
								  _BUTTON_OK_H);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            try {
                Keypad frame = new Keypad();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}
}
