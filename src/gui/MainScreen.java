package gui;

import global.Singleton;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

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
		
		tpPanels = new JTabbedPane();
		pnlCalendar = new CalendarPanel(s, 1); //TODO: CHANGE
		pnlSelection = new SelectionPanel(this, s, pnlCalendar.getHandler());
		tpPanels.add(pnlSelection,"Selection");
		tpPanels.add(pnlCalendar,"Schedule");
		add(tpPanels);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainScreen();
	}
}
