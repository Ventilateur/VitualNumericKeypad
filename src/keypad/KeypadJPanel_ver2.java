package keypad;

import autoComplete.AutoCompleteTextField;
import dictionaryManager.GetDictionary;
import dictionaryManager.Words;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class KeypadJPanel_ver2 extends JPanel {

	/// Class's constants ///
	// some additional constants
	private static final long serialVersionUID = 1L;
	private static final String _SPACE = " ";
	private static final int _AUTO_COMPLETE_TRIGGER_NB = 1;

	// dictionary's path
	private static final String _DICT_PATH_WINDOWS = "\\data\\20k_most_common.txt";
	private static final String _DICT_PATH_LINUX = "/data/20k_most_common.txt";

	// screen's size
	private static final int _SCREEN_WIDTH  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int _SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	// window's size
	private static final int _WIN_WIDTH  = 750;
	private static final int _WIN_HEIGHT = 650;
    private static final Rectangle _FRAME_RECT = new Rectangle((_SCREEN_WIDTH  - _WIN_WIDTH ) / 2,
                                                               (_SCREEN_HEIGHT - _WIN_HEIGHT) / 2,
                                                               _WIN_WIDTH, _WIN_HEIGHT);

	// gap between buttons and window's borders
	private static final int _UPPER_GAP = 10;
	private static final int _LEFT_GAP = 100;

	// gap between 2 main buttons
	private static final int _H_GAP = 5;
	private static final int _V_GAP = 5;

	// suggestion labels' sizes
	private static final int _SUGGESTION_LABEL_W = 100;
	private static final int _SUGGESTION_LABEL_H = 26;

	// main buttons' sizes
	private static final int _BUTTON_W  = 80;
	private static final int _BUTTON_H = 80;
	private static final int _BUTTON_ENTER_W = _BUTTON_W;
	private static final int _BUTTON_ENTER_H = 2 * _BUTTON_H + _V_GAP;
	private static final int _BUTTON_BACKSPACE_W = _BUTTON_W;
	private static final int _BUTTON_BACKSPACE_H = 2 * _BUTTON_H + _V_GAP;
	private static final int _BUTTON_SW_W = _BUTTON_W;
	private static final int _BUTTON_SW_H = 2 * _BUTTON_H + _V_GAP;
	private static final int _BUTTON_OK_W = _BUTTON_W;
	private static final int _BUTTON_OK_H = 2 * _BUTTON_H + _V_GAP;

    // text fields' sizes
    private static final int _TEXT_EDIT_W = 3 * (_BUTTON_W + _H_GAP) - _H_GAP;
    private static final int _TEXT_EDIT_H = 20;
    private static final int _TEXT_DISP_W = 3 * (_BUTTON_W + _H_GAP) - _H_GAP;
    private static final int _TEXT_DISP_H = 120;

	// pop-up buttons' size
	private static final int _BUTTON_OP_W  = 100;
	private static final int _BUTTON_OP_H = 100;

	// number of main buttons and pop-up buttons
	private static final int _NB_OF_BUTTONS  = 12;
	private static final int _NB_OF_OP_BUTTONS = 5;
	private static final int _BUTTONS_PER_ROW = 3;
	private static final int _MAX_NB_OF_SUGGESTIONS = 5;

    // binding number for arrow keys
    private static final int _UP     = 0;
    private static final int _LEFT   = 1;
    private static final int _CENTER = 2;
    private static final int _RIGHT  = 3;
    private static final int _DOWN   = 4;

	// displayed texts for buttons
	private static final String b0Disp          = "0( )/";
	private static final String b1Disp          = "1!?.,";
	private static final String b2Disp          = "2abc@";
	private static final String b3Disp          = "3def#";
	private static final String b4Disp          = "4ghi$";
	private static final String b5Disp          = "5jkl%";
	private static final String b6Disp          = "6mno^";
	private static final String b7Disp          = "7pqrs";
	private static final String b8Disp          = "8tuv*";
	private static final String b9Disp          = "9wxyz";
	private static final String bSharpDisp      = "#+=;&";
    private static final String bAsteriskDisp   = "*[]{}";
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
    private JPanel panelOnPress;
	private List<JLabel> suggestionLabels;

	private JButton btnOnPressChosen;
	private int labelNumberChosen;
    private int numberCompletions;

	private Rectangle btnEnterRect, btnBackSpaceRect, btnSWRect, btnOKRect;
	private Rectangle textDispRect, textEditRect;

    private boolean isUp, isDown, isLeft, isRight;
	/// Class's properties

    /**
	 * Create the frame.
	 */
	public KeypadJPanel_ver2() {
		
		// setup main frame
		setBounds(_FRAME_RECT);
		
		// initialize lists
		buttons = new ArrayList<>(_NB_OF_BUTTONS);
		buttonsOnPress = new ArrayList<>(_NB_OF_OP_BUTTONS);
		suggestionLabels = new ArrayList<>(_MAX_NB_OF_SUGGESTIONS);
		
		// initialize contentPane
        panelOnPress = new JPanel();
		setOpaque(true);
        setLayout(new CardLayout(0, 0));
		
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
		if (System.getProperty("os.name").startsWith("Windows"))
		    textEdit.updateDict(GetDictionary.getCommonDictFromFile(_DICT_PATH_WINDOWS));
        else
            textEdit.updateDict(GetDictionary.getCommonDictFromFile(_DICT_PATH_LINUX));
		setupTextEditAutoComplete();
		
		// add all to main frame
		add(layeredPane);
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
			buttons.get(i).setBorder(BorderFactory.createRaisedBevelBorder());
			layeredPane.add(buttons.get(i));
			layeredPane.moveToBack(buttons.get(i));
			if ((i + 1) % _BUTTONS_PER_ROW == 0) {
				x = (int)btnSWRect.getMaxX() + _H_GAP;
				y += _V_GAP + _BUTTON_H;
			} else x += _H_GAP + _BUTTON_W;
		}
		
		// setup on-press buttons' sizes
        panelOnPress.setSize(new Dimension(3 * _BUTTON_OP_W + 2 * _H_GAP, 3 * _BUTTON_OP_H + 2 * _V_GAP));
        panelOnPress.setLayout(new GridBagLayout());
        panelOnPress.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

		for (int i = 0; i < _NB_OF_OP_BUTTONS; i++) {
			buttonsOnPress.add(new JButton());
            switch (i) {
                case 0:
                    constraints.gridx = 1;
                    constraints.gridy = 0;
                    break;
                case 1:
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    break;
                case 2:
                    constraints.gridx = 1;
                    constraints.gridy = 1;
                    break;
                case 3:
                    constraints.gridx = 2;
                    constraints.gridy = 1;
                    break;
                case 4:
                    constraints.gridx = 1;
                    constraints.gridy = 2;
                    break;
                default:
                    break;
            }
            buttonsOnPress.get(i).setPreferredSize(new Dimension(_BUTTON_OP_W, _BUTTON_OP_H));
            buttonsOnPress.get(i).setBorder(BorderFactory.createRaisedBevelBorder());
            panelOnPress.add(buttonsOnPress.get(i), constraints);
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
        layeredPane.setFocusable(true);
        isUp = isDown = isLeft = isRight = false;
		layeredPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                for (JButton btn : buttonsOnPress) btn.setBorder(BorderFactory.createRaisedBevelBorder());
                switch (evt.getKeyCode()) {
                    case (KeyEvent.VK_UP):
                        isUp = true;
                        if (!(isDown || isLeft || isRight)) {
                            btnOnPressChosen = buttonsOnPress.get(_UP);
                        }
                        break;
                    case (KeyEvent.VK_DOWN):
                        isDown = true;
                        if (!(isUp || isLeft || isRight)) {
                            btnOnPressChosen = buttonsOnPress.get(_DOWN);
                        }
                        break;
                    case (KeyEvent.VK_LEFT):
                        isLeft = true;
                        if (!(isUp || isDown || isRight)) {
                            btnOnPressChosen = buttonsOnPress.get(_LEFT);
                        }
                        break;
                    case (KeyEvent.VK_RIGHT):
                        isRight = true;
                        if (!(isUp || isDown || isLeft)) {
                            btnOnPressChosen = buttonsOnPress.get(_RIGHT);
                        }
                        break;
                    default:
                        break;
                }
                btnOnPressChosen.setBorder(BorderFactory.createLoweredBevelBorder());
            }

            @Override
            public void keyReleased(KeyEvent evt) {
                switch (evt.getKeyCode()) {
                    case (KeyEvent.VK_UP):
                        isUp = false;
                        buttonsOnPress.get(_UP).setBorder(BorderFactory.createRaisedBevelBorder());
                        break;
                    case (KeyEvent.VK_DOWN):
                        isDown = false;
                        buttonsOnPress.get(_DOWN).setBorder(BorderFactory.createRaisedBevelBorder());
                        break;
                    case (KeyEvent.VK_LEFT):
                        isLeft = false;
                        buttonsOnPress.get(_LEFT).setBorder(BorderFactory.createRaisedBevelBorder());
                        break;
                    case (KeyEvent.VK_RIGHT):
                        isRight = false;
                        buttonsOnPress.get(_RIGHT).setBorder(BorderFactory.createRaisedBevelBorder());
                        break;
                    default:
                        break;
                }
                if (!(isUp || isDown || isLeft || isRight)) {
                    btnOnPressChosen = buttonsOnPress.get(_CENTER);
                    btnOnPressChosen.setBorder(BorderFactory.createLoweredBevelBorder());
                }
            }
        });
	}
	
	private void setupButtonsHandlers() {
		// number buttons
		for (JButton button : buttons) {
			button.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(MouseEvent evt) {
                    JButton btn = (JButton) evt.getSource();
                    btn.setBorder(BorderFactory.createLoweredBevelBorder());
                    validate();
                    repaint();
                }
				@Override
				public void mouseExited(MouseEvent evt) {
                    JButton btn = (JButton) evt.getSource();
                    btn.setBorder(BorderFactory.createRaisedBevelBorder());
                    validate();
                    repaint();
				}

				// on-press event handler
				@Override
				public void mousePressed(MouseEvent evt) {
					if (evt.getButton() == MouseEvent.BUTTON1) {
						JButton btn = (JButton) evt.getSource();
						btn.setVisible(false);
						// display on-press buttons and their texts
						for (int i = 0; i < _NB_OF_OP_BUTTONS; i++) {
							JButton btnTemp = buttonsOnPress.get(i);
                            String txt;
                            try {
                                txt = String.valueOf(buttonsTexts[buttons.indexOf(btn)].charAt(i));
                            } catch (IndexOutOfBoundsException e) {
                                txt = "";
                            }
							btnTemp.setText(txt);
						}
                        btnOnPressChosen = buttonsOnPress.get(_CENTER);
                        btnOnPressChosen.setBorder(BorderFactory.createLoweredBevelBorder());
						panelOnPress.setLocation(btn.getLocation().x + (_BUTTON_W - panelOnPress.getSize().width) / 2,
                                                 btn.getLocation().y + (_BUTTON_H - panelOnPress.getSize().height) / 2);
						layeredPane.add(panelOnPress);
                        layeredPane.moveToFront(panelOnPress);
						validate();
						repaint();
					}
				}

				// on-release event handler
				@Override
				public void mouseReleased(MouseEvent evt) {
					JButton btn = (JButton)evt.getSource();
					btn.setVisible(true);
                    Point pos = evt.getPoint();
                    pos.x += btn.getLocation().x;
                    pos.y += btn.getLocation().y;
                    if (!panelOnPress.getBounds().contains(pos)) btnOnPressChosen = null;
					if (btnOnPressChosen != null) textEdit.setText(textEdit.getText() + btnOnPressChosen.getText());
					for (JButton btnTemp : buttonsOnPress) btnTemp.setBorder(BorderFactory.createRaisedBevelBorder());
                    layeredPane.remove(panelOnPress);
					validate();
                    repaint();
				}
			});
		}
		
		// enter button
		btnEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textDisp.append(textEdit.getText() + "\r\n");
                textEdit.setText(null);
                layeredPane.requestFocusInWindow();
            }
        });
		
		// backspace button
		btnBackSpace.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text = textEdit.getText();
                if (text.length() == 1) textEdit.setText(null);
                else if (text.length() > 1) {
                    text = text.substring(0, text.length() - 1);
                    textEdit.setText(text);
                }
                layeredPane.requestFocusInWindow();
            }
        });
		
		// switch button
		btnSW.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (labelNumberChosen < numberCompletions - 1) labelNumberChosen++;
                else labelNumberChosen = 0;
                for (JLabel label : suggestionLabels) label.setOpaque(false);
                suggestionLabels.get(labelNumberChosen).setOpaque(true);
                layeredPane.requestFocusInWindow();
                validate();
                repaint();
            }
        });
		
		// OK button
		btnOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
                layeredPane.requestFocusInWindow();
                validate();
                repaint();
            }
        });
	}

	// compute sizes and positions of components, just for readability
	private void computeRectangles() {

		textDispRect = new Rectangle((_FRAME_RECT.width - _TEXT_DISP_W) / 2,
							   	     _UPPER_GAP, 
							   	     _TEXT_DISP_W, 
							   	     _TEXT_DISP_H);

		textEditRect = new Rectangle(textDispRect.x,
							   	     (int)textDispRect.getMaxY() + _V_GAP, 
							   	     _TEXT_EDIT_W, 
							   	     _TEXT_EDIT_H);

        btnSWRect = new Rectangle(textEditRect.x - _BUTTON_SW_W - _H_GAP,
                                  (int)textEditRect.getMaxY() + _V_GAP,
                                  _BUTTON_SW_W,
                                  _BUTTON_SW_H);

        btnOKRect = new Rectangle(btnSWRect.x,
                                  (int)btnSWRect.getMaxY() + _V_GAP,
                                  _BUTTON_OK_W,
                                  _BUTTON_OK_H);

		btnBackSpaceRect = new Rectangle((int)textEditRect.getMaxX() + _H_GAP,
								   	     (int)textEditRect.getMaxY() + _V_GAP,
								   	     _BUTTON_BACKSPACE_W, 
								   	     _BUTTON_BACKSPACE_H);
		
		btnEnterRect = new Rectangle(btnBackSpaceRect.x,
							   		 (int)btnBackSpaceRect.getMaxY() + _V_GAP,
							   		 _BUTTON_ENTER_W, 
							   		 _BUTTON_ENTER_H);

	}
}
