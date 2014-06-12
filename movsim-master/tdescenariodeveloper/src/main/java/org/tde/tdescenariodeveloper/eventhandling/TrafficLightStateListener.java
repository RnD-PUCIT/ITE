package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.Phase;
import org.movsim.autogen.TrafficLightCondition;
import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLightStatus;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class TrafficLightStateListener implements ActionListener, Blockable,
		DocumentListener {
	boolean blocked=true;
	MovsimConfigContext mvCxt;
	JTextField name;
	JComboBox<String>cbCondition,cbStatus;
	TrafficLightState state;
	private List<TrafficLightState> states;
	JButton remove;
	
	public TrafficLightStateListener(TrafficLightState st,List<TrafficLightState> states, MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		this.state=st;
		this.states=states;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=null;
		if(e.getSource() instanceof JButton)b=(JButton)e.getSource();
		JComboBox<String> cb=null;
		if(e.getSource() instanceof JComboBox<?>)cb=(JComboBox<String>)e.getSource();
		if(b==remove){
			states.remove(state);
//			RoadNetworkUtils.refresh(rdCxt);
//			rdCxt.updatePanel();
//			rdCxt.getMvCxt().updatePanels();
			mvCxt.getTrafficLights().updateTrafficLightsPanel();
		}else if(cb==cbCondition){
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
	}

	private void textChanged(DocumentEvent e) {
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==name.getDocument()){
			if(!Conditions.isValid(name, state.getName()))return;
			state.setName(name.getText());
		}
	}

	public void setName(JTextField name) {
		this.name = name;
	}

	public void setCbCondition(JComboBox<String> cbCondition) {
		this.cbCondition = cbCondition;
	}

	public void setCbStatus(JComboBox<String> cbStatus) {
		this.cbStatus = cbStatus;
	}

	public void setRemove(JButton remove) {
		this.remove = remove;
	}
}
