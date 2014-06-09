package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.Lane.Width;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Predecessor;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Successor;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry.Arc;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class ToolsPanel extends JPanel {
	private final ToolsPanel tb;
	private Border defaultBorder;
	private static final long serialVersionUID = -1452084837775482733L;
	private AbstractButton straightRoad,arcRoad,addTrafficSource,junctionEditor,linker;
	private MovsimConfigContext mvCxt;
	private Set<RoadSegment> selectedRoads=Collections.synchronizedSet(new HashSet<RoadSegment>());
	Insets in;
	public ToolsPanel(MovsimConfigContext mvCxt) {
		tb=this;
		this.mvCxt=mvCxt;
		setLayout(new BorderLayout());
		JPanel p=new JPanel();
		add(p,BorderLayout.NORTH);
		p.setLayout(new GridLayout(0,2));
		straightRoad=new JToggleButton(TDEResources.getResources().getStraightRoad());
		arcRoad=new JToggleButton(TDEResources.getResources().getArcRoad());
		addTrafficSource=new JToggleButton(TDEResources.getResources().getTrafficSource());
		junctionEditor=new JToggleButton(TDEResources.getResources().getJunctions());
		linker=new JToggleButton(TDEResources.getResources().getLinker());
		Insets in=new Insets(3, 3, 3, 3);
		straightRoad.setMargin(in);
		arcRoad.setMargin(in);
		addTrafficSource.setMargin(in);
		junctionEditor.setMargin(in);
		linker.setMargin(in);
		straightRoad.setToolTipText("Straight road");
		arcRoad.setToolTipText("Arc raod");
		addTrafficSource.setToolTipText("Traffic Source");
		junctionEditor.setToolTipText("Junctions editor");
		linker.setToolTipText("Road linker");
		
		straightRoad.setFocusable(false);
		arcRoad.setFocusable(false);
		addTrafficSource.setFocusable(false);
		junctionEditor.setFocusable(false);
		linker.setFocusable(false);
		
		addListener(straightRoad);
		addListener(arcRoad);
		addListener(addTrafficSource);
		addListener(junctionEditor);
		addListener(linker);
		p.add(straightRoad);
		p.add(arcRoad);
		p.add(addTrafficSource);
		p.add(junctionEditor);
		p.add(linker);
		
		defaultBorder=arcRoad.getBorder();
	}
	private void addListener(final AbstractButton absBtn) {
		absBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				absBtn.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Point2D p=new Point2D.Double();
				AbstractButton src=null;
				if(e.getSource() instanceof AbstractButton)src=(AbstractButton)e.getSource();
				in=defaultBorder.getBorderInsets(absBtn);
				if(src!=junctionEditor && selectedRoads.size()>0)selectedRoads.clear();
				if(src==straightRoad){
					JToggleButton jtb=(JToggleButton)straightRoad;
					setSelected(linker,arcRoad,addTrafficSource,junctionEditor);
					if(jtb.isSelected()){
						jtb.setBorder(BorderFactory.createLoweredBevelBorder());
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().STRAIGHT_ROAD_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
					else{
						jtb.setBorder(defaultBorder);
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().DEFAULT_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
				}else if(src==arcRoad){
					JToggleButton jtb=(JToggleButton)arcRoad;
					setSelected(linker,junctionEditor,addTrafficSource,straightRoad);
					if(jtb.isSelected()){
						jtb.setBorder(BorderFactory.createLoweredBevelBorder());
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().ARC_ROAD_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
					else{
						jtb.setBorder(defaultBorder);
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().DEFAULT_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
				}else if(src==addTrafficSource){
					JToggleButton jtb=(JToggleButton)addTrafficSource;
					setSelected(linker,arcRoad,junctionEditor,straightRoad);
					if(jtb.isSelected()){
						jtb.setBorder(BorderFactory.createLoweredBevelBorder());
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().TRAFFIC_SOURCE_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
					else{
						jtb.setBorder(defaultBorder);
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().DEFAULT_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
				}
				else if(src==junctionEditor){
					JToggleButton jtb=(JToggleButton)junctionEditor;
					setSelected(linker,arcRoad,addTrafficSource,straightRoad);
					selectedRoads.clear();
					if(jtb.isSelected()){
						jtb.setBorder(BorderFactory.createLoweredBevelBorder());
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().HAND_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
					else{
						jtb.setBorder(defaultBorder);
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().DEFAULT_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
				}else if(src==linker){
					JToggleButton jtb=(JToggleButton)linker;
					setSelected( junctionEditor,straightRoad,arcRoad,addTrafficSource);
					if(jtb.isSelected()){
						jtb.setBorder(BorderFactory.createLoweredBevelBorder());
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().LINK_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
					else{
						jtb.setBorder(defaultBorder);
						mvCxt.getRdCxt().getDrawingArea().setCursor(TDEResources.getResources().DEFAULT_CURSOR);
						mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getDrawingArea().getGraphics());
					}
				}
			}
		});
	}
	public void straightRoadClicked(MouseEvent e){
		Point p=new Point();
		try {
			mvCxt.getRdCxt().getDrawingArea().transform.inverseTransform(new Point(e.getX(),e.getY()), p);
			Road r=RoadNetworkUtils.getRoad(mvCxt.getRdCxt().getRn().getOdrNetwork());
			RoadSegment undr=RoadNetworkUtils.getUnderLyingRoadSegment(p, mvCxt);
			if(undr!=null){
				RoadMapping rm=undr.roadMapping();
				if(!(rm instanceof RoadMappingPoly)){
					PosTheta p1=rm.endPos();
					r.getPlanView().getGeometry().get(0).setX(p1.x);
					r.getPlanView().getGeometry().get(0).setY(p1.y);
					r.getPlanView().getGeometry().get(0).setHdg(p1.theta());
				}
				else{
					RoadMappingPoly rmp=(RoadMappingPoly)rm;
					PosTheta p1=rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1).endPos();
					r.getPlanView().getGeometry().get(0).setX(p1.x);
					r.getPlanView().getGeometry().get(0).setY(p1.y);
					r.getPlanView().getGeometry().get(0).setHdg(p1.theta());
				}
				int laneCountPr=undr.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size();
				int curLaneCount=0;
				double laneWidth=undr.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().get(0).getWidth().get(0).getA();
				Width w=new Width();
				w.setA(laneWidth);
				w.setSOffset(0.0);
				w.setC(0.0);
				w.setD(0.0);
				w.setB(0.0);
				Lane ln=r.getLanes().getLaneSection().get(0).getRight().getLane().get(0);
				ln.getWidth().clear();
				ln.getWidth().add(w);
				org.movsim.network.autogen.opendrive.Lane.Link lin=new org.movsim.network.autogen.opendrive.Lane.Link();
				org.movsim.network.autogen.opendrive.Lane.Link.Predecessor prd=new org.movsim.network.autogen.opendrive.Lane.Link.Predecessor();
				org.movsim.network.autogen.opendrive.Lane.Link.Successor scs=new org.movsim.network.autogen.opendrive.Lane.Link.Successor();
				prd.setId(-1);
				scs.setId(-1);
				lin.setPredecessor(prd);
				lin.setSuccessor(scs);
				ln.setLink(lin);
				while(r.getLanes().getLaneSection().get(0).getRight().getLane().size()!=laneCountPr){
					int laneId=r.getLanes().getLaneSection().get(0).getRight().getLane().get(curLaneCount).getId()-1;
					ln=new Lane();
					lin=new org.movsim.network.autogen.opendrive.Lane.Link();
					prd=new org.movsim.network.autogen.opendrive.Lane.Link.Predecessor();
					scs=new org.movsim.network.autogen.opendrive.Lane.Link.Successor();
					prd.setId(laneId);
					scs.setId(laneId);
					lin.setPredecessor(prd);
					lin.setSuccessor(scs);
					ln.setLink(lin);
					ln.setLevel("false");
					ln.setType("driving");
					ln.setId(laneId);
					ln.getWidth().add(w);
					r.getLanes().getLaneSection().get(0).getRight().getLane().add(ln);
					curLaneCount++;
				}
				Link l=new Link();
				Predecessor pr=new Predecessor();
				pr.setContactPoint("end");
				pr.setElementId(undr.getUserId());
				pr.setElementType("road");
				l.setPredecessor(pr);
				r.setLink(l);
				
				if(undr.getOdrRoad().getLink()==null)undr.getOdrRoad().setLink(new Link());
				Successor sr=new Successor();
				sr.setContactPoint("start");
				sr.setElementId(r.getId());
				sr.setElementType("road");
				undr.getOdrRoad().getLink().setSuccessor(sr);
			}else{
				if(mvCxt.getRdCxt().getAppFrame().getToolbar().getDropRoadAtLast().isSelected() && mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad().size()>0){
					RoadMapping rm=mvCxt.getRdCxt().getRn().getRoadSegments().get(mvCxt.getRdCxt().getRn().getRoadSegments().size()-1).roadMapping();
					PosTheta pt=null;
					if(rm instanceof RoadMappingPoly){
						RoadMappingPoly rmp=(RoadMappingPoly)rm;
						rm=rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1);
					}
					pt=rm.map(rm.roadLength());
					r.getPlanView().getGeometry().get(0).setX(pt.x);
					r.getPlanView().getGeometry().get(0).setY(pt.y);
					r.getPlanView().getGeometry().get(0).setHdg(pt.theta());
				}else{
					r.getPlanView().getGeometry().get(0).setX(p.getX());
					r.getPlanView().getGeometry().get(0).setY(p.getY());
					r.getPlanView().getGeometry().get(0).setHdg(0);
				}
			}
			mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad().add(r);
			RoadNetworkUtils.refresh(mvCxt.getRdCxt());
		} catch (NoninvertibleTransformException e1) {
			GraphicsHelper.showToast(e1.getMessage(), mvCxt.getRdCxt().getToastDurationMilis());
		}
	}
	public void arcRoadClicked(MouseEvent e){
		Point p=new Point();
		try {
			mvCxt.getRdCxt().getDrawingArea().transform.inverseTransform(new Point(e.getX(),e.getY()), p);
			Road r=RoadNetworkUtils.getRoad(mvCxt.getRdCxt().getRn().getOdrNetwork());
			Geometry g=r.getPlanView().getGeometry().get(0);
			g.setLine(null);
			Arc a=new Arc();
			a.setCurvature(0.0045);
			g.setArc(a);
			RoadSegment undr=RoadNetworkUtils.getUnderLyingRoadSegment(p, mvCxt);
			if(undr!=null){
				RoadMapping rm=undr.roadMapping();
				if(!(rm instanceof RoadMappingPoly)){
					PosTheta p1=rm.endPos();
					r.getPlanView().getGeometry().get(0).setX(p1.x);
					r.getPlanView().getGeometry().get(0).setY(p1.y);
					r.getPlanView().getGeometry().get(0).setHdg(p1.theta());
				}
				else{
					RoadMappingPoly rmp=(RoadMappingPoly)rm;
					PosTheta p1=rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1).endPos();
					r.getPlanView().getGeometry().get(0).setX(p1.x);
					r.getPlanView().getGeometry().get(0).setY(p1.y);
					r.getPlanView().getGeometry().get(0).setHdg(p1.theta());
				}
				int laneCountPr=undr.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size();
				int curLaneCount=0;
				double laneWidth=undr.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().get(0).getWidth().get(0).getA();
				Width w=new Width();
				w.setA(laneWidth);
				w.setSOffset(0.0);
				w.setC(0.0);
				w.setD(0.0);
				w.setB(0.0);
				Lane ln=r.getLanes().getLaneSection().get(0).getRight().getLane().get(0);
				ln.getWidth().clear();
				ln.getWidth().add(w);
				org.movsim.network.autogen.opendrive.Lane.Link lin=new org.movsim.network.autogen.opendrive.Lane.Link();
				org.movsim.network.autogen.opendrive.Lane.Link.Predecessor prd=new org.movsim.network.autogen.opendrive.Lane.Link.Predecessor();
				org.movsim.network.autogen.opendrive.Lane.Link.Successor scs=new org.movsim.network.autogen.opendrive.Lane.Link.Successor();
				prd.setId(-1);
				scs.setId(-1);
				lin.setPredecessor(prd);
				lin.setSuccessor(scs);
				ln.setLink(lin);
				while(r.getLanes().getLaneSection().get(0).getRight().getLane().size()!=laneCountPr){
					int laneId=r.getLanes().getLaneSection().get(0).getRight().getLane().get(curLaneCount).getId()-1;
					ln=new Lane();
					lin=new org.movsim.network.autogen.opendrive.Lane.Link();
					prd=new org.movsim.network.autogen.opendrive.Lane.Link.Predecessor();
					scs=new org.movsim.network.autogen.opendrive.Lane.Link.Successor();
					prd.setId(laneId);
					scs.setId(laneId);
					lin.setPredecessor(prd);
					lin.setSuccessor(scs);
					ln.setLink(lin);
					ln.setLevel("false");
					ln.setType("driving");
					ln.setId(laneId);
					ln.getWidth().add(w);
					r.getLanes().getLaneSection().get(0).getRight().getLane().add(ln);
					curLaneCount++;
				}
				Link l=new Link();
				Predecessor pr=new Predecessor();
				pr.setContactPoint("end");
				pr.setElementId(undr.getUserId());
				pr.setElementType("road");
				l.setPredecessor(pr);
				r.setLink(l);
				
				if(undr.getOdrRoad().getLink()==null)undr.getOdrRoad().setLink(new Link());
				Successor sr=new Successor();
				sr.setContactPoint("start");
				sr.setElementId(r.getId());
				sr.setElementType("road");
				undr.getOdrRoad().getLink().setSuccessor(sr);
			}else{
				if(mvCxt.getRdCxt().getAppFrame().getToolbar().getDropRoadAtLast().isSelected() && mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad().size()>0){
					RoadMapping rm=mvCxt.getRdCxt().getRn().getRoadSegments().get(mvCxt.getRdCxt().getRn().getRoadSegments().size()-1).roadMapping();
					PosTheta pt=null;
					if(rm instanceof RoadMappingPoly){
						RoadMappingPoly rmp=(RoadMappingPoly)rm;
						rm=rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1);
					}
					pt=rm.map(rm.roadLength());
					r.getPlanView().getGeometry().get(0).setX(pt.x);
					r.getPlanView().getGeometry().get(0).setY(pt.y);
					r.getPlanView().getGeometry().get(0).setHdg(pt.theta());
				}else{
					r.getPlanView().getGeometry().get(0).setX(p.getX());
					r.getPlanView().getGeometry().get(0).setY(p.getY());
					r.getPlanView().getGeometry().get(0).setHdg(0);
				}
			}
			mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad().add(r);
			RoadNetworkUtils.refresh(mvCxt.getRdCxt());
		} catch (NoninvertibleTransformException e1) {
			GraphicsHelper.showToast(e1.getMessage(), mvCxt.getRdCxt().getToastDurationMilis());
		}
	
	
	}
	public void trafficSourceClicked(MouseEvent e){
		Point p=new Point();

		try {
			mvCxt.getRdCxt().getDrawingArea().transform.inverseTransform(new Point(e.getX(),e.getY()), p);
			RoadSegment undr=RoadNetworkUtils.getUnderLyingRoadSegment(p, mvCxt);
			if(undr!=null){
				if(!Conditions.existsIdInRoadsCustomizations(undr.userId(), mvCxt)){
					org.movsim.autogen.Road rd=new org.movsim.autogen.Road();
					rd.setId(undr.userId());
					if(mvCxt.getMovsim().getScenario().getSimulation().getRoad().add(rd)){
						rd.setTrafficSource(MovsimScenario.getTrafficSource());
						mvCxt.updatePanels();
						mvCxt.getRdCxt().updateGraphics();
					}
					else
						GraphicsHelper.showToast("Road couldn't be added", mvCxt.getRdCxt().getToastDurationMilis());
				}else{
					boolean trfSrcSet=false;
					org.movsim.autogen.Road road=null;
					for(org.movsim.autogen.Road rr:mvCxt.getMovsim().getScenario().getSimulation().getRoad()){
						if(rr.getId().equals(undr.userId())){
							trfSrcSet=rr.isSetTrafficSource();
							road=rr;
						}
					}
					if(!trfSrcSet){
						road.setTrafficSource(MovsimScenario.getTrafficSource());
					}
				}
				
				RoadNetworkUtils.refresh(mvCxt.getRdCxt());
				mvCxt.updatePanels();
			}
		} catch (NoninvertibleTransformException e1) {
			GraphicsHelper.showToast(e1.getMessage(), mvCxt.getRdCxt().getToastDurationMilis());
		}
	
	}
	public Set<RoadSegment> getSelectedRoads() {
		return selectedRoads;
	}
	public void setSelectedRoads(Set<RoadSegment> selectedRoads) {
		this.selectedRoads = selectedRoads;
	}
	public boolean addRoadSelection(RoadSegment s){
		return selectedRoads.add(s);
	}
	public boolean removeRoadSelection(RoadSegment s){
		RoadSegment rs=null;
		for(RoadSegment rs2:selectedRoads){
			if(rs2.userId().equals(s.userId())){
				rs=rs2;
				break;
			}
		}
		return selectedRoads.remove(rs);
	}
	public void clearRoadSelections(){
		selectedRoads.clear();
	}
	public JToggleButton getJunctionEditor() {
		return (JToggleButton)junctionEditor;
	}
	public JToggleButton getLinker() {
		return (JToggleButton)linker;
	}
	public void setSelected(AbstractButton...b){
		for(AbstractButton bt:b){
			JToggleButton btn=(JToggleButton)bt;
			if(bt.isSelected()){
				btn.setSelected(false);
				btn.setBorder(defaultBorder);
			}
		}
	}
	public JToggleButton getStraightRoad() {
		return (JToggleButton)straightRoad;
	}
	public JToggleButton getArcRoad() {
		return (JToggleButton)arcRoad;
	}
	public JToggleButton getAddTrafficSource() {
		return (JToggleButton)addTrafficSource;
	}
}
