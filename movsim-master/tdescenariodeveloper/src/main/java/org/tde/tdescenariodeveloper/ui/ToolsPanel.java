package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1452084837775482733L;
	//JButton straightRoad,arcRoad,
	public ToolsPanel() {
		setLayout(new BorderLayout());
		JPanel p=new JPanel();
		add(p,BorderLayout.NORTH);
		p.setLayout(new GridLayout(0,2));
		for(int i=0;i<30;i++){
			JButton b=new JButton("Tool "+(i+1));
			b.setMargin(new Insets(12, 2, 12, 2));
			p.add(b);
		}
	}
}
