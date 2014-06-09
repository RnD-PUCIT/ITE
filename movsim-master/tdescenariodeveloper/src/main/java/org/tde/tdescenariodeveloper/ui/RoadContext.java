package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.eventhandling.Blockable;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaMouseListener;
import org.tde.tdescenariodeveloper.eventhandling.GeometryPanelListener;
import org.tde.tdescenariodeveloper.eventhandling.LanesPanelListener;
import org.tde.tdescenariodeveloper.eventhandling.RoadFieldsPanelListener;

public class RoadContext extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1683090113017092656L;
	private RoadSegment selectedRoad;
	
	private LinkPanel linkPanel;
	private GeometryPanel gmPnl;
	private RoadFieldsPanel rdFldPnl;
	private LanesPanel lanesPnl;
	private GridBagConstraints gbc;
	private DrawingArea drawingArea;
	private AppFrame appFrame;
	private JScrollPane sp;
	private RoadNetwork rn;
	private RoadFieldsPanelListener rfpl;
	private GeometryPanelListener gpl;
	private LanesPanelListener lpl;
	private int toastDurationMilis=4000;
	private MovsimConfigContext mvCxt;
	public RoadContext(RoadNetwork rn,AppFrame appfr) {
		appFrame=appfr;
		this.rn=rn;
		sp=new JScrollPane();
		sp.getViewport().add(this);
		
		linkPanel=new LinkPanel(this);
		
		RoadFieldsPanelListener rfl=new RoadFieldsPanelListener(this);
		rdFldPnl=new RoadFieldsPanel(this, rfl);
		rfpl=rfl;
		
		GeometryPanelListener gpl=new GeometryPanelListener(this);
		gmPnl=new GeometryPanel(this,gpl);
		this.gpl=gpl;
		
		LanesPanelListener lpl=new LanesPanelListener(this);
		lanesPnl=new LanesPanel(this,lpl);
		this.lpl=lpl;
		
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets=new Insets(10, 2, 10, 2);
		add(rdFldPnl,gbc);
		if(selectedRoad!=null && selectedRoad.getOdrRoad().getLink()!=null)
			add(linkPanel, gbc);
		add(gmPnl,gbc);
		add(lanesPnl,gbc);
	}
	public void blockListeners(boolean b){
		gpl.setBlocked(b);
		rfpl.setBlocked(b);
		lpl.setBlocked(b);
		drawingArea.getPopup().setBlockListener(b);
		appFrame.getToolbar().setBlocked(b);
		appFrame.getJl().setBlocked(b);
		linkPanel.getListener().setBlocked(b);
	}
	public void updateGraphics(){
		drawingArea.revalidate();
		drawingArea.paint(drawingArea.getGraphics());
	}
	public void updatePanel(){
		blockListeners(true);
		rdFldPnl.updateFields();
		updateLinkPanel();
		gmPnl.updateGeomPanel();
		lanesPnl.updatelanesPanel();
		setVisible(true);
		blockListeners(false);
	}
	private void updateLinkPanel() {
		boolean linkAdded = isAdded(linkPanel);
		if (selectedRoad.getOdrRoad().getLink() != null) {
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
		setSelectedRoad(selectedRoad, true);
	}
	public void setSelectedRoad(RoadSegment selectedRoad,boolean update) {
		if(selectedRoad==null)return;
		this.selectedRoad = selectedRoad;
		if(update)updatePanel();
	}
	public void setSelectedRoadNull() {
		DrawingAreaMouseListener dl=(DrawingAreaMouseListener)drawingArea.getMouseListeners()[0];
		dl.setSelected(false);
		blockListeners(true);
		this.selectedRoad = null;
		linkPanel.reset();
		gmPnl.reset();
		rdFldPnl.reset();
		lanesPnl.reset();
		blockListeners(false);
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

	public RoadNetwork getRn() {
		return rn;
	}

	public void setRn(RoadNetwork rn) {
		this.rn = rn;
	}

	public AppFrame getAppFrame() {
		return appFrame;
	}
	public int getToastDurationMilis() {
		return toastDurationMilis;
	}
	public void setToastDurationMilis(int toastDurationMilis) {
		this.toastDurationMilis = toastDurationMilis;
	}
	public MovsimConfigContext getMvCxt() {
		return mvCxt;
	}
	public void setMvCxt(MovsimConfigContext mvCxt) {
		this.mvCxt = mvCxt;
	}
	
}
