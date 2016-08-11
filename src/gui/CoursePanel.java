package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import observer.CourseObservable;
import data.Course;

@SuppressWarnings("serial")
public class CoursePanel extends JPanel implements ActionListener {
	
	private boolean callback;
	private Course course;
	private CourseObservable observable;
	private JComboBox<String> cboStages;
	private JToggleButton tbtnNotInterested;
	private HashMap<String, Integer> map = new HashMap<String, Integer>();

	public CoursePanel(Course course, Observer o) {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 20, 10, 20), new LineBorder(Color.black)));
		
		this.course = course;
		observable = new CourseObservable();
		observable.addObserver(o);
		map.put("Not Selected", Course.notSelected);
		for (int stage : course.getStages())
			map.put("Fase " + stage, stage);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridwidth = 2;
		add(new JLabel(course.getName() + " (" + course.getEcts() + ")"), gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		add(new JLabel(course.getCode()));
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		tbtnNotInterested = new JToggleButton("Geen Interesse");
		tbtnNotInterested.addActionListener(this);
		add(tbtnNotInterested, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		cboStages = new JComboBox<String>(map.keySet().toArray(new String[map.size()]));
		cboStages.setSelectedItem("Not Selected");
		cboStages.addActionListener(this);
		add(cboStages, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.ipadx = 30;
		gbc.ipady = 30;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(new SemesterPanel(course.getSemester()), gbc);
		callback = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (callback) {
			if (e.getSource() == cboStages)
				course.setSelected(map.get(cboStages.getSelectedItem()));
			else if (e.getSource() == tbtnNotInterested)
				course.setNotInterested(tbtnNotInterested.isSelected());
			observable.change(course.clone());
		}
	}
	
	protected void refresh() {
		callback = false;
		tbtnNotInterested.setSelected(course.getNotInterested());
		for (String key : map.keySet())
			if (map.get(key) == course.getSelected())
				cboStages.setSelectedItem(key);
		cboStages.setEnabled(!tbtnNotInterested.isSelected());
		callback = true;
	}
}
