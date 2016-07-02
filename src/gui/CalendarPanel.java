package gui;

import global.Singleton;
import handlers.ScheduleHandler;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class CalendarPanel extends JPanel implements CalendarPanelInterface, Observer, MouseMotionListener {
	
	private ScheduleHandler handler;
	private NavigationPanel pnlNavigation;
	private DayPanel pnlDay;
	private int currentStage;
	private int selectedWeek;
	private String selectedDay;

	public CalendarPanel(Singleton s, ScheduleHandler handler) {
		setLayout(new BorderLayout());
		this.handler = handler;
		JSplitPane spnlSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		spnlSplit.setDividerLocation(0.5);
		
		pnlNavigation = new NavigationPanel(handler.getStages(), this);
		currentStage = pnlNavigation.getStage();
		selectedWeek = pnlNavigation.getWeek();
		selectedDay = pnlNavigation.getDay();
		spnlSplit.add(pnlNavigation);
		pnlDay = new DayPanel();
		pnlDay.addMouseMotionListener(this);
		spnlSplit.add(pnlDay);
		
		add(spnlSplit, BorderLayout.CENTER);
	}

	@Override
	public void refresh() {
		//TODO: REFRESH WHEN FOCUS
		pnlNavigation.updateWeeks(handler.getWeeks(currentStage));
		selectedWeek = pnlNavigation.getWeek();
		pnlDay.update(handler.getLessons(currentStage, selectedDay, selectedWeek));
	}

	@Override
	public void update(Observable o, Object arg) {
		if (pnlNavigation.getStage() != currentStage) {
			currentStage = pnlNavigation.getStage();
			pnlNavigation.updateWeeks(handler.getWeeks(currentStage));
		}
		if (pnlNavigation.getWeek() != selectedWeek) {
			selectedWeek = pnlNavigation.getWeek();
			pnlDay.update(handler.getLessons(currentStage, selectedDay, selectedWeek));
		}
		if (!pnlNavigation.getDay().equals(selectedDay)) {
			selectedDay = pnlNavigation.getDay();
			pnlDay.update(handler.getLessons(currentStage, selectedDay, selectedWeek));
		}
	}
	
	protected Observer getHandler() {
		return handler;
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (LessonPanel pnlLesson : pnlDay.getPanels()) {
			if (pnlLesson.isIn(e.getX(), e.getY())) {
				pnlDay.setToolTipText(pnlLesson.toString());
				break;
			}
		}
	}
}
