package org.tde.tdescenariodeveloper.updation;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.Lane.Width;
import org.tde.tdescenariodeveloper.ui.RoadContext;
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
		rdCxt.getLanesPnl().getOdrLanes().remove(rdCxt.getLanesPnl().getOdrLanes().size()-1);
		RoadNetworkUtils.updateLaneIds(rdCxt);
		RoadNetworkUtils.refresh(rdCxt);
	}
	public void updateWidth(){
		double d=Double.parseDouble(rdCxt.getLanesPnl().getTfWidth().getText());
		Width w=new Width();
		w.setA(d);
		w.setB(0);
		w.setC(0);
		w.setD(0);
		w.setSOffset(0);
		for(Lane ln:rdCxt.getLanesPnl().getOdrLanes()){
			ln.getWidth().clear();
			ln.getWidth().add(w);
		}
		RoadNetworkUtils.refresh(rdCxt);
	}
}
