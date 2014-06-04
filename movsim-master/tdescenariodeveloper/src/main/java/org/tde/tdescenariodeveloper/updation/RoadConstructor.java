package org.tde.tdescenariodeveloper.updation;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class RoadConstructor {

	public static void ConstructStraigh(Point p, MovsimConfigContext mvCxt, MouseEvent e) {
		try {
			mvCxt.getRdCxt().getDrawingArea().transform.inverseTransform(new Point(e.getX()-mvCxt.getRdCxt().getAppFrame().getToolbar().getWidth(),e.getY()), p);
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
			}else{
				r.getPlanView().getGeometry().get(0).setX(p.x);
				r.getPlanView().getGeometry().get(0).setY(p.y);
				r.getPlanView().getGeometry().get(0).setHdg(0);
			}
			mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad().add(r);
			RoadNetworkUtils.refresh(mvCxt.getRdCxt());
		} catch (NoninvertibleTransformException e1) {
			GraphicsHelper.showToast(e1.getMessage(), mvCxt.getRdCxt().getToastDurationMilis());
		}		
	}
	
}
