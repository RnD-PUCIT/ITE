package org.tde.tdescenariodeveloper.updation;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.Lane.Speed;
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
		double d=rdCxt.getLanesPnl().getOdrLanes().get(0).getWidth().get(0).getA();
		Width w=new Width();
		w.setA(d);
		w.setB(0);
		w.setC(0);
		w.setD(0);
		w.setSOffset(0);
		ln.getWidth().add(w);
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
	public void updateMaxSpeed(){
		double d=Double.parseDouble(rdCxt.getLanesPnl().getMaxSpeed().getText());
		double pos=0;
		if(rdCxt.getLanesPnl().getSelectedLane().getSpeed().size()>0 && rdCxt.getLanesPnl().getSelectedLane().getSpeed().get(0).isSetSOffset())
			pos=rdCxt.getLanesPnl().getSelectedLane().getSpeed().get(0).getSOffset();
		rdCxt.getLanesPnl().getSelectedLane().getSpeed().clear();
		Speed s=new Speed();
		s.setMax(d);
		s.setSOffset(pos);
		rdCxt.getLanesPnl().getSelectedLane().getSpeed().add(s);
		RoadNetworkUtils.refresh(rdCxt);
	}
	public void updateLaneType() {
		rdCxt.getLanesPnl().getSelectedLane().setType((String)rdCxt.getLanesPnl().getCbtype().getSelectedItem());
		RoadNetworkUtils.refresh(rdCxt);		
	}
	
}
