package org.tde.tdescenariodeveloper.updation;

import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;

public class RoadFieldsUpdater {
	RoadPropertiesPanel roadPropertiesPanel;
	public RoadFieldsUpdater(RoadPropertiesPanel rpp) {
		roadPropertiesPanel=rpp;
	}
	public void updateRoadName(){
		roadPropertiesPanel.getSelectedRoad().getOdrRoad().setName(roadPropertiesPanel.getRdFldPnl().getTfName().getText());
		roadPropertiesPanel.getSelectedRoad().setRoadName(roadPropertiesPanel.getRdFldPnl().getTfName().getText());
	}
	public void updateRoadJunction(){
		roadPropertiesPanel.getSelectedRoad().getOdrRoad().setJunction((String)roadPropertiesPanel.getRdFldPnl().getCbJunction().getSelectedItem());
//		roadPropertiesPanel.getSelectedRoad().setRoadName(roadPropertiesPanel.getRdFldPnl().getTfName().getText());
	}
}
