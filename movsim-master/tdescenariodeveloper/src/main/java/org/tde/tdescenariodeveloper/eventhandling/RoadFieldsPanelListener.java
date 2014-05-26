package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.RoadFieldsUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.validation.RoadFieldsValidator;

public class RoadFieldsPanelListener implements DocumentListener,ActionListener {
	RoadContext rdCxt;
	RoadFieldsUpdater roadFieldsUpdater;
	RoadFieldsValidator validator;
	private boolean blocked=true;
	public RoadFieldsPanelListener(RoadContext roadPropertiesPanel) {
		rdCxt=roadPropertiesPanel;
		this.roadFieldsUpdater=new RoadFieldsUpdater(rdCxt);
		validator=new RoadFieldsValidator(rdCxt);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JComboBox<String>src=null;
		if(e.getSource() instanceof JComboBox<?>)src=(JComboBox<String>)e.getSource();
		if(src==rdCxt.getRdFldPnl().getCbJunction() && ((String)src.getSelectedItem())!=null && !((String)src.getSelectedItem()).equals("None")){
			boolean []isValid=validator.isJunctionValid();
			if(isValid[0]){
				if(isValid[1])
					roadFieldsUpdater.updateRoadJunction();
				else{
					GraphicsHelper.showToast("Road "+rdCxt.getSelectedRoad().userId()+" is not referenced in Junction "+rdCxt.getRdFldPnl().getCbJunction().getSelectedItem(), rdCxt.getToastDurationMilis());
					String jnc=rdCxt.getSelectedRoad().getOdrRoad().getJunction();
					if(jnc.equals("-1"))jnc="None";
					rdCxt.getRdFldPnl().getCbJunction().setSelectedItem(jnc);
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
		if(rdCxt.getRdFldPnl().getTfName().getDocument()==doc){
			if(rdCxt.getRdFldPnl().getTfName().getText().equals("")){
				return;
			}
			roadFieldsUpdater.updateRoadName();
			rdCxt.updateGraphics();
		}
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
