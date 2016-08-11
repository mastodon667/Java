package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
public class PropagationDialog extends JDialog implements ActionListener {

	private ArrayList<Course> before;
	private ArrayList<Course> after;
	private ArrayList<JCheckBox> checkboxes;
	private DialogObservable observable;
	
	public PropagationDialog(JFrame parent, Observer o, ArrayList<Course> before, ArrayList<Course> after) {
		super(parent, "Propagaties", true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(500, 400);
		setLocation(parent.getX()+parent.getWidth()/2-250, parent.getY()+parent.getHeight()/2-200);
		this.before = before;
		this.after = after;
		checkboxes = new ArrayList<JCheckBox>();
		observable = new DialogObservable();
		observable.addObserver(o);
		
		JPanel pnlCheckBoxes = new JPanel();
		pnlCheckBoxes.setLayout(new BoxLayout(pnlCheckBoxes, BoxLayout.Y_AXIS));
		for (Course course : before) {
			JCheckBox cboCourse = new JCheckBox(course.toString());
			cboCourse.setName(course.getCode());
			checkboxes.add(cboCourse);
			pnlCheckBoxes.add(cboCourse);
		}
		add(pnlCheckBoxes, BorderLayout.CENTER);
		
		JLabel lblDescription = new JLabel("<html>De nieuwe keuze heeft ervoor gezorgd dat onderstaande propagaties niet meer van kracht zijn."
				+ " Gelieve te selecteren welke van deze propagaties je wenst te behouden.</html>");
		add(lblDescription, BorderLayout.NORTH);
		
		JButton btnConfirm = new JButton("OK");
		btnConfirm.addActionListener(this);
		add(btnConfirm, BorderLayout.SOUTH);
	}
	
	private Course getCourse(String code, ArrayList<Course> courses) {
		for (Course course : courses)
			if (course.getCode().equals(code))
				return course;
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Course> result = new ArrayList<Course>();
		for (JCheckBox cboCourse : checkboxes) {
			if (cboCourse.isSelected())
				result.add(getCourse(cboCourse.getName(), before));
			else
				result.add(getCourse(cboCourse.getName(), after));
		}
		observable.confirm(result);
		setVisible(false);
		dispose();
	}
}
