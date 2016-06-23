package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import observer.SolutionDialogObservable;
import data.Course;

public class SolutionDialog extends JDialog implements ActionListener {

	private HashMap<Integer, HashMap<Course, Course>> solutions;
	private ArrayList<JRadioButton> buttons;
	private SolutionDialogObservable observable;
	
	public SolutionDialog(JFrame parent, Observer o, ArrayList<HashMap<Course, Course>> solutions) {
		super(parent, "Oplossingen", true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(500, 400);
		this.solutions = new HashMap<Integer, HashMap<Course,Course>>();
		int i = 0;
		for (HashMap<Course, Course> solution : solutions) {
			this.solutions.put(i, solution);
			i++;
		}
		buttons = new ArrayList<JRadioButton>();
		observable = new SolutionDialogObservable();
		observable.addObserver(o);
		
		JPanel pnlRadioButtons = new JPanel();
		pnlRadioButtons.setLayout(new BoxLayout(pnlRadioButtons, BoxLayout.Y_AXIS));
		ButtonGroup group = new ButtonGroup();
		int j = 0;
		for (HashMap<Course, Course> solution : solutions) {
			String s = "";
			for (Course course : solution.keySet())
				s += course.toString() + ", ";
			s = (String) s.subSequence(0, s.length()-1);
			JRadioButton rbtnSolution = new JRadioButton(s);
			rbtnSolution.setName(j + "");
			j++;
			buttons.add(rbtnSolution);
			pnlRadioButtons.add(rbtnSolution);
			group.add(rbtnSolution);
		}
		add(pnlRadioButtons, BorderLayout.CENTER);
		
		JLabel lblDescription = new JLabel("De huidige selectie kan niet tot een geldig ISP leiden, gelieve ��n van de onderstaande oplossingen te kiezen. \n"
				+ "Deze keuzes zullen ongedaan gemaakt worden.");
		add(lblDescription, BorderLayout.NORTH);
		
		JButton btnConfirm = new JButton("OK");
		btnConfirm.addActionListener(this);
		add(btnConfirm, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO: add functionality
		
	}

}