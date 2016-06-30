package gui;

import global.Singleton;
import handlers.InferenceHandler;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.Action;
import data.Course;

@SuppressWarnings("serial")
public class SelectionPanel extends JPanel implements ActionListener, Observer, SelectionPanelInterface {

	private GroupPanel pnlProgramme;
	private HistoryPanel pnlHistory;
	private InferenceHandler handler;
	private JComboBox<String> cboParameter;
	private JFrame parent;
	
	public SelectionPanel(JFrame parent, Singleton s, Observer o) {
		setLayout(new BorderLayout());
		this.parent = parent;
		handler = new InferenceHandler(s,(SelectionPanelInterface)this);
		handler.addObserver(o);
		pnlProgramme = new GroupPanel(handler.getProgramme(), this);
		JScrollPane spProgramme = new JScrollPane(pnlProgramme, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pnlHistory = new HistoryPanel();
		add(spProgramme, BorderLayout.CENTER);
		add(pnlHistory, BorderLayout.EAST);
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
		JButton btnExpand = new JButton("Genereer ISP");
		JButton btnMinimize = new JButton("Optimaal ISP");
		cboParameter = new JComboBox<String>(handler.getTerms().toArray(new String[handler.getTerms().size()]));
		JButton btnDistribution = new JButton("ECTS Distributie");
		JButton btnUndo = new JButton("Maak Ongedaan");
		btnExpand.addActionListener(this);
		btnMinimize.addActionListener(this);
		cboParameter.addActionListener(this);
		btnDistribution.addActionListener(this);
		btnUndo.addActionListener(this);
		pnlButtons.add(btnExpand);
		pnlButtons.add(btnMinimize);
		pnlButtons.add(cboParameter);
		pnlButtons.add(btnDistribution);
		pnlButtons.add(btnUndo);
		add(pnlButtons, BorderLayout.SOUTH);
		update(null, new ArrayList<Course>());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			String text = ((JButton)e.getSource()).getText();
			if (text.equals("Genereer ISP"))
				handler.expand();
			else if (text.equals("Optimaal ISP"))
				handler.minimize(cboParameter.getSelectedItem().toString());
			else if (text.equals("ECTS Distributie"))
				System.out.println("SHOW DISTRIBUTION");
			else if (text.equals("Maak Ongedaan")) {
				Action a = pnlHistory.undoAction();
				if (a != null)
					a.undoAction(handler);
			}
				
		}
		refresh();
	}
	
	private void refresh() {
		pnlProgramme.refresh();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		handler.update((ArrayList<Course>)arg);
		refresh();
	}

	@Override
	public void showSolutionPopup(ArrayList<HashMap<Course, Course>> solutions) {
		SolutionDialog dlgSolution = new SolutionDialog(parent, this, solutions);		
		dlgSolution.setVisible(true);
	}

	@Override
	public void showPropagationPopup(ArrayList<Course> before, ArrayList<Course> after) {
		PropagationDialog dlgPropagation = new PropagationDialog(parent, this, before, after);
		dlgPropagation.setVisible(true);
	}

	@Override
	public void addAction(Action action) {
		pnlHistory.addAction(action);
	}
	
	protected Observable getHandler() {
		return handler;
	}
}
