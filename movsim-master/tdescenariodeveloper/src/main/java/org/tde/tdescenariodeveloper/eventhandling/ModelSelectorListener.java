package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.movsim.autogen.AccelerationModelType;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;

public class ModelSelectorListener implements ActionListener,Blockable {
	MovsimConfigContext mvCxt;
	JComboBox<String>src;
	AccelerationModelType accelerationModelType;
	boolean blocked=true;
	
	public ModelSelectorListener(MovsimConfigContext mvCxt, AccelerationModelType accelerationModelType) {
		this.mvCxt=mvCxt;
		this.accelerationModelType=accelerationModelType;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		String selectedItem=(String)src.getSelectedItem();
		switch(selectedItem){
		case "ACC":
			clearMdls();
			accelerationModelType.setModelParameterACC(MovsimScenario.getModelParameterACC());
			mvCxt.updatePanels();
			break;
		case "IDM":
			clearMdls();
			accelerationModelType.setModelParameterIDM(MovsimScenario.getModelParameterIDM());
			mvCxt.updatePanels();
			break;
		case "Gipps":
			clearMdls();
			accelerationModelType.setModelParameterGipps(MovsimScenario.getModelParameterGipps());
			mvCxt.updatePanels();
			break;
		case "NSM":
			clearMdls();
			accelerationModelType.setModelParameterNSM(MovsimScenario.getModelParameterNSM());
			mvCxt.updatePanels();
			break;
		case "Krauss":
			clearMdls();
			accelerationModelType.setModelParameterKrauss(MovsimScenario.getModelParameterKrauss());
			mvCxt.updatePanels();
			break;
		case "Newell":
			clearMdls();
			accelerationModelType.setModelParameterNewell(MovsimScenario.getModelParameterNewell());
			mvCxt.updatePanels();
			break;
		}
	}
	public void clearMdls(){
		accelerationModelType.setModelParameterACC(null);
		accelerationModelType.setModelParameterNSM(null);
		accelerationModelType.setModelParameterNewell(null);
		accelerationModelType.setModelParameterIDM(null);
		accelerationModelType.setModelParameterKrauss(null);
		accelerationModelType.setModelParameterGipps(null);
	}
	public void setSrc(JComboBox<String> src) {
		this.src = src;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
