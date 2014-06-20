package org.tde.tdescenariodeveloper.utils;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;

import javax.sound.midi.Sequence;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.movsim.autogen.Movsim;
import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.Lane.Width;
import org.movsim.network.autogen.opendrive.OpenDRIVE;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Header;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes.LaneSection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes.LaneSection.Right;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry.Line;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.LaneSegment;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.movsim.simulator.trafficlights.TrafficLightLocation;
import org.movsim.simulator.trafficlights.TrafficLights;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.ui.RoadContext;
/**
 * This class helps to achieve small {@link OpenDRIVE} and {@link RoadNetwork} related tasks
 * @author Shmeel
 * @see Movsim
 * @see RoadNetwork
 * @see OpenDRIVE
 */
public class RoadNetworkUtils {
	public static OpenDRIVE getNewOdr(){
		OpenDRIVE odr=new OpenDRIVE();
		odr.setHeader(getHeader());
		odr.getRoad().add(getRoad(odr));
		return odr;
	}
	/**
	 * used to get id of the first signal
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 * @return id of first signal if found otherwise null
	 */
	public static String getFirstSignal(MovsimConfigContext mvCxt){
		for(Road r:mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad()){
			if(r.isSetSignals()){
				return r.getSignals().getSignal().get(0).getId();
			}
		}
		return null;
	}
	/**
	 * used to get point on {@link Lane} of {@link RoadSegment} which is 5 units away from start of the road. It is used to show links
	 * @param rs {@link RoadSegment}
	 * @param lane {@link Lane}
	 * @return {@link Point2D}
	 */
	public static Point2D getStart(RoadSegment rs,int lane){
//		LaneSegment ls=rs.getLaneSegments()[lane-1];
		Point2D.Double p=new Point2D.Double();
		PosTheta pt;
		RoadMapping first=null;
		double rw=0;
		if(!(rs.roadMapping() instanceof RoadMappingPoly)){
			first=rs.roadMapping();
			rw=first.roadWidth()/2.0;
		}
		else{
			RoadMappingPoly rmp=(RoadMappingPoly)rs.roadMapping();
			first=rmp.getRoadMappings().get(0);
			rw=rmp.roadWidth()/2.0;
		}
		if(first.roadLength()/2>=5){
			pt=first.map(5, (lane*rs.roadMapping().laneWidth())-(first.laneWidth()/2.0));
		}else {
			pt=first.map(2,(lane*rs.roadMapping().laneWidth())-(first.laneWidth()/2.0));
		}
		p.setLocation(pt.x-rw*pt.sinTheta, pt.y-rw*pt.cosTheta);
//		if(!ls.getBounds().contains(p))throw new IllegalArgumentException("Get start not found");
		return p;
	}
	public static void syncTrafficPanels(MovsimConfigContext mvCxt){
		mvCxt.getTrafficLights().getControllerPanel().updateControllerPanel();
		mvCxt.getTrafficLights().updateTrafficLightsPanel();;
	}
	/**
	 * used to get point on {@link Lane} of {@link RoadSegment} which is 5 units away from end of the road. It is used to show links
	 * @param rs {@link RoadSegment}
	 * @param lane {@link Lane}
	 * @return {@link Point2D}
	 */
	public static Point2D getEnd(RoadSegment rs,int lane){
		Point2D.Double p=new Point2D.Double();
		PosTheta pt;
		RoadMapping end=null;
		double rw=0;
		if(!(rs.roadMapping() instanceof RoadMappingPoly)){
			end=rs.roadMapping();
			rw=end.roadWidth()/2.0;
		}
		else{
			RoadMappingPoly rmp=(RoadMappingPoly)rs.roadMapping();
			end=rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1);
			rw=rmp.roadWidth()/2.0;
		}
		if(end.roadLength()/2>=5){
			pt=end.map(end.roadLength()-5, (lane*rs.roadMapping().laneWidth())-(end.laneWidth()/2.0));
		}else {
			pt=end.map(end.roadLength()-2,(lane*rs.roadMapping().laneWidth())-(end.laneWidth()/2.0));
		}
		p.setLocation(pt.x-rw*pt.sinTheta, pt.y-rw*pt.cosTheta);
		return p;
	}
	/**
	 * used to get point on road in mid width and at 5 units away from start
	 * @param rs {@link RoadSegment}
	 * @return {@link Point2D}
	 */
	public static Point2D getStartMidRoad(RoadSegment rs){
		Point2D.Double p=new Point2D.Double();
		PosTheta pt;
		RoadMapping first=null;
		if(!(rs.roadMapping() instanceof RoadMappingPoly))first=rs.roadMapping();
		else{
			RoadMappingPoly rmp=(RoadMappingPoly)rs.roadMapping();
			first=rmp.getRoadMappings().get(0);
		}
		if(first.roadLength()/2>=5){
			pt=first.map(5,0);
		}else {
			pt=first.map(2,0);
		}
		p.setLocation(pt.x,pt.y);
		return p;
	}
	/**
	 * used to get point on road in mid width and at 5 units away from start
	 * @param rs {@link RoadSegment}
	 * @return {@link Point2D}
	 */
	public static Point2D getEndMidRoad(RoadSegment rs){
		Point2D.Double p=new Point2D.Double();
		PosTheta pt;
		RoadMapping end=null;
		if(!(rs.roadMapping() instanceof RoadMappingPoly))end=rs.roadMapping();
		else{
			RoadMappingPoly rmp=(RoadMappingPoly)rs.roadMapping();
			end=rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1);
		}
		if(end.roadLength()/2>=5){
			pt=end.map(end.roadLength()-5,0);
		}else {
			pt=end.map(end.roadLength()-2,0);
		}
		p.setLocation(pt.x, pt.y);
		return p;
	}
	public static Road getRoad(OpenDRIVE odr) {
		Road r=new Road();
		if(odr.getRoad().size()>0){
			//TODO adjust ids
			int id=Integer.parseInt(odr.getRoad().get(odr.getRoad().size()-1).getId())+1;
			r.setId(id+"");
			r.setName("R"+id);
		}
		else{
			r.setId(1+"");
			r.setName("R1");
		}
		r.setLength(700);
		r.setJunction("-1");
		r.setPlanView(getPlainView(odr));
		r.setLanes(getLanes(odr));
		return r;
	}
	public static Lanes getLanes(OpenDRIVE odr) {
		Lanes l=new Lanes();
		l.getLaneSection().add(getLaneSection(odr));
		return l;
	}
	public static LaneSection getLaneSection(OpenDRIVE odr) {
		LaneSection s=new LaneSection();
		s.setRight(getRight(odr));
		s.setS(0.0);
		return s;
	}
	public static Right getRight(OpenDRIVE odr) {
		Right r=new Right();
		r.getLane().add(getLane(odr));
		return r;
	}
	public static Lane getLane(OpenDRIVE odr) {
		Lane ln=new Lane();
		ln.setId(-1);
		ln.setLevel("false");
		ln.setType("driving");
		ln.getWidth().add(getLaneWidth(odr));
		return ln;
	}
	public static Width getLaneWidth(OpenDRIVE odr) {
		Width w=new Width();
		w.setA(10);
		w.setB(0.0);
		w.setC(0.0);
		w.setD(0.0);
		w.setSOffset(0.0);
		return w;
	}
	public static PlanView getPlainView(OpenDRIVE odr) {
		PlanView pv=new PlanView();
		pv.getGeometry().add(getGeometry());
		return pv;
	}
	public static Geometry getGeometry() {
		Geometry g=new Geometry();
		g.setLength(700);
		g.setLine(new Line());
		g.setHdg(0.0);
		g.setS(0.0);
		g.setX(200);
		g.setY(100);
		return g;
	}
	public static Header getHeader() {
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
//	public static void refresh(RoadContext rdCxt){
//		SwingUtilities.invokeLater(new Refresher(rdCxt));
//	}
	/**
	 * checks if {@link Lane} ids are in sequence
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 * @return true if ids are in valid sequence or false otherwise
	 */
	public static boolean areValidLaneIds(RoadContext rdCxt){
		if(rdCxt.getLanesPnl().getOdrLanes().get(0).getId()!=-1)return false;
		for(int i=1;i<rdCxt.getLanesPnl().getOdrLanes().size();i++){
			if(rdCxt.getLanesPnl().getOdrLanes().get(i).getId()>rdCxt.getLanesPnl().getOdrLanes().get(i-1).getId()){
				return false;
			}
		}
		return true;
	}
	
	public static Road getUnderLyingRoad(Point p,MovsimConfigContext mvCxt){
		Road r=null;
		for(int i=mvCxt.getRdCxt().getRn().getRoadSegments().size()-1;i>=0;i++){
			RoadMapping rm=mvCxt.getRdCxt().getRn().getRoadSegments().get(i).roadMapping();
			if(rm instanceof RoadMappingPoly){
				RoadMappingPoly rmp=(RoadMappingPoly)rm;
				if(rmp.getBounds().contains(p.getX(),p.getY())){
					r=mvCxt.getRdCxt().getRn().getRoadSegments().get(i).getOdrRoad();
					return r; 
				}
			}
			else{
				if(rm.getBounds().contains(p.getX(),p.getY())){
					r=mvCxt.getRdCxt().getRn().getRoadSegments().get(i).getOdrRoad();
					return r;
				}
			}
		}
		return r;
	}
	/**
	 * makes lane ids in required sequence
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 */
	public static void updateLaneIds(RoadContext rdCxt){
		if(!areValidLaneIds(rdCxt)){
			for(int i=0;i<rdCxt.getLanesPnl().getOdrLanes().size();i++){
				rdCxt.getLanesPnl().getOdrLanes().get(i).setId(-(i+1));
			}
		}
	}
	/**
	 * updates headings and coordinates of selected road relative to first geometry
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 */
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
	/**
	 * removes signals which are not controlled by any {@link Controller}
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
	public static void removeUncontrolledSignals(MovsimConfigContext mvCxt){
		for(Road r:mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad()){
			if(r.isSetSignals()){
				ArrayList<Signal>sgnls=new ArrayList<OpenDRIVE.Road.Signals.Signal>();
				for(Signal s:r.getSignals().getSignal()){
					if(!isRefferedInController(s.getId(),mvCxt))
						sgnls.add(s);
				}
				r.getSignals().getSignal().removeAll(sgnls);
				if(r.getSignals().getSignal().size()<1){
					r.setSignals(null);
				}
			}
		}
	}
	/**
	 * checks if signal is referred in any controller
	 * @param id id of the {@link Signal}
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 * @return true if there is any {@link Controller} of this signal
	 */
	private static boolean isRefferedInController(String id,
			MovsimConfigContext mvCxt) {
		if(!mvCxt.getRdCxt().getRn().getOdrNetwork().isSetController())return false;
		for(Controller cc:mvCxt.getRdCxt().getRn().getOdrNetwork().getController()){
			for(Control c:cc.getControl()){
				if(c.getSignalId().equals(id))return true;
			}
		}
		return false;
	}
	/**
	 * used to get first {@link Controller}
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 * @return first {@link Controller} if not found null is returned
	 */
	public static Controller getFirstController(RoadContext rdCxt){
		if(!rdCxt.getRn().getOdrNetwork().isSetController())return null;
		for(Controller cc:rdCxt.getRn().getOdrNetwork().getController()){
			return cc;
		}
		return null;
	}
	/**
	 * used to get underlying {@link RoadSegment} under provided {@link Point2D}
	 * @param p {@link Point2D}
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 * @return {@link RoadSegment} if found other wise null
	 */
	public static RoadSegment getUnderLyingRoadSegment(Point2D p,
			MovsimConfigContext mvCxt) {
		RoadSegment r=null;
		for(int i=mvCxt.getRdCxt().getRn().getRoadSegments().size()-1;i>=0;i--){
			RoadMapping rm=mvCxt.getRdCxt().getRn().getRoadSegments().get(i).roadMapping();
			if(rm instanceof RoadMappingPoly){
				RoadMappingPoly rmp=(RoadMappingPoly)rm;
				if(rmp.getBounds().contains(p)){
					r=mvCxt.getRdCxt().getRn().getRoadSegments().get(i);
					return r; 
				}
			}
			else{
				if(rm.getBounds().contains(p)){
					r=mvCxt.getRdCxt().getRn().getRoadSegments().get(i);
					return r;
				}
			}
		}
		return r;
	}
	/**
	 * used to get underlying {@link LaneSegment} under provided {@link Point2D}
	 * @param p {@link Point2D}
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 * @return {@link LaneSegment} if found other wise null
	 */
	public static LaneSegment getUnderLyingLaneSegment(Point2D p,
			MovsimConfigContext mvCxt) {
		LaneSegment r=null;
		for(int i=mvCxt.getRdCxt().getRn().getRoadSegments().size()-1;i>=0;i--){
			RoadMapping rm=mvCxt.getRdCxt().getRn().getRoadSegments().get(i).roadMapping();
			if(rm instanceof RoadMappingPoly){
				RoadMappingPoly rmp=(RoadMappingPoly)rm;
				if(rmp.getBounds().contains(p)){
					return getUnderLyingLaneSegment(p, mvCxt.getRdCxt().getRn().getRoadSegments().get(i));
				}
			}
			else{
				if(rm.getBounds().contains(p)){
					return getUnderLyingLaneSegment(p, mvCxt.getRdCxt().getRn().getRoadSegments().get(i));
				}
			}
		}
		return r;
	}
	/**
	 * used to get underlying {@link LaneSegment} under provided {@link Point2D} in given {@link RoadSegment}
	 * @param p {@link Point2D}
	 * @param rr {@link RoadSegment} to be searched
	 * @return {@link LaneSegment} if found other wise null
	 */
	public static LaneSegment getUnderLyingLaneSegment(Point2D p,RoadSegment rr){
		for(LaneSegment ls:rr.getLaneSegments()){
			if(ls.getBounds().contains(p))return ls;
		}
		return null;
	}
	/**
	 * initializes {@link TrafficLightLocation}s and {@link TrafficLights}
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
	public static void SetupLights(MovsimConfigContext mvCxt){
		if(!mvCxt.getMovsim().getScenario().isSetTrafficLights() || !mvCxt.getRdCxt().getRn().getOdrNetwork().isSetController())return;
		mvCxt.getMovsim().getScenario().getTrafficLights().setLogging(false);
		new TrafficLights(mvCxt.getMovsim().getScenario().getTrafficLights(), mvCxt.getRdCxt().getRn());
	}
	/**
	 * used to get bounds of the {@link Road} underlying given {@link Point2D}
	 * @param p {@link Point2D}
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 * @return {@link Shape} bounds
	 */
	public static Shape getUnderLyingRoadShape(Point2D p,
			MovsimConfigContext mvCxt) {
		Shape r=null;
		for(int i=mvCxt.getRdCxt().getRn().getRoadSegments().size()-1;i>=0;i--){
			RoadMapping rm=mvCxt.getRdCxt().getRn().getRoadSegments().get(i).roadMapping();
			if(rm instanceof RoadMappingPoly){
				RoadMappingPoly rmp=(RoadMappingPoly)rm;
				if(rmp.getBounds().contains(p)){
					r=rmp.getBounds();
					return r; 
				}
			}
			else{
				if(rm.getBounds().contains(p)){
					r=rm.getBounds();
					return r;
				}
			}
		}
		return r;
	}
	/**
	 * reloads all .xodr data from memory and rebuilds {@link RoadMapping}s and set up lights again and reconstructs whole {@link RoadNetwork} 
	 * @param rdCxt contains reference to loaded .xprj and other added panels in it
	 */
	public static void refresh(RoadContext rdCxt){
		SwingUtilities.invokeLater(new Refresher(rdCxt));
	}
	public static void cleanJunctions(MovsimConfigContext mvCxt) {
		ArrayList<Junction>jns=new ArrayList<>();
		for(Junction j:mvCxt.getRdCxt().getRn().getOdrNetwork().getJunction())
			if(j.getConnection().size()<1)jns.add(j);
		mvCxt.getRdCxt().getRn().getOdrNetwork().getJunction().removeAll(jns);
	}
}
/**
 * used in refresh method all work to refresh is done in separate thread
 * @author Shmeel
 *
 */
class Refresher extends SwingWorker<Object, String>{
	RoadContext rdCxt;
	public Refresher(RoadContext rp) {
		this.rdCxt=rp;
	}

	@Override
	protected Object doInBackground() throws Exception {
		boolean selected=rdCxt.getSelectedRoad()!=null;
		boolean roadExists=rdCxt.getRn().getRoadSegments().size()>0;
		if(selected && roadExists){
			int gmInd=rdCxt.getGmPnl().getSelectedIndex();
			int lnInd=rdCxt.getLanesPnl().getSelectedIndex();
			int ind=rdCxt.getRn().getOdrNetwork().getRoad().indexOf(rdCxt.getSelectedRoad().getOdrRoad());
			if(ind<0)ind=0;
			if(gmInd<0)gmInd=0;
			if(lnInd<0)lnInd=0;
			rdCxt.setSelectedRoadNull();
			rdCxt.getRn().reset();
			new OpenDriveHandlerJaxb().create("", rdCxt.getRn().getOdrNetwork(), rdCxt.getRn());
			RoadNetworkUtils.SetupLights(rdCxt.getMvCxt());
			if(rdCxt.getRn().getRoadSegments().size()-1<ind)ind=rdCxt.getRn().getRoadSegments().size()-1;
			RoadSegment rs=rdCxt.getRn().getRoadSegments().get(ind);
			if(rs.getOdrRoad().getPlanView().getGeometry().size()-1<gmInd)gmInd=rs.getOdrRoad().getPlanView().getGeometry().size()-1;
			if(rs.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size()-1<lnInd)lnInd=rs.getOdrRoad().getPlanView().getGeometry().size()-1;
			rdCxt.getGmPnl().setSelectedGeometry(gmInd,false);
			rdCxt.getLanesPnl().setSelectedLane(lnInd, false);
			rdCxt.setSelectedRoad(rs);
		}else{
			rdCxt.setSelectedRoadNull();
			rdCxt.getRn().reset();
			new OpenDriveHandlerJaxb().create("", rdCxt.getRn().getOdrNetwork(), rdCxt.getRn());
			RoadNetworkUtils.SetupLights(rdCxt.getMvCxt());
		}
		rdCxt.updateGraphics();
		return null;
	}
}
