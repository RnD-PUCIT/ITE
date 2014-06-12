package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.tde.tdescenariodeveloper.ui.ControllerPanel;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class ControlListener implements  ActionListener,Blockable {
	RoadContext rdCxt;
	List<Control>controls;
	Control control;
	JButton remove;
	private boolean blocked=true;
	JComboBox<String>cbId;
	public ControlListener(Control s, List<Control> controls, RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		this.controls=controls;
		this.control=s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=null;
		JComboBox<String>srcCb=null;
		if(e.getSource() instanceof JButton)b=(JButton)e.getSource();
		if(e.getSource() instanceof JComboBox<?>)srcCb=(JComboBox<String>)e.getSource();
		if(b==remove){
			//remove related signals first
			if(ControllerPanel.getControllerCount(control.getSignalId(),rdCxt)<2)ControllerPanel.removeRelatedSignals(control.getSignalId(), rdCxt);
			controls.remove(control);
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updatePanel();
			rdCxt.getMvCxt().updatePanels();
		}else if(srcCb==cbId){
			if(((String)cbId.getSelectedItem()).equals("") || ((String)cbId.getSelectedItem()).equals("None") || ((String)cbId.getSelectedItem()).equals(control.getSignalId()) )
				return;
			control.setSignalId((String)cbId.getSelectedItem());
		}
	}

	public void setRemove(JButton remove) {
		this.remove = remove;
	}

	public void setIdCb(JComboBox<String>cbId) {
		this.cbId=cbId;
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

}
