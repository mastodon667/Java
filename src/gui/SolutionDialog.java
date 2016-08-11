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

import observer.DialogObservable;
import data.Course;

@SuppressWarnings("serial")
public class SolutionDialog extends JDialog implements ActionListener {

	private HashMap<Integer, ArrayList<Course>> solutions;
	private ArrayList<JRadioButton> buttons;
	private DialogObservable observable;
	
	public SolutionDialog(JFrame parent, Observer o, ArrayList<ArrayList<Course>> solutions, ArrayList<String> brokenRules) {
		super(parent, "Oplossingen", true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(500, 400);
		setLocation(parent.getX()+parent.getWidth()/2-250, parent.getY()+parent.getHeight()/2-200);
		this.solutions = new HashMap<Integer, ArrayList<Course>>();
		int i = 1;
		for (ArrayList<Course> solution : solutions) {
			this.solutions.put(i, solution);
			i++;
		}
		buttons = new ArrayList<JRadioButton>();
		observable = new DialogObservable();
		observable.addObserver(o);
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		
		JPanel pnlRadioButtons = new JPanel();
		pnlRadioButtons.setLayout(new BoxLayout(pnlRadioButtons, BoxLayout.Y_AXIS));
		ButtonGroup group = new ButtonGroup();
		int j = 1;
		for (ArrayList<Course> solution : solutions) {
			String s = "";
			for (Course course : solution)
				s += course.toString() + ", ";
			s = (String) s.subSequence(0, s.length()-1);
			JRadioButton rbtnSolution = new JRadioButton(s);
			rbtnSolution.setName(j + "");
			if (j == 1)
				rbtnSolution.setSelected(true);
			j++;
			buttons.add(rbtnSolution);
			pnlRadioButtons.add(rbtnSolution);
			group.add(rbtnSolution);
		}
		
		JPanel pnlRules = new JPanel();
		pnlRules.setLayout(new BoxLayout(pnlRules, BoxLayout.Y_AXIS));
		for (String rule : brokenRules)
			pnlRules.add(new JLabel("<html>" + rule + "</html>"));
		
		pnlCenter.add(pnlRadioButtons);
		pnlCenter.add(pnlRules);
		add(pnlCenter, BorderLayout.CENTER);
		
		JLabel lblDescription = new JLabel("<html>De huidige selectie kan niet tot een geldig ISP leiden, gelieve één van "
				+ "de onderstaande oplossingen te kiezen. </html>");
		add(lblDescription, BorderLayout.NORTH);
		
		JButton btnConfirm = new JButton("OK");
		btnConfirm.addActionListener(this);
		add(btnConfirm, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Course> choices = new ArrayList<Course>();
		for (JRadioButton rbtnSolution : buttons)
			if (rbtnSolution.isSelected()) {
				for (Course course : solutions.get(Integer.parseInt(rbtnSolution.getName())))
					choices.add(course.init());
			}
		observable.confirm(choices);
		setVisible(false);
		dispose();
	}
}
