package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.tde.tdescenariodeveloper.ui.RoadFieldsPanel;
import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;
import org.tde.tdescenariodeveloper.updation.RoadFieldsUpdater;
import org.tde.tdescenariodeveloper.validation.RoadFieldsValidator;
import org.tde.tdescenariodevelopment.utils.GraphicsHelper;

public class RoadFieldsListener implements DocumentListener,ActionListener {
	RoadFieldsPanel rdFldsPnl;
	RoadPropertiesPanel rdPrpPnl;
	RoadFieldsUpdater roadFieldsUpdater;
	RoadFieldsValidator validator;
	public RoadFieldsListener(RoadPropertiesPanel roadPropertiesPanel) {
		rdPrpPnl=roadPropertiesPanel;
		this.roadFieldsUpdater=new RoadFieldsUpdater(rdPrpPnl);
		validator=new RoadFieldsValidator(rdPrpPnl);
	}
	public void setRdFldsPnl() {
		rdFldsPnl=rdPrpPnl.getRdFldPnl();;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox<String>src=null;
		if(e.getSource() instanceof JComboBox<?>)src=(JComboBox<String>)e.getSource();
		if(src==rdFldsPnl.getCbJunction() && ((String)src.getSelectedItem())!=null && !((String)src.getSelectedItem()).equals("None")){
			if(validator.isJunctionValid()){
				roadFieldsUpdater.updateRoadJunction();
			}
			else{
				GraphicsHelper.showMessage("Junction doesn't exist");
			}
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
	public void textChanged(DocumentEvent e){
		Document doc=e.getDocument();
		if(rdFldsPnl.getTfName().getDocument()==doc){
			roadFieldsUpdater.updateRoadName();
			rdPrpPnl.updateGraphics();
		}
	}

}
