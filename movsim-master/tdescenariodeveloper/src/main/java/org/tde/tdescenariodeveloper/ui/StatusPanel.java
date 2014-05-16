package org.tde.tdescenariodeveloper.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {
	JLabel status;
	public StatusPanel() {
		status=new JLabel();
		add(status);
	}
	public void setStatus(String msg){
		status.setText(msg);
	}
}
