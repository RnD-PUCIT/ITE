package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.movsim.network.autogen.opendrive.OpenDRIVE;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaMouseListener;
import org.tde.tdescenariodeveloper.eventhandling.GeometryPanelListener;
import org.tde.tdescenariodeveloper.eventhandling.LanesPanelListener;
import org.tde.tdescenariodeveloper.eventhandling.RoadFieldsPanelListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * This class holds input of .xodr file and also panels of selected road with junctions information
 * @author Shmeel
 * @see LinkPanel
 * @see GeometryPanel
 * @see RoadFieldsPanel
 * @see SignalsPanel
 * @see RoadSegment
 * @see DrawingArea
 * @see AppFrame
 * @see RoadNetwork
 * @see OpenDRIVE
 */
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
	private SignalsPanel signalsPanel;
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
	/**
	 * 
	 * @param rn {@link RoadNetwork}
	 * @param appfr {@link AppFrame}
	 */
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
		signalsPanel=new SignalsPanel(this);
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
		add(signalsPanel,gbc);
	}
	/**
	 * blocks all listeners
	 * @param b if true listeners are blocked, false otherwise
	 */
	public void blockListeners(boolean b){
		gpl.setBlocked(b);
		rfpl.setBlocked(b);
		lpl.setBlocked(b);
		drawingArea.getPopup().setBlockListener(b);
		appFrame.getToolbar().setBlocked(b);
		appFrame.getJl().setBlocked(b);
		linkPanel.getListener().setBlocked(b);
		signalsPanel.getListener().setBlocked(b);
	}
	public void updateGraphics(){
		drawingArea.paint(drawingArea.getGraphics());
	}
	/**
	 * updates the information of all the panels contained in {@link RoadContext} from memory
	 */
	public void updatePanel(){
		if(selectedRoad==null)return;
		blockListeners(true);
		rdFldPnl.updateFields();
		updateLinkPanel();
		gmPnl.updateGeomPanel();
		lanesPnl.updatelanesPanel();
		signalsPanel.updateSignalPanel();
		updateGraphics();
		revalidate();
		repaint();
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
			}
			linkPanel.updateLinkPanel();
		} else if (linkAdded) {
			remove(linkPanel);
		}
	}
/**
 * used to check if component exists in this {@link RoadContext}
 * @param c Components to be checked
 * @return returns true if this components is added false otherwise 
 */
	public boolean isAdded(Component c){
		if(c.getParent()==this)return true;
		return false;
	}
	/**
	 * sets selected road
	 * @param selectedRoad {@link RoadSegment} to be set as selected
	 */
	public void setSelectedRoad(RoadSegment selectedRoad) {
		setSelectedRoad(selectedRoad, true);
	}
	/**
	 * sets selected {@link Road} and updates all the panels contained in this {@link RoadContext}
	 * @param selectedRoad {@link RoadSegment} to be set as selected
	 * @param update if true {@link Road} is set and panels are updated and if false road is set but panels are not updated
	 */
	public void setSelectedRoad(RoadSegment selectedRoad,boolean update) {
		if(selectedRoad==null)return;
		this.selectedRoad = selectedRoad;
		if(update)updatePanel();
	}
	/**
	 * Sets road as null and resets all fields
	 */
	public void setSelectedRoadNull() {
		DrawingAreaMouseListener dl=(DrawingAreaMouseListener)drawingArea.getMouseListeners()[0];
		dl.setSelected(false);
		blockListeners(true);
		this.selectedRoad = null;
		linkPanel.reset();
		gmPnl.reset();
		rdFldPnl.reset();
		lanesPnl.reset();
		signalsPanel.reset();
		blockListeners(false);
		revalidate();
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
	public SignalsPanel getSignalsPanel() {
		return signalsPanel;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
