package org.tde.tdescenariodeveloper.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar implements ItemListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4127064504014635395L;

JCheckBox name,id,axis;

DrawingArea drawingArea;
	public ToolBar(DrawingArea drawingArea) {
		this.drawingArea=drawingArea;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.weightx=1;
		gbc.insets=new Insets(5, 5, 5, 5);
		name=new JCheckBox("name");
		id=new JCheckBox("id");
		axis=new JCheckBox("draw axis");
		name.addItemListener(this);
		id.addItemListener(this);
		axis.addItemListener(this);
		add(name,gbc);
		add(id,gbc);
		add(axis,gbc);
		id.setSelected(true);
		axis.setSelected(true);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
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
}
