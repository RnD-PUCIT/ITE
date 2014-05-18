package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.eventhandling.RoadFieldsListener;

public class RoadPropertiesPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1683090113017092656L;
	private RoadSegment selectedRoad;
	private JToggleButton pin;

	private LinkPanel linkPanel;
	private GeometryPanel gmPnl;
	private RoadFieldsPanel rdFldPnl;
	private LanesPanel lanesPnl;
	GridBagConstraints gbc;
	private DrawingArea drawingArea;
	JScrollPane sp;
	public RoadPropertiesPanel(RoadNetwork rn) {
		sp=new JScrollPane();
		sp.getViewport().add(this);
		sp.setPreferredSize(new Dimension(260,700));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		pin=new JToggleButton("Pin");
		pin.addActionListener(this);
		gmPnl=new GeometryPanel(rn);
		lanesPnl=new LanesPanel(rn);
		linkPanel=new LinkPanel(rn);
		
		RoadFieldsListener rfl=new RoadFieldsListener(this);
		rdFldPnl=new RoadFieldsPanel(rn, rfl);
		rfl.setRdFldsPnl();
		
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(pin,gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets=new Insets(5, 5, 5, 5);
		add(rdFldPnl,gbc);
		if(selectedRoad!=null && selectedRoad.getOdrRoad().getLink()!=null)
			add(linkPanel, gbc);
		add(gmPnl,gbc);
		add(lanesPnl,gbc);
		sp.setVisible(false);
	}
	
	public void updateGraphics(){
		drawingArea.revalidate();
		drawingArea.paint(drawingArea.getGraphics());
	}
	public void updatePanel(){
		setVisible(true);
		rdFldPnl.updateFields(selectedRoad);
		updateLinkPanel();
		gmPnl.updateGeomPanel(selectedRoad);
		lanesPnl.updateLanesPanel(selectedRoad);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		((JToggleButton)e.getSource()).setText(pin.isSelected()?"Pinned":"Pin");
	}
	@Override
	public void setVisible(boolean b){
		sp.setVisible(b || pin.isSelected());
		((JPanel)drawingArea.getParent()).revalidate();
		//super.setVisible(b || pin.isSelected());
	}

	public JScrollPane getSp() {
		return sp;
	}

	public RoadFieldsPanel getRdFldPnl() {
		return rdFldPnl;
	}

	public LinkPanel getLinkPanel() {
		return linkPanel;
	}

	public LanesPanel getLanesPnl() {
		return lanesPnl;
	}

	public void setRdFldPnl(RoadFieldsPanel rdFldPnl) {
		this.rdFldPnl = rdFldPnl;
	}
	
}
