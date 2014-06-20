package org.tde.tdescenariodeveloper.ui;

import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Class used to show status/warnings/errors/coordinates
 * @author Shmeel
 *
 */
public class StatusPanel extends JPanel {
	JLabel status;
	public StatusPanel() {
		status=new JLabel();
		add(status);
	}
	public void setStatus(String msg){
		status.setText(msg);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
