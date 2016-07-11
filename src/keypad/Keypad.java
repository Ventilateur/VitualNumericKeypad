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


public class Keypad extends JFrame {

	/// Class's constants ///
	private static final long serialVersionUID = 1L;
	
	// screen's size
	private static final int _SCREEN_WIDTH  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int _SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	// window's size
	private static final int _WIN_WIDTH  = 600;
	private static final int _WIN_HEIGHT = 600;
	
	// main buttons' size
	private static final int _BUTTON_WIDTH  = 70;
	private static final int _BUTTON_HEIGHT = 70;
	
	// pop-up buttons' size
	private static final int _BUTTON_OP_WIDTH  = 100;
	private static final int _BUTTON_OP_HEIGHT = 80;
	
	// number of main buttons and pop-up buttons
	private static final int _NB_OF_BUTTONS  = 12;
	private static final int _MAX_NB_OF_OP_BUTTONS = 4;
	private static final int _BUTTONS_PER_ROW = 3;
	
	// gap between 2 main buttons
	private static final int _H_GAP = 5;
	private static final int _V_GAP = 5;
	
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
	private static final String[] texts = {b1Disp, b2Disp, b3Disp, b4Disp, 
										   b5Disp, b6Disp, b7Disp, b8Disp, 
										   b9Disp, bAsteriskDisp, b0Disp, bSharpDisp};
	
	/// Class's constants ///
	
	private JPanel contentPane;
	private JLayeredPane layeredPane;
	
	
	private JButton btnOnPressChosen;
	private List<JButton> buttons;
	private List<JButton> buttonsOnPress;

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
		setUpAllButtonsPositions();
		setUpAllButtonsTexts();
		setUpAllButtonsHandlers();
		btnOnPressChosen = null;
		
		// add all to main frame
		contentPane.add(layeredPane);
		setContentPane(contentPane);
	}
	
	private void setUpAllButtonsPositions() {
		int x = _H_GAP + 2 * _BUTTON_WIDTH, y = _V_GAP + _BUTTON_HEIGHT;
		for (int i = 0; i < _NB_OF_BUTTONS; i++) {
			buttons.add(new JButton());
			buttons.get(i).setBounds(x, y, _BUTTON_WIDTH, _BUTTON_HEIGHT);
			layeredPane.add(buttons.get(i));
			layeredPane.moveToBack(buttons.get(i));
			if ((i + 1) % _BUTTONS_PER_ROW == 0) {
				x = _H_GAP + 2 * _BUTTON_WIDTH;
				y += _V_GAP + _BUTTON_HEIGHT;
			} else x += _H_GAP + _BUTTON_WIDTH;
		}
		for (int i = 0; i < _MAX_NB_OF_OP_BUTTONS; i++) {
			buttonsOnPress.add(new JButton());
			buttonsOnPress.get(i).setSize(_BUTTON_OP_WIDTH, _BUTTON_OP_HEIGHT);
		}
	}
	
	private void setUpAllButtonsTexts() {
		for (int i = 0; i < _NB_OF_BUTTONS; i++) {
			String txt = "<html><center>" + texts[i].charAt(0) + "<br>" + texts[i].substring(1) + "</center></html>";
			buttons.get(i).setText(txt);
		}
	}
	
	private void setUpAllButtonsHandlers() {
		for (JButton button : buttons) {
			button.addMouseListener(new MouseAdapter() {	
				
				// on-press event handler
				@Override
				public void mousePressed(MouseEvent evt) {
					JButton btn = (JButton)evt.getSource();
					btn.setVisible(false);
					// some mathematical shits, the goal is to centralize all pop-up buttons
					int numberOfButtons = texts[buttons.indexOf(btn)].length() - 1;
					Point center = new Point(btn.getLocation().x + (_BUTTON_WIDTH - _BUTTON_OP_WIDTH) / 2,
											 btn.getLocation().y + (_BUTTON_HEIGHT - _BUTTON_OP_HEIGHT) / 2);
					double firstX = center.x - (numberOfButtons - 1) * _BUTTON_OP_WIDTH / 2;
					for (int i = 0; i < numberOfButtons; i++) {
						JButton btnTemp = buttonsOnPress.get(i);
						String txt = String.valueOf(texts[buttons.indexOf(btn)].charAt(i+1));
						btnTemp.setText(txt);
						// more mathematical shits
						double x = firstX + i * _BUTTON_OP_WIDTH;
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
	}
}
