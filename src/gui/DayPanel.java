package gui;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.Lesson;

public class DayPanel extends JPanel {
	
	ArrayList<Lesson> lessons;
	
	public DayPanel() {
		
	}
	
	protected void update(ArrayList<Lesson> lessons) {
		lessons = new ArrayList<Lesson>(lessons);
		repaint();
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < 15; i++) {
        	g.drawString(i+8 + ":00", (getWidth()/15)*i+(getWidth()/30)-10, 20);
        	g.drawLine((getWidth()/15)*i+(getWidth()/30), 30, (getWidth()/15)*i+(getWidth()/30), getHeight());
        }
        
        //g.drawString("BLAH", 20, 20);
        //g.drawRect(200, 200, 200, 200);
    }
}
