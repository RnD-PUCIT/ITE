package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLights;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.ui.SignalsPanel;
import org.tde.tdescenariodeveloper.ui.TrafficLightsPanel;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Class used to listen changes made to Signal of selected road
 * @author Shmeel
 * @see Signal
 * @see Control
 * @see TrafficLights
 * @see TrafficLightState
 * @see TrafficLightStateListener
 * @see TrafficLightsPanel
 */
public class SignalListener implements DocumentListener, ActionListener,
		ChangeListener,Blockable {
	RoadContext rdCxt;
	List<Signal>signals;
	Signal signal;
	JButton remove;
	private boolean blocked=true;
	private JTextField id;
	private JSlider slider;
	/**
	 * 
	 * @param s {@link Signal} to which this listener is attached
	 * @param signals {@link List} of signals in which above referred signal is contained
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 */
	public SignalListener(Signal s, List<Signal> signals, RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		this.signals=signals;
		this.signal=s;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(blocked)return;
		JSlider src=null;
		if(e.getSource() instanceof JSlider)src=(JSlider)e.getSource();
		if(!src.getValueIsAdjusting()){
			if(src==slider){
				signal.setS(src.getValue());
				RoadNetworkUtils.refresh(rdCxt);
				rdCxt.getMvCxt().updatePanels();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b=(JButton)e.getSource();
		if(b==remove){
			String old=signal.getId();
			signals.remove(signal);
			SignalsPanel.adjustControllersAfterSignalRemove(rdCxt,old);
			if(signals.size()<1)rdCxt.getSelectedRoad().getOdrRoad().setSignals(null);
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.getMvCxt().updatePanels();
		}
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
/**
 * Called when text of related {@link JTextField} is changed
 * @param e
 */
	private void textChanged(DocumentEvent e) {
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==id.getDocument()){
			if(!Conditions.isValid(id,signal.getId()))
				return;
			String old=signal.getId();
			signal.setId(id.getText());
			signal.setName(id.getText());
			SignalsPanel.adjustControllers(rdCxt,old,signal.getId());
			SignalsPanel.adjustControllersGroups(rdCxt,old,signal.getId());
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updatePanel();
			rdCxt.getMvCxt().updatePanels();
		}
	}

	public void setRemove(JButton remove) {
		this.remove = remove;
	}

	public void setId(JTextField id) {
		this.id=id;
	}

	public void setSlider(JSlider slider) {
		this.slider=slider;
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

}
