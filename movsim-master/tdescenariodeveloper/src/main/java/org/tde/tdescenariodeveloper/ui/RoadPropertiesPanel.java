package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

public class RoadPropertiesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1683090113017092656L;
	private RoadSegment selectedRoad;

	private LinkPanel linkPanel;
	private GeometryPanel gmPnl;
	private RoadFieldsPanel rdFldPnl;
	GridBagConstraints gbc;
	private DrawingArea drawingArea;
	public RoadPropertiesPanel(RoadNetwork rn) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		gmPnl=new GeometryPanel(rn);
		linkPanel=new LinkPanel(rn);
		rdFldPnl=new RoadFieldsPanel(rn);
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets=new Insets(5, 5, 5, 5);
		add(rdFldPnl,gbc);
		if(selectedRoad!=null && selectedRoad.getOdrRoad().getLink()!=null)
			add(linkPanel, gbc);
		add(gmPnl,gbc);
		setVisible(false);
	}
	
	public void updateGraphics(){
		drawingArea.paint(drawingArea.getGraphics());
	}
	public void updatePanel(){
		setVisible(true);
		rdFldPnl.updateFields(selectedRoad);
		updateLinkPanel();
		gmPnl.updateGeomPanel(selectedRoad);
	}
	private void updateLinkPanel() {
		boolean linkAdded = isAdded(linkPanel);
		if (selectedRoad.getOdrRoad().getLink() != null) {
			linkPanel.setSelectedRoad(selectedRoad);
			if (!linkAdded) {
				gbc = new GridBagConstraints();
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.anchor = GridBagConstraints.NORTHWEST;
				gbc.fill = GridBagConstraints.BOTH;
				gbc.insets = new Insets(5, 5, 5, 5);
				add(linkPanel, gbc);
				revalidate();
			}
			linkPanel.updateLinkPanel();
		} else if (linkAdded) {
			remove(linkPanel);
			revalidate();
		}
	}

	boolean isAdded(Component c){
		if(c.getParent()==this)return true;
		return false;
	}
	
	public void setSelectedRoad(RoadSegment selectedRoad) {
		if(selectedRoad==null)return;
		this.selectedRoad = selectedRoad;
		updatePanel();
	}
	public void setSelectedRoadNull() {
		this.selectedRoad = null;
	}
	

	public RoadSegment getSelectedRoad() {
		return selectedRoad;
	}


	public GeometryPanel getGmPnl() {
		return gmPnl;
	}


	public DrawingArea getDrawingArea() {
		return drawingArea;
	}


	public void setDrawingArea(DrawingArea drawingArea) {
		this.drawingArea = drawingArea;
	}
	
}
