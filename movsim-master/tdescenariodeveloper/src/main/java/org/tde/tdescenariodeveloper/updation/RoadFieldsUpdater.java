package org.tde.tdescenariodeveloper.updation;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.tde.tdescenariodeveloper.ui.RoadContext;
/**
 * class used to udpate properties of a {@link Road}
 * @author Shmeel
 *
 */
public class RoadFieldsUpdater {
	RoadContext roadPropertiesPanel;
	/**
	 * 
	 * @param rpp contains reference to loaded .xodr file and other panels added to it
	 */
	public RoadFieldsUpdater(RoadContext rpp) {
		roadPropertiesPanel=rpp;
	}
	/**
	 * updates road's name
	 */
	public void updateRoadName(){
		roadPropertiesPanel.getSelectedRoad().getOdrRoad().setName(roadPropertiesPanel.getRdFldPnl().getTfName().getText());
		roadPropertiesPanel.getSelectedRoad().setRoadName(roadPropertiesPanel.getRdFldPnl().getTfName().getText());
	}
	/**
	 * updates Road's junction id
	 */
	public void updateRoadJunction(){
		roadPropertiesPanel.getSelectedRoad().getOdrRoad().setJunction((String)roadPropertiesPanel.getRdFldPnl().getCbJunction().getSelectedItem());
		roadPropertiesPanel.getAppFrame().getJp().updateJunction();
	}
}
