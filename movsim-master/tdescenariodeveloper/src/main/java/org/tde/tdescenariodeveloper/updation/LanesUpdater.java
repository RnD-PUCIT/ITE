package org.tde.tdescenariodeveloper.updation;

import org.movsim.network.autogen.opendrive.Lane;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class LanesUpdater {
	RoadContext rdCxt;
	public LanesUpdater(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}

	public void addnewLane() {
		Lane ln=new Lane();
		ln.setId(-1*rdCxt.getSelectedRoad().getLaneSegments().length-1);
		ln.setLevel("false");
		ln.setType("driving");
		rdCxt.getLanesPnl().getOdrLanes().add(ln);
		RoadNetworkUtils.updateLaneIds(rdCxt);
		RoadNetworkUtils.refresh(rdCxt);
	}
	public void removeLastLane() {
		GraphicsHelper.showMessage(rdCxt.getLanesPnl().getOdrLanes().size()+"");
		rdCxt.getLanesPnl().getOdrLanes().remove(rdCxt.getLanesPnl().getOdrLanes().size()-1);
		GraphicsHelper.showMessage(rdCxt.getLanesPnl().getOdrLanes().size()+"");
		RoadNetworkUtils.updateLaneIds(rdCxt);
		RoadNetworkUtils.refresh(rdCxt);
	}
}
