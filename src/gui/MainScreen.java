package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import global.Singleton;
import handlers.InferenceHandler;
import handlers.ScheduleHandler;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		ScheduleHandler sHandler = new ScheduleHandler(iHandler.getProgramme().getStages());
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
		Singleton s = Singleton.getInstance();
		File f = new File(s.getIdpPath());
		if (!f.exists() || f.isDirectory() || (!s.getIdpPath().endsWith("idp.bat") && !s.getIdpPath().endsWith("idp"))) {
			int option = JOptionPane.showConfirmDialog(null, "<html>Could not find idp.bat file, please select file location and then restart the application.</html>");
			if (option == JOptionPane.YES_OPTION) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String location = fc.getSelectedFile().getAbsolutePath();
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(s.getConfigPath(), false));
						bw.write(location);
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else {
			new MainScreen();
		}
	}
}
