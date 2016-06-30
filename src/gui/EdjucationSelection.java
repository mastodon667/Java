package gui;

import global.Edjucation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class EdjucationSelection {

	private String selection;

	
	public Edjucation selectEdjucation() {
		ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
		selection = "";
		
		JPanel pnlCheckBoxes = new JPanel();
		ButtonGroup bgEdjucation = new ButtonGroup();
		pnlCheckBoxes.setLayout(new BoxLayout(pnlCheckBoxes, BoxLayout.Y_AXIS));
		for (Edjucation edj : Edjucation.values()) {
			JCheckBox cboEdjucation = new JCheckBox(edj.getName());
			bgEdjucation.add(cboEdjucation);
			cboEdjucation.setName(edj.getCode());
			checkboxes.add(cboEdjucation);
			pnlCheckBoxes.add(cboEdjucation);
			if (cboEdjucation.getName().equals(Edjucation.TI.getCode()))
				cboEdjucation.setSelected(true);
		}
		
		JButton btnConfirm = new JButton("OK");
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox cboEdjucation : checkboxes) {
					if (cboEdjucation.isSelected())
						selection = cboEdjucation.getName();
				}
				SwingUtilities.getWindowAncestor(btnConfirm).dispose();
			}
		});
		
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

        content.add(pnlCheckBoxes, BorderLayout.CENTER);
        content.add(btnConfirm, BorderLayout.SOUTH);

        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.setTitle("Selecteer een opleiding");
        dialog.getContentPane().add(content);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        
        
        for (Edjucation edj : Edjucation.values()) {
			if (edj.getCode().equals(selection))
				return edj;
		}
        System.out.println("FAIL");
        return null;
	}
}
