package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import org.movsim.autogen.ControllerGroup;
import org.movsim.autogen.Phase;
import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLightStatus;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Class for listening changes made to related {@link ControllerGroup}.
 * @author Shmeel
 * @see ControllerGroup
 */
public class ControllerGroupListener implements  ActionListener,Blockable {
	MovsimConfigContext mvCxt;
	List<ControllerGroup>controlllerGroups;
	ControllerGroup controllerGroup;
	private boolean blocked=true;
	private JButton newPhase;
	/**
	 * 
	 * @param s controller group to which this listener is attached
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
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
			for(TrafficLightState st:controllerGroup.getPhase().get(controllerGroup.getPhase().size()-1).getTrafficLightState()){
				TrafficLightState tls=new TrafficLightState();
				tls.setCondition(st.getCondition());
				tls.setName(st.getName());
				if(st.getStatus()==TrafficLightStatus.GREEN)tls.setStatus(TrafficLightStatus.GREEN_RED);
				else if(st.getStatus()==TrafficLightStatus.GREEN_RED)tls.setStatus(TrafficLightStatus.RED); 
				else if(st.getStatus()==TrafficLightStatus.RED)tls.setStatus(TrafficLightStatus.GREEN); 
				c.getTrafficLightState().add(tls);
			}
			controllerGroup.getPhase().add(c);
			mvCxt.getTrafficLights().updateTrafficLightsPanel();
		}
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}
	/**
	 * sets button to which this listener is attached.
	 * @param newPhase button to be listened
	 */
	public void setNewPhase(JButton newPhase) {
		this.newPhase=newPhase;
	}

}
