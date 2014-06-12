package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.ControllerGroup;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.tde.tdescenariodeveloper.ui.ControllerPanel;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class ControllerListener implements DocumentListener, ActionListener,Blockable {
	RoadContext rdCxt;
	List<Controller>controllers;
	Controller controller;
	JButton remove;
	private boolean blocked=true;
	private JTextField id;
	private JButton newControl;
	public ControllerListener(Controller s, List<Controller> controllers, RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		this.controllers=controllers;
		this.controller=s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b=(JButton)e.getSource();
		if(b==remove){
			//remove related Controller Group
			for(ControllerGroup cg: rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup()){
				if(cg.getId().equals(controller.getId()))
					rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup().remove(cg);
			}
			//remove related signals
			for(Control c:controller.getControl())
				if(ControllerPanel.getControllerCount(c.getSignalId(), rdCxt)<2)ControllerPanel.removeRelatedSignals(c.getSignalId(), rdCxt);
			controllers.remove(controller);
			
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updatePanel();
			rdCxt.getMvCxt().updatePanels();
		}
		else if(b==newControl){
			String id=ControllerPanel.getNotUsedSignalId(rdCxt, controller);
			if(id==null){
				GraphicsHelper.showToast("No signal found or all signals already added", rdCxt.getToastDurationMilis());
				return;
			}
			Control c=new Control();
			c.setSignalId(id);
			controller.getControl().add(c);
			rdCxt.getMvCxt().getTrafficLights().getControllerPanel().updateControllerPanel();
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

	private void textChanged(DocumentEvent e) {
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==id.getDocument()){
			if(!Conditions.isValid(id,controller.getId()))
				return;
			String old=controller.getId();
			controller.setId(id.getText());
			adjustControllerGroupIds(rdCxt.getMvCxt(), old, id.getText());
			rdCxt.updatePanel();
			rdCxt.getMvCxt().updatePanels();
		}
	}

	public void setRemove(JButton remove) {
		this.remove = remove;
	}

	public void setIdtf(JTextField id) {
		this.id=id;
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

	public void setNewControl(JButton newControl) {
		this.newControl=newControl;
	}
	public static void adjustControllerGroupIds(MovsimConfigContext mvCxt,String old,String newId){
		for(ControllerGroup cg:mvCxt.getMovsim().getScenario().getTrafficLights().getControllerGroup()){
			if(cg.getId().equals(old))cg.setId(newId);
		}
	}
}
