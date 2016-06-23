package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class CalendarPanel extends JPanel {

	public CalendarPanel(int stages) {
		setLayout(new BorderLayout());
		JSplitPane spnlSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		spnlSplit.setDividerLocation(0.5);
		
		spnlSplit.add(new CalendarNavigationPanel(stages));
		spnlSplit.add(new DayPanel());
		
		add(spnlSplit, BorderLayout.CENTER);
	}
}
