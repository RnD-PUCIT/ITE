package org.tde.tdescenariodeveloper.utils;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;

public class RoadNetworkUtils {
	public static void refresh(RoadPropertiesPanel rdPrPnl){
		SwingUtilities.invokeLater(new Refresher(rdPrPnl));
	}
	public static void updateCoordinatesAndHeadings(RoadPropertiesPanel rdPrPnl) {
		if(rdPrPnl.getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
			RoadMappingPoly rmp=(RoadMappingPoly)rdPrPnl.getSelectedRoad().roadMapping();
			for(int i=1;i<rmp.getRoadMappings().size();i++){
				rmp=(RoadMappingPoly)OpenDriveHandlerJaxb.createRoadMapping(rdPrPnl.getSelectedRoad().getOdrRoad());
				Geometry gm=rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(i);
				PosTheta preEnd=rmp.getRoadMappings().get(i-1).map(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(i-1).getLength());
				gm.setHdg(preEnd.theta());
				gm.setX(preEnd.x);
				gm.setY(preEnd.y);
			}
		}
	}
}
class Refresher extends SwingWorker<Object, String>{
	RoadPropertiesPanel rdPrPnl;
	public Refresher(RoadPropertiesPanel rp) {
		this.rdPrPnl=rp;
	}

	@Override
	protected Object doInBackground() throws Exception {
		int gmInd=rdPrPnl.getGmPnl().getSelectedIndex();
		int ind=rdPrPnl.getRn().getOdrNetwork().getRoad().indexOf(rdPrPnl.getSelectedRoad().getOdrRoad());
		if(ind<0)ind=0;
		if(gmInd<0)gmInd=0;
		rdPrPnl.getRn().reset();
		new OpenDriveHandlerJaxb().create("", rdPrPnl.getRn().getOdrNetwork(), rdPrPnl.getRn());
		if(rdPrPnl.getRn().getRoadSegments().size()-1<ind)ind=rdPrPnl.getRn().getRoadSegments().size()-1;
		RoadSegment rs=rdPrPnl.getRn().getRoadSegments().get(ind);
		if(rs.getOdrRoad().getPlanView().getGeometry().size()-1<gmInd)gmInd=rs.getOdrRoad().getPlanView().getGeometry().size()-1;
		rdPrPnl.getGmPnl().setSelectedGeometry(gmInd,false);
		rdPrPnl.setSelectedRoad(rs);
		return null;
	}
	
}
