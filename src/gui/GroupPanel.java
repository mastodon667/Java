package gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import observer.GroupObserver;
import data.Course;
import data.Group;

@SuppressWarnings("serial")
public class GroupPanel extends JPanel {

	private GroupObserver observer;
	private ArrayList<CoursePanel> coursePanels;
	private ArrayList<GroupPanel> groupPanels;
	
	public GroupPanel(Group group, Observer o) {
		coursePanels = new ArrayList<CoursePanel>();
		groupPanels = new ArrayList<GroupPanel>();
		observer = new GroupObserver();
		observer.addObserver(o);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.black), new EmptyBorder(0, 10, 0, 0)));
		add(new GroupNamepanel(group.getName()));
		
		ArrayList<Course> mandatoryCourses = group.getMandatoryCourses();
		if (!mandatoryCourses.isEmpty()) {
			add(new JLabel("Verplicht"));
			for (Course course : mandatoryCourses) {
				CoursePanel pnlCourse = new CoursePanel(course, observer);
				coursePanels.add(pnlCourse);
				add(pnlCourse);
			}
		}
		
		ArrayList<Course> optionalCourses = group.getOptionalCourses();
		if (!optionalCourses.isEmpty()) {
			add(new JLabel("Keuze"));
			for (Course course : optionalCourses) {
				CoursePanel pnlCourse = new CoursePanel(course, observer);
				coursePanels.add(pnlCourse);
				add(pnlCourse);
			}
		}
		
		for (Group g : group.getGroups()) {
			GroupPanel pnlGroup = new GroupPanel(g, observer);
			groupPanels.add(pnlGroup);
			add(pnlGroup);
		}
	}
	
	protected void refresh() {
		for (CoursePanel pnlCourse : coursePanels)
			pnlCourse.refresh();
		for (GroupPanel pnlGroup : groupPanels)
			pnlGroup.refresh();
	}
}
