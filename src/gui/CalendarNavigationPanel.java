package gui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

public class CalendarNavigationPanel extends JPanel {

	public CalendarNavigationPanel(int stages) {
		setLayout(new BorderLayout());
		setSize(1400, 200);
		JPanel pnlStages = new JPanel();
		pnlStages.add(new JLabel("Stage:"));
		ButtonGroup bgStages = new ButtonGroup();
		for (int i = 1; i <= stages; i++) {
			JRadioButton rbtnStage = new JRadioButton(i + "");
			bgStages.add(rbtnStage);
			pnlStages.add(rbtnStage);
			if (i == 1)
				rbtnStage.setSelected(true);
		}
		add(pnlStages, BorderLayout.CENTER);
		
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		
		JPanel pnlBottom1 = new JPanel();
		pnlBottom1.setLayout(new BoxLayout(pnlBottom1, BoxLayout.X_AXIS));
		JButton btnPrevious = new JButton("Vorige week");
		JButton btnNext = new JButton("Volgende week");
		JLabel lblWeek = new JLabel();
		pnlBottom1.add(btnPrevious);
		pnlBottom1.add(lblWeek);
		pnlBottom1.add(btnNext);
		pnlBottom.add(pnlBottom1);
		
		JPanel pnlBottom2 = new JPanel();
		pnlBottom2.setLayout(new BoxLayout(pnlBottom2, BoxLayout.X_AXIS));
		ButtonGroup bgDays = new ButtonGroup();
		JToggleButton tbtnMonday = new JToggleButton("Maandag");
		JToggleButton tbtnTuesday = new JToggleButton("Dinsdag");
		JToggleButton tbtnWednesday = new JToggleButton("Woensdag");
		JToggleButton tbtnThursday = new JToggleButton("Donderdag");
		JToggleButton tbtnFriday = new JToggleButton("Vrijdag");
		bgDays.add(tbtnMonday);
		bgDays.add(tbtnTuesday);
		bgDays.add(tbtnWednesday);
		bgDays.add(tbtnThursday);
		bgDays.add(tbtnFriday);
		tbtnMonday.setSelected(true);
		pnlBottom2.add(tbtnMonday);
		pnlBottom2.add(tbtnTuesday);
		pnlBottom2.add(tbtnWednesday);
		pnlBottom2.add(tbtnThursday);
		pnlBottom2.add(tbtnFriday);
		pnlBottom.add(pnlBottom2);
		
		add(pnlBottom, BorderLayout.SOUTH);
	}
}
