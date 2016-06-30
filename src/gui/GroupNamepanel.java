package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class GroupNamepanel extends JPanel {
	
	public GroupNamepanel(String name) {
		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new LineBorder(Color.black)));
		add(new JLabel(name));
	}

}
