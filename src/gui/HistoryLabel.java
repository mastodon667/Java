package gui;

import javax.swing.JLabel;

import data.Action;

@SuppressWarnings("serial")
public class HistoryLabel extends JLabel {

	private Action action;
	
	public HistoryLabel(Action action) {
		setText(action.toString());
		this.action = action;
	}
	
	protected Action getAction() {
		return action;
	}
}
