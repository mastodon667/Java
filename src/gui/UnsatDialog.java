package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import observer.DialogObservable;
import data.Course;

@SuppressWarnings("serial")
public class UnsatDialog extends JDialog implements ActionListener {

	private HashMap<String, Course> unsatStructure;
	private ArrayList<JCheckBox> boxes;
	private DialogObservable observable;
	
	public UnsatDialog(JFrame parent, Observer o, HashMap<String, Course> unsatStructure, ArrayList<String> brokenRules) {
		super(parent, "Conflicterende Selectie", true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(500, 400);
		setLocation(parent.getX()+parent.getWidth()/2-250, parent.getY()+parent.getHeight()/2-200);
		
		this.unsatStructure = unsatStructure;
		boxes = new ArrayList<JCheckBox>();
		observable = new DialogObservable();
		observable.addObserver(o);
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		
		JPanel pnlCheckBoxes = new JPanel();
		pnlCheckBoxes.setLayout(new BoxLayout(pnlCheckBoxes, BoxLayout.Y_AXIS));
		
		for (Course course : unsatStructure.values()) {
			JCheckBox cboCourse = new JCheckBox();
			cboCourse.setName(course.getCode());
			cboCourse.setText(course.toString());
			boxes.add(cboCourse);
			pnlCheckBoxes.add(cboCourse);
		}
		
		JPanel pnlRules = new JPanel();
		pnlRules.setLayout(new BoxLayout(pnlRules, BoxLayout.Y_AXIS));
		for (String rule : brokenRules)
			pnlRules.add(new JLabel("<html>" + rule + "</html>"));
		
		pnlCenter.add(pnlCheckBoxes);
		pnlCenter.add(pnlRules);
		add(pnlCenter, BorderLayout.CENTER);
		
		JLabel lblDescription = new JLabel("<html>De huidige selectie kan niet tot een geldig ISP leiden, gelieve sommige van "
				+ "de onderstaande keuzes ongedaan te maken om zo het conflict op te lossen.</html>");
		add(lblDescription, BorderLayout.NORTH);
		
		JButton btnConfirm = new JButton("OK");
		btnConfirm.addActionListener(this);
		add(btnConfirm, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ArrayList<Course> choices = new ArrayList<Course>();
		for (JCheckBox cboCourse : boxes)
			if (cboCourse.isSelected())
				choices.add(unsatStructure.get(cboCourse.getName()).init());
			else
				choices.add(unsatStructure.get(cboCourse.getName()));
		observable.confirm(choices);
		setVisible(false);
		dispose();
	}
}
