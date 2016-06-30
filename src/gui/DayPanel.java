package gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import data.Lesson;

@SuppressWarnings("serial")
public class DayPanel extends JPanel {
	
	private ArrayList<Lesson> lessons;
	private ArrayList<LessonPanel> panels;
	
	public DayPanel() {
		lessons = new ArrayList<Lesson>();
		panels = new ArrayList<LessonPanel>();
	}
	
	protected void update(ArrayList<Lesson> lessons) {
		this.lessons = new ArrayList<Lesson>(lessons);
		repaint();
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 1; i < 14; i++) {
        	g.drawString(i+7 + ":00", ((getWidth()/14)*i)-10, 20);
        	g.drawLine((getWidth()/14)*i, 30, (getWidth()/14)*i, getHeight());
        }
        
        panels = new ArrayList<LessonPanel>();
        for (Lesson lesson : lessons) {
        	LessonPanel pnlLesson = new LessonPanel(calculateX(lesson.getStartOfClass()), 50, calculateWidth(lesson.getDuration()), 120, lesson.toString());
        	while (collision(pnlLesson)) {
        		pnlLesson.increaseY(130);
        	}
        	panels.add(pnlLesson);
        	g.fillRect(pnlLesson.getX(), pnlLesson.getY(), pnlLesson.getWidth(), pnlLesson.getHeight());
        }
    }
	
	private boolean collision(LessonPanel pnlLesson) {
		for (LessonPanel pnlLesson1 : panels)
			if (pnlLesson1.collision(pnlLesson))
				return true;
		return false;
	}
	
	private int calculateX(GregorianCalendar start) {
		GregorianCalendar d = new GregorianCalendar(start.get(GregorianCalendar.YEAR), 
				start.get(GregorianCalendar.MONTH),
				start.get(GregorianCalendar.DAY_OF_MONTH), 7, 00);
		long diff = ((start.getTimeInMillis() - d.getTimeInMillis()) / (60*1000));
		double sol = getWidth();
		sol /= 14;
		double den = diff;
		den /= 60;
		sol *= den;
		return (int) sol;
	}
	
	private int	calculateWidth(int duration) {
		double sol = getWidth();
		sol /= 14;
		double den = duration;
		den /= 60;
		sol *= den;
		return (int) sol;
	}

	protected ArrayList<LessonPanel> getPanels() {
		return panels;
	}
}
