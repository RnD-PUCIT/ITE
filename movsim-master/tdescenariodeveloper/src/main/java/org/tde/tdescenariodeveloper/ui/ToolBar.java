package org.tde.tdescenariodeveloper.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar implements ItemListener{
JCheckBox selectWholeRoad;
DrawingArea drawingArea;
	public ToolBar(DrawingArea drawingArea) {
		this.drawingArea=drawingArea;
		selectWholeRoad=new JCheckBox("Select whole road");
		selectWholeRoad.addItemListener(this);
		add(selectWholeRoad);
		selectWholeRoad.setSelected(true);
	}
	public boolean isWholeRoadSelected(){
		return selectWholeRoad.isSelected();
	}
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		drawingArea.setWholeRoadSelectable(selectWholeRoad.isSelected());
	}
}
