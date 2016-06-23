package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

import javax.swing.JPanel;

public class SemesterPanel extends JPanel {

	private int semester;
	
	public SemesterPanel(int semester) {
		setSize(30, 30);
		this.semester = semester;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.black);
		int start = 90;
		int extent = 180;
		if (semester == 2)
			start = 270;
		else if (semester == 3)
			extent = 360;
		Arc2D.Double circle = new Arc2D.Double(getWidth()/2, getHeight()/2, 20, 20, start, extent, Arc2D.PIE);
		g2d.fill(circle);
	}
}
