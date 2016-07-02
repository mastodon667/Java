package gui;

import global.Singleton;
import handlers.InferenceHandler;
import handlers.ScheduleHandler;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MainScreen extends JFrame {

	private JTabbedPane tpPanels;
	private SelectionPanel pnlSelection;
	private CalendarPanel pnlCalendar;

	public MainScreen() {
		super("ISP Selection Application");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1400, 800);

		Singleton s = Singleton.getInstance();
		InferenceHandler iHandler = new InferenceHandler(s);
		ScheduleHandler sHandler = new ScheduleHandler(s, iHandler.getProgramme().getStages());
		iHandler.addObserver(sHandler);
		
		tpPanels = new JTabbedPane();
		pnlCalendar = new CalendarPanel(s, sHandler);
		pnlSelection = new SelectionPanel(this, s, iHandler);
		tpPanels.add(pnlSelection,"Selection");
		tpPanels.add(pnlCalendar,"Schedule");
		add(tpPanels);
		tpPanels.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tpPanels.getSelectedIndex() == 1)
					pnlCalendar.refresh();
			}
		});
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainScreen();
	}
}
