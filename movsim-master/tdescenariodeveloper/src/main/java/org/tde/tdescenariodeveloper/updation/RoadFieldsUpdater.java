package org.tde.tdescenariodeveloper.updation;

import org.tde.tdescenariodeveloper.ui.RoadContext;

public class RoadFieldsUpdater {
	RoadContext roadPropertiesPanel;
	public RoadFieldsUpdater(RoadContext rpp) {
		roadPropertiesPanel=rpp;
	}
	public void updateRoadName(){
		roadPropertiesPanel.getSelectedRoad().getOdrRoad().setName(roadPropertiesPanel.getRdFldPnl().getTfName().getText());
		roadPropertiesPanel.getSelectedRoad().setRoadName(roadPropertiesPanel.getRdFldPnl().getTfName().getText());
	}
	public void updateRoadJunction(){
		roadPropertiesPanel.getSelectedRoad().getOdrRoad().setJunction((String)roadPropertiesPanel.getRdFldPnl().getCbJunction().getSelectedItem());
		roadPropertiesPanel.getAppFrame().getJp().updateJunction();
	}
}