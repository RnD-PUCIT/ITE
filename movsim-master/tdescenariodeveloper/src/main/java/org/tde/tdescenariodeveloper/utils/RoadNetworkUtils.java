package org.tde.tdescenariodeveloper.utils;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;

public class RoadNetworkUtils {
	public static void refresh(RoadPropertiesPanel rdPrPnl){
		rdPrPnl.getRn().reset();
		new OpenDriveHandlerJaxb().create("", rdPrPnl.getRn().getOdrNetwork(), rdPrPnl.getRn());
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
