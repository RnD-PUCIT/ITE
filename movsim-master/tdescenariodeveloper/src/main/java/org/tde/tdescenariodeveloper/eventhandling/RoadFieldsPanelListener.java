package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;
import org.tde.tdescenariodeveloper.updation.RoadFieldsUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.validation.RoadFieldsValidator;

public class RoadFieldsPanelListener implements DocumentListener,ActionListener {
	RoadPropertiesPanel rdPrpPnl;
	RoadFieldsUpdater roadFieldsUpdater;
	RoadFieldsValidator validator;
	private boolean blocked=false;
	public RoadFieldsPanelListener(RoadPropertiesPanel roadPropertiesPanel) {
		rdPrpPnl=roadPropertiesPanel;
		this.roadFieldsUpdater=new RoadFieldsUpdater(rdPrpPnl);
		validator=new RoadFieldsValidator(rdPrpPnl);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JComboBox<String>src=null;
		if(e.getSource() instanceof JComboBox<?>)src=(JComboBox<String>)e.getSource();
		if(src==rdPrpPnl.getRdFldPnl().getCbJunction() && ((String)src.getSelectedItem())!=null && !((String)src.getSelectedItem()).equals("None")){
			boolean []isValid=validator.isJunctionValid();
			if(isValid[0]){
				if(isValid[1])
					roadFieldsUpdater.updateRoadJunction();
				else{
					GraphicsHelper.showToast("Road "+rdPrpPnl.getSelectedRoad().userId()+" is not referenced in Junction "+rdPrpPnl.getRdFldPnl().getCbJunction().getSelectedItem(), 5000);
					String jnc=rdPrpPnl.getSelectedRoad().getOdrRoad().getJunction();
					if(jnc.equals("-1"))jnc="None";
					rdPrpPnl.getRdFldPnl().getCbJunction().setSelectedItem(jnc);
				}
				
			}
			else{
				GraphicsHelper.showMessage("Junction doesn't exist");
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		if(blocked)return;
		textChanged(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(blocked)return;
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(blocked)return;
		textChanged(e);
	}
	public void textChanged(DocumentEvent e){
		Document doc=e.getDocument();
		if(rdPrpPnl.getRdFldPnl().getTfName().getDocument()==doc){
			roadFieldsUpdater.updateRoadName();
			rdPrpPnl.updateGraphics();
		}
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
