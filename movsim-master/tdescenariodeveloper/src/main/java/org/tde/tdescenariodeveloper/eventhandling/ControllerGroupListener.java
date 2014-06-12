package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import org.movsim.autogen.ControllerGroup;
import org.movsim.autogen.Phase;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class ControllerGroupListener implements  ActionListener,Blockable {
	MovsimConfigContext mvCxt;
	List<ControllerGroup>controlllerGroups;
	ControllerGroup controllerGroup;
	private boolean blocked=true;
	private JButton newPhase;
	public ControllerGroupListener(ControllerGroup s, MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		this.controllerGroup=s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=(JButton)e.getSource();
		if(b==newPhase){
			Phase c=new Phase();
			c.setDuration(30);
			controllerGroup.getPhase().add(c);
			mvCxt.getTrafficLights().updateTrafficLightsPanel();
		}
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

	public void setNewPhase(JButton newPhase) {
		this.newPhase=newPhase;
	}

}
