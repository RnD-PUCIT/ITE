package org.tde.tdescenariodeveloper.utils;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.ui.RoadContext;

public class RoadNetworkUtils {
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
