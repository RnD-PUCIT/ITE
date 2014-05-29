package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.JunctionsUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.validation.JunctionsValidator;

public class JunctionsListener implements ActionListener, ChangeListener,
		DocumentListener {
	RoadContext rdCxt;
	JunctionsUpdater updater;
	JunctionsValidator validator;
	boolean blocked=true;
	public JunctionsListener(RoadContext rd) {
		rdCxt=rd;
		updater=new JunctionsUpdater(rd);
		validator=new JunctionsValidator(rd);
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		if(blocked)return;

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(blocked)return;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(blocked)return;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(blocked)return;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JComboBox<String>src=null;
		JButton srcBtn=null;
		if(e.getSource() instanceof JComboBox<?>)src=(JComboBox<String>)e.getSource();
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		if(srcBtn==rdCxt.getAppFrame().getJp().getAdd()){
			if(validator.exists(rdCxt.getAppFrame().getJp().getSelectedJn())){
				updater.addNewJunc();
				actionPerformed(new ActionEvent(rdCxt.getAppFrame().getJp().getCbSelectJunc(), 234, ""));
				rdCxt.updateGraphics();
			}else{
				GraphicsHelper.showToast("Junction "+rdCxt.getAppFrame().getJp().getSelectedJn()+" doesn't exist.", rdCxt.getToastDurationMilis());
			}
		}
		if(rdCxt.getAppFrame().getJp().getSelectedJn()==null || rdCxt.getAppFrame().getJp().getSelectedJn().equals("")){
			GraphicsHelper.showToast("No junction found", rdCxt.getToastDurationMilis());
			return;
		}
		if(src==rdCxt.getAppFrame().getJp().getCbSelectJunc()){
			String slct=(String)rdCxt.getAppFrame().getJp().getCbSelectJunc().getSelectedItem();
			rdCxt.getAppFrame().getJp().setSelectedJn(slct);
			rdCxt.getAppFrame().getJp().updateJunctionPanel(rdCxt.getAppFrame().getJp().getJunction(slct));
		}else if(srcBtn==rdCxt.getAppFrame().getJp().getRemove()){
			if(validator.exists(rdCxt.getAppFrame().getJp().getSelectedJn())){
				updater.removeJunc();
				rdCxt.updateGraphics();
			}else{
				GraphicsHelper.showToast("Junction "+rdCxt.getAppFrame().getJp().getSelectedJn()+" doesn't exist.", rdCxt.getToastDurationMilis());
			}
		}else if(srcBtn==rdCxt.getAppFrame().getJp().getAddCn()){
			if(validator.exists(rdCxt.getAppFrame().getJp().getSelectedJn())){
				updater.addNewConn();
				rdCxt.updateGraphics();
				actionPerformed(new ActionEvent(rdCxt.getAppFrame().getJp().getCbSelectJunc(), 234, ""));
			}else{
				GraphicsHelper.showToast("Select valid junction first", rdCxt.getToastDurationMilis());
			}
			
		}
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

}
