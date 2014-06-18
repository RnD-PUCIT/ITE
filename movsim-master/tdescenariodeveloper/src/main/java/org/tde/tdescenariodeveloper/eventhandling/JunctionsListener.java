package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.JunctionsUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.validation.JunctionsValidator;
/**
 * Class for listening chages made to related {@link Junction} 
 * @author Shmeel
 * @see Junction
 */
public class JunctionsListener implements ActionListener, ChangeListener,Blockable {
	RoadContext rdCxt;
	JunctionsUpdater updater;
	JunctionsValidator validator;
	boolean blocked=true;
	/**
	 * 
	 * @param rd contains reference to loaded .xodr and other added panels in it
	 */
	public JunctionsListener(RoadContext rd) {
		rdCxt=rd;
		updater=new JunctionsUpdater(rd);
		validator=new JunctionsValidator(rd);
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
			String id=updater.addNewJunc().getId();
			rdCxt.getAppFrame().getJp().setSelectedJn(id+"");
			rdCxt.getAppFrame().getJp().getCbSelectJunc().addItem(id+"");
			
			rdCxt.getAppFrame().getJp().setSelectedJn(id);
			rdCxt.getAppFrame().getJp().getCbSelectJunc().setSelectedItem(id);
			rdCxt.getAppFrame().getJp().getAddCn().setEnabled(true);
			rdCxt.getAppFrame().getJp().getRemove().setEnabled(true);
			rdCxt.updateGraphics();
		}
		if(rdCxt.getAppFrame().getJp().getSelectedJn()==null || rdCxt.getAppFrame().getJp().getSelectedJn().equals("")){
			return;
		}
		if(src==rdCxt.getAppFrame().getJp().getCbSelectJunc()){
			String slct=(String)rdCxt.getAppFrame().getJp().getCbSelectJunc().getSelectedItem();
			rdCxt.getAppFrame().getJp().setSelectedJn(slct);
			rdCxt.getAppFrame().getJp();
			rdCxt.getAppFrame().getJp();
			rdCxt.getAppFrame().getJp().updateJunctionPanel(JunctionsUpdater.getJunction(slct, rdCxt));
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
