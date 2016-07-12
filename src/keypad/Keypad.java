package keypad;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Point;

import javax.swing.JLayeredPane;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Keypad extends JFrame {

	/// Class's constants ///
	private static final long serialVersionUID = 1L;
	
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
	private static final int _TEXT_DISP_H = 100;
	
	// main buttons' sizes
	private static final int _BUTTON_W  = 70;
	private static final int _BUTTON_H = 70;
	private static final int _BUTTON_ENTER_W = _BUTTON_W;
	private static final int _BUTTON_ENTER_H = 2 * _BUTTON_H + _V_GAP;
	private static final int _BUTTON_BACKSPACE_W = _BUTTON_W;
	private static final int _BUTTON_BACKSPACE_H = 2 * _BUTTON_H + _V_GAP;
	
	// pop-up buttons' size
	private static final int _BUTTON_OP_W  = 70;
	private static final int _BUTTON_OP_H = 100;
	
	// number of main buttons and pop-up buttons
	private static final int _NB_OF_BUTTONS  = 12;
	private static final int _MAX_NB_OF_OP_BUTTONS = 4;
	private static final int _BUTTONS_PER_ROW = 3;
	
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
	
	
	/// Class's properties ///
	private JPanel contentPane;
	private JLayeredPane layeredPane;
	
	private JScrollPane scrollPane;
	private JTextArea textDisp;
	private JTextField textEdit;
	
	private JButton btnOnPressChosen;
	private JButton btnEnter;
	private JButton btnBackSpace;
	private List<JButton> buttons;
	private List<JButton> buttonsOnPress;

	/**
	 * Create the frame.
	 */
	public Keypad() {
		// setup main frame
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((_SCREEN_WIDTH  - _WIN_WIDTH ) / 2, 
				  (_SCREEN_HEIGHT - _WIN_HEIGHT) / 2, 
				   _WIN_WIDTH, _WIN_HEIGHT);
		
		// initialize button lists
		buttons = new ArrayList<JButton>();
		buttonsOnPress = new ArrayList<JButton>();
		
		// initialize contentPane
		contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setLayout(new CardLayout(0, 0));
		
		// initialize layeredPane
		layeredPane = new JLayeredPane();
		layeredPane.setLayout(null);
		
		// setup buttons' positions and event handlers
		setupButtonsPositions();
		setupButtonsTexts();
		setupTextFields();
		setupButtonsHandlers();
		btnOnPressChosen = null;
		
		// add all to main frame
		contentPane.add(layeredPane);
		setContentPane(contentPane);
	}

	private void setupTextFields() {
		// setup text edit field
		textEdit = new JTextField();
		textEdit.setBounds(_LEFT_GAP, _UPPER_GAP + _TEXT_DISP_H + _V_GAP, _TEXT_EDIT_W, _TEXT_EDIT_H);
		textEdit.setColumns(10);
		layeredPane.add(textEdit);
		layeredPane.moveToBack(textEdit);

		// setup text displayer field
		textDisp = new JTextArea();
		textDisp.setEditable(false);
		textDisp.setBounds(_LEFT_GAP, _UPPER_GAP, _TEXT_DISP_W, _TEXT_DISP_H);
		
		// setup scroll panel for containing text displayer
		scrollPane = new JScrollPane(textDisp);
		scrollPane.setViewportBorder(new CompoundBorder());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(_LEFT_GAP, _UPPER_GAP, _TEXT_DISP_W, _TEXT_DISP_H);
		layeredPane.add(scrollPane);
		layeredPane.moveToBack(scrollPane);
	}
	
	private void setupButtonsPositions() {
		int x = _LEFT_GAP, y = _UPPER_GAP + _TEXT_DISP_H + _TEXT_EDIT_H + 2 * _V_GAP;
		
		// setup number buttons' positions
		for (int i = 0; i < _NB_OF_BUTTONS; i++) {
			buttons.add(new JButton());
			buttons.get(i).setBounds(x, y, _BUTTON_W, _BUTTON_H);
			layeredPane.add(buttons.get(i));
			layeredPane.moveToBack(buttons.get(i));
			if ((i + 1) % _BUTTONS_PER_ROW == 0) {
				x = _LEFT_GAP;
				y += _V_GAP + _BUTTON_H;
			} else x += _H_GAP + _BUTTON_W;
		}
		
		// setup on-press buttons' positions
		for (int i = 0; i < _MAX_NB_OF_OP_BUTTONS; i++) {
			buttonsOnPress.add(new JButton());
			buttonsOnPress.get(i).setSize(_BUTTON_OP_W, _BUTTON_OP_H);
		}
		
		// setup enter button position
		btnEnter = new JButton();
		btnEnter.setBounds(_LEFT_GAP + _BUTTONS_PER_ROW * (_BUTTON_W + _H_GAP),
						   _UPPER_GAP + (_TEXT_DISP_H + _TEXT_EDIT_H + 2 * _V_GAP) + 2 * (_BUTTON_H + _V_GAP),
						   _BUTTON_ENTER_W, 
						   _BUTTON_ENTER_H);
		layeredPane.add(btnEnter);
		layeredPane.moveToBack(btnEnter);
		
		// setup backspace button position
		btnBackSpace = new JButton();
		btnBackSpace.setBounds(_LEFT_GAP + _BUTTONS_PER_ROW * (_BUTTON_W + _H_GAP),
						   	   _UPPER_GAP + (_TEXT_DISP_H + _TEXT_EDIT_H + 2 * _V_GAP),
						   	   _BUTTON_BACKSPACE_W, 
						   	   _BUTTON_BACKSPACE_H);
		layeredPane.add(btnBackSpace);
		layeredPane.moveToBack(btnBackSpace);
	}
	
	private void setupButtonsTexts() {
		for (int i = 0; i < _NB_OF_BUTTONS; i++) {
			String txt = "<html><center>" + buttonsTexts[i].charAt(0) + "<br>" + buttonsTexts[i].substring(1) + "</center></html>";
			buttons.get(i).setText(txt);
		}
		btnEnter.setText("<html><center>ENTER</center></html>");
		btnBackSpace.setText("<html><center>BACK<br>SPACE</center></html>");
	}
	
	private void setupButtonsHandlers() {
		// number buttons
		for (JButton button : buttons) {
			button.addMouseListener(new MouseAdapter() {	
				
				// on-press event handler
				@Override
				public void mousePressed(MouseEvent evt) {
					JButton btn = (JButton)evt.getSource();
					btn.setVisible(false);
					// some mathematical shits, the goal is to centralize all pop-up buttons
					int numberOfButtons = buttonsTexts[buttons.indexOf(btn)].length() - 1;
					Point center = new Point(btn.getLocation().x + (_BUTTON_W - _BUTTON_OP_W) / 2,
											 btn.getLocation().y + (_BUTTON_H - _BUTTON_OP_H) / 2);
					double firstX = center.x - (numberOfButtons - 1) * _BUTTON_OP_W / 2;
					for (int i = 0; i < numberOfButtons; i++) {
						JButton btnTemp = buttonsOnPress.get(i);
						String txt = String.valueOf(buttonsTexts[buttons.indexOf(btn)].charAt(i+1));
						btnTemp.setText(txt);
						// more mathematical shits
						double x = firstX + i * _BUTTON_OP_W;
						double y = center.y;
						btnTemp.setLocation((int)x, (int)y);
						layeredPane.add(btnTemp);
						layeredPane.moveToFront(btnTemp);
					}
					validate(); repaint();
				}
				
				// on-release event handler
				@Override
				public void mouseReleased(MouseEvent evt) {
					JButton btn = (JButton)evt.getSource();
					btn.setVisible(true);
					if (btnOnPressChosen != null) {
						System.out.println("Name = " + btnOnPressChosen.getText());
					} else {
						System.out.println("Nothing chosen");
					}
					for (JButton btnTemp : buttonsOnPress) layeredPane.remove(btnTemp);
					validate(); repaint();
				}
			});
		}
		
		// on-press buttons
		for (JButton button_op : buttonsOnPress) {
			button_op.addMouseListener(new MouseAdapter() {
				
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
		btnEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		// backspace button
		btnBackSpace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Keypad frame = new Keypad();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
