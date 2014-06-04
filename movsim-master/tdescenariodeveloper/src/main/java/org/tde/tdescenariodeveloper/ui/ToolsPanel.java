package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
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
	final ToolsPanel tb;
	Border defaultBorder;
	private static final long serialVersionUID = -1452084837775482733L;
	JButton straightRoad,arcRoad,addTrafficSource;
	MovsimConfigContext mvCxt;
	public ToolsPanel(MovsimConfigContext mvCxt) {
		tb=this;
		this.mvCxt=mvCxt;
		setLayout(new BorderLayout());
		JPanel p=new JPanel();
		add(p,BorderLayout.NORTH);
		p.setLayout(new GridLayout(0,2));
		straightRoad=new JButton(TDEResources.getResources().getStraightRoad());
		arcRoad=new JButton(TDEResources.getResources().getArcRoad());
		addTrafficSource=new JButton(TDEResources.getResources().getTrafficSource());
		Insets in=new Insets(3, 3, 3, 3);
		straightRoad.setMargin(in);
		arcRoad.setMargin(in);
		addTrafficSource.setMargin(in);
		straightRoad.setToolTipText("Straight road");
		arcRoad.setToolTipText("Arc raod");
		addTrafficSource.setToolTipText("Traffic Source");
		addListener(straightRoad);
		addListener(arcRoad);
		addListener(addTrafficSource);
		p.add(straightRoad);
		p.add(arcRoad);
		p.add(addTrafficSource);
		defaultBorder=arcRoad.getBorder();
	}
	private void addListener(final JButton btn) {
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				btn.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Point2D p=new Point2D.Double();
				JButton src=null;
				btn.setBorder(defaultBorder);
				if(e.getSource() instanceof JButton)src=(JButton)e.getSource();
				Insets in=defaultBorder.getBorderInsets(btn);
				if(src==straightRoad){
					try {
						mvCxt.getRdCxt().getDrawingArea().transform.inverseTransform(new Point(e.getX()-tb.getWidth(),e.getY()), p);
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
								ln.getWidth().add(w);
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
							r.getPlanView().getGeometry().get(0).setX(p.getX());
							r.getPlanView().getGeometry().get(0).setY(p.getY());
							r.getPlanView().getGeometry().get(0).setHdg(0);
						}
						mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad().add(r);
						RoadNetworkUtils.refresh(mvCxt.getRdCxt());
					} catch (NoninvertibleTransformException e1) {
						GraphicsHelper.showToast(e1.getMessage(), mvCxt.getRdCxt().getToastDurationMilis());
					}
				}else if(src==arcRoad){

					try {
						mvCxt.getRdCxt().getDrawingArea().transform.inverseTransform(new Point(e.getX()-tb.getWidth()+btn.getWidth()+in.left+in.right,e.getY()), p);
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
							r.getPlanView().getGeometry().get(0).setX(p.getX());
							r.getPlanView().getGeometry().get(0).setY(p.getY());
							r.getPlanView().getGeometry().get(0).setHdg(0);
						}
						mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad().add(r);
						RoadNetworkUtils.refresh(mvCxt.getRdCxt());
					} catch (NoninvertibleTransformException e1) {
						GraphicsHelper.showToast(e1.getMessage(), mvCxt.getRdCxt().getToastDurationMilis());
					}
				
				}else if(src==addTrafficSource){

					try {
						mvCxt.getRdCxt().getDrawingArea().transform.inverseTransform(new Point(e.getX()-tb.getWidth(),e.getY()+btn.getHeight()), p);
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
			}
		});
	}
}
