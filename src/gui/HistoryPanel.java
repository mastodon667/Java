package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.Action;

@SuppressWarnings("serial")
public class HistoryPanel extends JPanel {
	
	private JPanel pnlActions;
	private ArrayList<HistoryLabel> actions;
	
	public HistoryPanel() {
		setLayout(new BorderLayout());
		setSize(200, 800);
		setPreferredSize(new Dimension(200, 800));
		add(new JLabel("Geschiedenis:"), BorderLayout.NORTH);
		pnlActions = new JPanel();
		pnlActions.setLayout(new BoxLayout(pnlActions, BoxLayout.Y_AXIS));
		add(pnlActions, BorderLayout.CENTER);
		actions = new ArrayList<HistoryLabel>();
	}
	
	
	public void addAction(Action action) {
		if (!action.toString().isEmpty())
			actions.add(new HistoryLabel(action));
			updateHistory();
	}
	
	public Action undoAction() {
		if (actions.isEmpty())
			return null;
		else {
			Action a = actions.get(actions.size()-1).getAction();
			actions.remove(actions.size()-1);
			updateHistory();
			return a;
		}
	}
	
	private void updateHistory() {
		pnlActions.removeAll();
		for (HistoryLabel lblHistory : actions) 
			pnlActions.add(lblHistory);
		pnlActions.revalidate();
		pnlActions.repaint();
	}
	
}
