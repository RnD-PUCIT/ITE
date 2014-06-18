package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.movsim.autogen.TrafficLightCondition;
import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLightStatus;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
/**
 * Class used to listen for changes made to {@link TrafficLightState}
 * @author Shmeel
 * @see TrafficLightState
 * @see TrafficLightCondition
 * @see TrafficLightStatus
 * @see Signal
 * @see Control
 * @see ControlListener
 * @see SignalListener
 */
public class TrafficLightStateListener implements ActionListener, Blockable {
	boolean blocked=true;
	MovsimConfigContext mvCxt;
	JComboBox<String>cbCondition,cbStatus,name;
	TrafficLightState state;
	private List<TrafficLightState> states;
//	JButton remove;
	/**
	 * 
	 * @param st {@link TrafficLightState} to which this listener is attached
	 * @param states {@link List} of states in which above referred {@link TrafficLightState} is contained
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
	public TrafficLightStateListener(TrafficLightState st,List<TrafficLightState> states, MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		this.state=st;
		this.states=states;
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JComboBox<String> cb=null;
		if(e.getSource() instanceof JComboBox<?>)cb=(JComboBox<String>)e.getSource();
//		if(b==remove){
//			states.remove(state);
//			mvCxt.getTrafficLights().updateTrafficLightsPanel();
//		}else 
		if(cb==cbCondition){
			String s=(String)cbCondition.getSelectedItem();
			switch(s){
			case "none":
				state.setCondition(TrafficLightCondition.NONE);
				break;
			case "clear":
				state.setCondition(TrafficLightCondition.CLEAR);
				break;
			case "request":
				state.setCondition(TrafficLightCondition.REQUEST);
				break;
			}
			mvCxt.getTrafficLights().updateTrafficLightsPanel();
		}else if(cb==cbStatus){
			String s=(String)cbStatus.getSelectedItem();
			switch(s){//Red","RedGreen","Green","GreenRed
			case "Red":
				state.setStatus(TrafficLightStatus.RED);
				break;
			case "RedGreen":
				state.setStatus(TrafficLightStatus.RED_GREEN);
				break;
			case "Green":
				state.setStatus(TrafficLightStatus.GREEN);
				break;
			case "GreenRed":
				state.setStatus(TrafficLightStatus.GREEN_RED);
				break;
			}
			mvCxt.getTrafficLights().updateTrafficLightsPanel();
		}
		else if(cb==name){
			String s=(String)name.getSelectedItem();
			state.setName(s);
		}
	}
//
//	public void setName(JComboBox name) {
//		this.name = name;
//	}

	public void setCbCondition(JComboBox<String> cbCondition) {
		this.cbCondition = cbCondition;
	}

	public void setCbStatus(JComboBox<String> cbStatus) {
		this.cbStatus = cbStatus;
	}
//
//	public void setRemove(JButton remove) {
//		this.remove = remove;
//	}
}
