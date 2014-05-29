package org.tde.tdescenariodeveloper.utils;

import java.util.Date;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.Lane.Width;
import org.movsim.network.autogen.opendrive.OpenDRIVE;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Header;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes.LaneSection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes.LaneSection.Right;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry.Line;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.ui.RoadContext;

public class RoadNetworkUtils {
	public static OpenDRIVE getNewOdr(){
		OpenDRIVE odr=new OpenDRIVE();
		odr.setHeader(getHeader());
		odr.getRoad().add(getRoad(odr));
		return odr;
	}
	private static Road getRoad(OpenDRIVE odr) {
		Road r=new Road();
		if(odr.getRoad()!=null){
			r.setId((odr.getRoad().size()+1)+"");
			r.setName("R"+odr.getRoad().size()+1);
		}
		else{
			r.setId(1+"");
			r.setName("R1");
		}
		r.setLength(100);
		r.setJunction("-1");
		r.setPlanView(getPlainView());
		r.setLanes(getLanes());
		return r;
	}
	private static Lanes getLanes() {
		Lanes l=new Lanes();
		l.getLaneSection().add(getLaneSection());
		return l;
	}
	private static LaneSection getLaneSection() {
		LaneSection s=new LaneSection();
		s.setRight(getRight());
		s.setS(0.0);
		return s;
	}
	private static Right getRight() {
		Right r=new Right();
		r.getLane().add(getLane());
		return r;
	}
	private static Lane getLane() {
		Lane ln=new Lane();
		ln.setId(-1);
		ln.setLevel("false");
		ln.setType("driving");
		ln.getWidth().add(getLaneWidth());
		return ln;
	}
	private static Width getLaneWidth() {
		Width w=new Width();
		w.setA(10.0);
		w.setB(0.0);
		w.setC(0.0);
		w.setD(0.0);
		w.setSOffset(0.0);
		return w;
	}
	private static PlanView getPlainView() {
		PlanView pv=new PlanView();
		pv.getGeometry().add(getGeometry());
		return pv;
	}
	private static Geometry getGeometry() {
		Geometry g=new Geometry();
		g.setLength(100);
		g.setLine(new Line());
		g.setHdg(0.0);
		g.setS(0.0);
		g.setX(500);
		g.setY(100);
		return g;
	}
	private static Header getHeader() {
		Header h=new Header();
		h.setDate(new Date().toString());
		h.setRevMajor(1);
		h.setRevMinor(2);
		h.setName("");
		h.setVersion(1.00);
		h.setNorth(0.0);
		h.setSouth(0.0);
		h.setEast(0.0);
		h.setWest(0.0);
		return h;
	}
	public static void refresh(RoadContext rdCxt){
		SwingUtilities.invokeLater(new Refresher(rdCxt));
	}
	public static boolean areValidLaneIds(RoadContext rdCxt){
		if(rdCxt.getLanesPnl().getOdrLanes().get(0).getId()!=-1)return false;
		for(int i=1;i<rdCxt.getLanesPnl().getOdrLanes().size();i++){
			if(rdCxt.getLanesPnl().getOdrLanes().get(i).getId()>rdCxt.getLanesPnl().getOdrLanes().get(i-1).getId()){
				return false;
			}
		}
		return true;
	}
	public static void updateLaneIds(RoadContext rdCxt){
		if(!areValidLaneIds(rdCxt)){
			for(int i=0;i<rdCxt.getLanesPnl().getOdrLanes().size();i++){
				rdCxt.getLanesPnl().getOdrLanes().get(i).setId(-(i+1));
			}
		}
	}
	public static void updateCoordinatesAndHeadings(RoadContext rdCxt) {
		if(rdCxt.getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
			RoadMappingPoly rmp=(RoadMappingPoly)rdCxt.getSelectedRoad().roadMapping();
			for(int i=1;i<rmp.getRoadMappings().size();i++){
				RoadMapping rm=OpenDriveHandlerJaxb.createRoadMapping(rdCxt.getSelectedRoad().getOdrRoad());
				if(rm instanceof RoadMappingPoly)rmp=(RoadMappingPoly)rm;
				else continue;
				Geometry gm=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(i);
				PosTheta preEnd=rmp.getRoadMappings().get(i-1).map(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(i-1).getLength());
				gm.setHdg(preEnd.theta());
				gm.setX(preEnd.x);
				gm.setY(preEnd.y);
			}
		}
	}
}
class Refresher extends SwingWorker<Object, String>{
	RoadContext rdCxt;
	public Refresher(RoadContext rp) {
		this.rdCxt=rp;
	}

	@Override
	protected Object doInBackground() throws Exception {
		int gmInd=rdCxt.getGmPnl().getSelectedIndex();
		int lnInd=rdCxt.getLanesPnl().getSelectedIndex();
		int ind=rdCxt.getRn().getOdrNetwork().getRoad().indexOf(rdCxt.getSelectedRoad().getOdrRoad());
		if(ind<0)ind=0;
		if(gmInd<0)gmInd=0;
		if(lnInd<0)lnInd=0;
		rdCxt.getRn().reset();
		new OpenDriveHandlerJaxb().create("", rdCxt.getRn().getOdrNetwork(), rdCxt.getRn());
		if(rdCxt.getRn().getRoadSegments().size()-1<ind)ind=rdCxt.getRn().getRoadSegments().size()-1;
		RoadSegment rs=rdCxt.getRn().getRoadSegments().get(ind);
		if(rs.getOdrRoad().getPlanView().getGeometry().size()-1<gmInd)gmInd=rs.getOdrRoad().getPlanView().getGeometry().size()-1;
		if(rs.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size()-1<lnInd)lnInd=rs.getOdrRoad().getPlanView().getGeometry().size()-1;
		rdCxt.getGmPnl().setSelectedGeometry(gmInd,false);
		rdCxt.getLanesPnl().setSelectedLane(lnInd, false);
		rdCxt.setSelectedRoad(rs);
		return null;
	}
	
}
