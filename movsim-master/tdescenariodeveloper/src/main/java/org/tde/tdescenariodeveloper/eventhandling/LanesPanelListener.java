package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.LanesUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.validation.LanesValidator;

public class LanesPanelListener implements DocumentListener, ActionListener {
	boolean locked=true;
	RoadContext rdCxt;
	LanesValidator validator;
	LanesUpdater updater;
	public LanesPanelListener(RoadContext roadPropertiesPanel) {
		rdCxt=roadPropertiesPanel;
		validator=new LanesValidator(rdCxt);
		updater=new LanesUpdater(rdCxt);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(locked)return;
		locked=true;
		JButton srcBtn=null;
		JComboBox<String>srcCb=null;
		if(evt.getSource() instanceof JButton)srcBtn=(JButton)evt.getSource();
		if(evt.getSource() instanceof JComboBox<?>)srcCb=(JComboBox<String>)evt.getSource();
		if(srcCb==rdCxt.getLanesPnl().getCbLanes()){
			if(rdCxt.getLanesPnl().getCbLanes().getSelectedItem()==null)return;
			rdCxt.getLanesPnl().setSelectedLane(rdCxt.getLanesPnl().getCbLanes().getSelectedIndex(),false);
			rdCxt.getLanesPnl().laneChanged();
		}
		else if(srcBtn==rdCxt.getLanesPnl().getAdd()){
			updater.addnewLane();
			rdCxt.updateGraphics();
		}
		else if(srcBtn==rdCxt.getLanesPnl().getRemove()){
			if(validator.isValidRemove()){
				updater.removeLastLane();
				rdCxt.updateGraphics();
			}
			else{
				GraphicsHelper.showToast("Atleast 1 lane is required in the road", rdCxt.getToastDurationMilis());
				rdCxt.getLanesPnl().getRemove().setEnabled(false);
			}
		}
		locked=false;
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
	private void textChanged(DocumentEvent e){
		if(locked)return;
		Document doc=e.getDocument();
		if(doc==rdCxt.getLanesPnl().getTfWidth().getDocument()){
			if(rdCxt.getLanesPnl().getTfWidth().getText().equals("")){
				return;
			}
			try{
				if(validator.isValidWidth()){
					updater.updateWidth();
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast("Lane width is not valid: (1 - 100)", rdCxt.getToastDurationMilis());
			}
		}
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
