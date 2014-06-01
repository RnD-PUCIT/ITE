package org.tde.tdescenariodeveloper.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar implements ItemListener,ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4127064504014635395L;

JCheckBox name,id,axis;
JButton open,run,save;
private boolean blocked=true;

DrawingArea drawingArea;
	public ToolBar(DrawingArea drawingArea) {
		this.drawingArea=drawingArea;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.anchor=GridBagConstraints.WEST;
		gbc.insets=new Insets(5, 10, 5, 10);
		gbc.ipadx=2;
		gbc.ipady=2;
		
		open=new JButton("Open",TDEResources.getResources().getOpen());
		save=new JButton("Save",TDEResources.getResources().getSave());
		run=new JButton("Run",TDEResources.getResources().getRun());
		
		name=new JCheckBox("Draw road names");
		id=new JCheckBox("Draw road id's");
		axis=new JCheckBox("Draw axis");
		name.addItemListener(this);
		id.addItemListener(this);
		axis.addItemListener(this);
		
		open.setFocusable(false);
		run.setFocusable(false);
		save.setFocusable(false);
		
		add(open,gbc);
		add(save,gbc);
		add(run,gbc);
		
		add(name,gbc);
		add(id,gbc);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.weightx=1;
		add(axis,gbc);
		id.setSelected(true);
		axis.setSelected(true);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(blocked)return;
		JRadioButton rdsrc=null;
		JCheckBox chsrc=null;
		if(e.getSource() instanceof JRadioButton)rdsrc=(JRadioButton)e.getSource();
		else if(e.getSource() instanceof JCheckBox)chsrc=(JCheckBox)e.getSource();
		if(chsrc==name){
			if(name.isSelected() && id.isSelected())id.setSelected(false);
			drawingArea.setDrawRoadId(id.isSelected());
			drawingArea.setDrawRoadNames(name.isSelected());
		}
		else if(chsrc==id){
			if(id.isSelected() && name.isSelected())name.setSelected(false);
			drawingArea.setDrawRoadId(id.isSelected());
			drawingArea.setDrawRoadNames(name.isSelected());
		}
		else if(chsrc==axis){
			drawingArea.setDrawAxis(axis.isSelected());
		}
		drawingArea.getRoadPrPnl().updateGraphics();
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
