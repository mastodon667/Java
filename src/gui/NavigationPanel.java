package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import observer.NavigationObservable;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel implements ActionListener {

	private HashMap<Integer, Integer> weeks;
	private int selectedWeek;
	private ArrayList<JRadioButton> stages;
	private ArrayList<JToggleButton> days;
	private JLabel lblWeek;
	private NavigationObservable observable; 

	public NavigationPanel(int stages, Observer o) {
		setLayout(new BorderLayout());
		setSize(1400, 200);
		observable = new NavigationObservable();
		observable.addObserver(o);
		JPanel pnlStages = new JPanel();
		pnlStages.add(new JLabel("Stage:"));
		ButtonGroup bgStages = new ButtonGroup();
		this.stages = new ArrayList<JRadioButton>();
		for (int i = 1; i <= stages; i++) {
			JRadioButton rbtnStage = new JRadioButton(i + "");
			rbtnStage.addActionListener(this);
			bgStages.add(rbtnStage);
			rbtnStage.setName(i+"");
			pnlStages.add(rbtnStage);
			this.stages.add(rbtnStage);
			if (i == 1)
				rbtnStage.setSelected(true);
		}
		add(pnlStages, BorderLayout.CENTER);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));

		lblWeek = new JLabel();
		updateWeeks(new TreeSet<Integer>());

		JPanel pnlBottom1 = new JPanel();
		pnlBottom1.setLayout(new BoxLayout(pnlBottom1, BoxLayout.X_AXIS));
		JButton btnPrevious = new JButton("Vorige week");
		btnPrevious.addActionListener(this);
		JButton btnNext = new JButton("Volgende week");
		btnNext.addActionListener(this);

		pnlBottom1.add(btnPrevious);
		pnlBottom1.add(lblWeek);
		pnlBottom1.add(btnNext);
		pnlBottom.add(pnlBottom1);

		JPanel pnlBottom2 = new JPanel();
		pnlBottom2.setLayout(new BoxLayout(pnlBottom2, BoxLayout.X_AXIS));
		ButtonGroup bgDays = new ButtonGroup();
		days = new ArrayList<JToggleButton>();
		JToggleButton tbtnMonday = new JToggleButton("Maandag");
		tbtnMonday.addActionListener(this);
		JToggleButton tbtnTuesday = new JToggleButton("Dinsdag");
		tbtnTuesday.addActionListener(this);
		JToggleButton tbtnWednesday = new JToggleButton("Woensdag");
		tbtnWednesday.addActionListener(this);
		JToggleButton tbtnThursday = new JToggleButton("Donderdag");
		tbtnThursday.addActionListener(this);
		JToggleButton tbtnFriday = new JToggleButton("Vrijdag");
		tbtnFriday.addActionListener(this);
		bgDays.add(tbtnMonday);
		days.add(tbtnMonday);
		bgDays.add(tbtnTuesday);
		days.add(tbtnTuesday);
		bgDays.add(tbtnWednesday);
		days.add(tbtnWednesday);
		bgDays.add(tbtnThursday);
		days.add(tbtnThursday);
		bgDays.add(tbtnFriday);
		days.add(tbtnFriday);
		tbtnMonday.setSelected(true);
		pnlBottom2.add(tbtnMonday);
		pnlBottom2.add(tbtnTuesday);
		pnlBottom2.add(tbtnWednesday);
		pnlBottom2.add(tbtnThursday);
		pnlBottom2.add(tbtnFriday);
		pnlBottom.add(pnlBottom2);

		add(pnlBottom, BorderLayout.SOUTH);
	}

	protected void updateWeeks(TreeSet<Integer> weeks) {
		this.weeks = new HashMap<Integer, Integer>();
		if (!weeks.isEmpty()) {
			Integer week = weeks.higher(26);
			int count = 1;
			while (week != null) {
				this.weeks.put(count, week);
				count++;
				week = weeks.higher(week);
			}
			week = weeks.first();
			while (week < 26) {
				this.weeks.put(count, week);
				count++;
				week = weeks.higher(week);
			}
			selectedWeek = 1;
		}
		else
			selectedWeek = 0;
		refresh();
	}

	protected int getStage() {
		int stage = 1;
		for (JRadioButton rbtnStage : stages)
			if (rbtnStage.isSelected())
				stage = Integer.parseInt(rbtnStage.getName());
		return stage;
	}

	protected String getDay() {
		String day = "Maandag";
		for (JToggleButton tbtnDay : days)
			if (tbtnDay.isSelected())
				day = tbtnDay.getText();
		return day;
	}

	protected int getWeek() {
		if (selectedWeek == 0)
			return selectedWeek;
		return weeks.get(selectedWeek);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			if (((JButton)e.getSource()).getText().equals("Next"))
				nextWeek();
			else
				previousWeek();
		}
		observable.change();
	}

	private void nextWeek() {
		if (selectedWeek > 0) {
			selectedWeek++;
			if (!weeks.keySet().contains(selectedWeek))
				selectedWeek = 1;
			refresh();
		}
	}

	private void previousWeek() {
		if (selectedWeek > 0) {
			selectedWeek--;
			if (!weeks.keySet().contains(selectedWeek))
				selectedWeek = weeks.keySet().size();
			refresh();
		}
	}

	private void refresh() {
		if (selectedWeek > 0)
			lblWeek.setText("Week " + weeks.get(selectedWeek));
		else 
			lblWeek.setText("~");
	}
}
