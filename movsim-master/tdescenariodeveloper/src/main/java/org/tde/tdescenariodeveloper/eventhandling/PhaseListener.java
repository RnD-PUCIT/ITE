package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.naming.ldap.Rdn;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.Phase;
import org.movsim.autogen.TrafficLightCondition;
import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLightStatus;
import org.movsim.autogen.TrafficLights;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Class used to listen changes made to {@link Phase}
 * @author Shmeel
 * @see Phase
 * @see TrafficLightState
 * @see TrafficLights
 */
public class PhaseListener implements  ActionListener,Blockable ,DocumentListener{
	MovsimConfigContext mvCxt;
	List<Phase>phases;
	Phase phase;
	JButton remove;
	JTextField duration;
	private boolean blocked=true;
	private JButton newState;
	/**
	 * 
	 * @param s {@link Phase}
	 * @param phases list of {@link Phase}s in which above referred phase is contained
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
	public PhaseListener(Phase s, List<Phase> phases, MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		this.phases=phases;
		this.phase=s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=null;
		if(e.getSource() instanceof JButton)b=(JButton)e.getSource();
		if(b==remove){
			phases.remove(phase);
			mvCxt.getTrafficLights().updateTrafficLightsPanel();
		}else if(b==newState){
			TrafficLightState s=new TrafficLightState();
			s.setCondition(TrafficLightCondition.NONE);
			String nm=RoadNetworkUtils.getFirstSignal(mvCxt);
			if(nm==null){
				GraphicsHelper.showToast("No signal found", mvCxt.getRdCxt().getToastDurationMilis());
				return;
			}
			s.setName(nm);
			s.setStatus(TrafficLightStatus.GREEN);
			phase.getTrafficLightState().add(s);
			mvCxt.getTrafficLights().updateTrafficLightsPanel();
		}
	}

	public void setRemove(JButton remove) {
		this.remove = remove;
	}


	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged(e);
		
	}
/**
 * called when text is changed of related {@link JTextField}
 * @param e
 */
	private void textChanged(DocumentEvent e) {
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==duration.getDocument()){
			if(!Conditions.isValid(duration, phase.getDuration()))return;
			try{
				phase.setDuration(Double.parseDouble(duration.getText()));
			}
			catch(NumberFormatException e2){
				GraphicsHelper.showToast("Value invalid for phase duration", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);
	}

	public void setDuration(JTextField duration) {
		this.duration = duration;
	}

	public void setNewStete(JButton newState) {
		this.newState=newState;
	}

}
