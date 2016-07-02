package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DistributionDialog extends JDialog {
	
	public DistributionDialog(JFrame parent, HashMap<String, Integer> min, HashMap<String, Integer> max, HashMap<String, Integer> distribution) {
		super(parent, "ECTS Distributie", false);
		setLayout(new BorderLayout());
		setSize(500, 400);
		setLocation(parent.getX()+parent.getWidth()/2-250, parent.getY()+parent.getHeight()/2-200);
		JPanel pnlTable = new JPanel();
		pnlTable.setLayout(new GridLayout(min.size()+1, 4, 5, 5));
		pnlTable.add(new JLabel());
		pnlTable.add(new JLabel("Minimum"));
		pnlTable.add(new JLabel("Distributie"));
		pnlTable.add(new JLabel("Max"));
		for (String group : min.keySet()) {
			pnlTable.add(new JLabel(group));
			pnlTable.add(new JLabel(min.get(group)+ ""));
			pnlTable.add(new JLabel(distribution.get(group)+ ""));
			pnlTable.add(new JLabel(max.get(group)+ ""));
		}
		add(pnlTable, BorderLayout.CENTER);
	}
}
