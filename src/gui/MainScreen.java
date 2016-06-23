package gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainScreen extends JFrame {

	private JTabbedPane tpPanels;
	private SelectionPanel pnlSelection;
	private CalendarPanel pnlCalendar;
	
	public MainScreen(String path) {
		super("ISP Selection Application");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1400, 800);
	
		tpPanels = new JTabbedPane();
		pnlSelection = new SelectionPanel(this, path);
		pnlCalendar = new CalendarPanel(2);
		tpPanels.add(pnlSelection,"Selection");
		tpPanels.add(pnlCalendar,"Schedule");
		add(tpPanels);	
		setVisible(true);
	}
	
	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser("src/files");
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			MainScreen m = new MainScreen(path);
		}
	}
}
