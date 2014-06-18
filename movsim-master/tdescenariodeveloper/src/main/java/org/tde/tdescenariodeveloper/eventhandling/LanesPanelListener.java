package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.Lane.Link.Predecessor;
import org.movsim.network.autogen.opendrive.Lane.Link.Successor;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.updation.LanesUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
import org.tde.tdescenariodeveloper.validation.LanesValidator;
/**
 * Class used to listen for changes made to lanes of selected road
 * @author Shmeel
 * @see Lane
 * @see LaneLink
 * @see Predecessor
 * @see Successor
 */
public class LanesPanelListener implements DocumentListener, ActionListener,ChangeListener,Blockable {
	boolean locked=true;
	RoadContext rdCxt;
	/**
	 * used for lanes data validation
	 */
	LanesValidator validator;
	/**
	 *  used to update lanes data
	 */
	LanesUpdater updater;
	/**
	 * 
	 * @param roadPropertiesPanel road properties panel
	 */
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
		}else if(srcCb==rdCxt.getLanesPnl().getCbtype()){
			if(rdCxt.getLanesPnl().getCbtype().getSelectedItem()==null || rdCxt.getLanesPnl().getCbtype().getSelectedItem().equals(""))
				return;
			updater.updateLaneType();
			rdCxt.updateGraphics();
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
	/**
	 * called when content of related {@link JTextField} changes
	 * @param e document event
	 */
	private void textChanged(DocumentEvent e){
		if(locked)return;
		Document doc=e.getDocument();
		if(doc==rdCxt.getLanesPnl().getTfWidth().getDocument()){
			if(!Conditions.isValid(rdCxt.getLanesPnl().getTfWidth(),rdCxt.getLanesPnl().getSelectedLane().getWidth().get(0).getA()))
				return;
			try{
				if(validator.isValidWidth()){
					updater.updateWidth();
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast("Lane width is not valid: (1 - 100)", rdCxt.getToastDurationMilis());
			}
		}
		else if(doc==rdCxt.getLanesPnl().getMaxSpeed().getDocument()){
			if(!Conditions.isValid(rdCxt.getLanesPnl().getMaxSpeed(),-44))
				return;
			try{
				if(validator.isValidMaxSpeed()){
					updater.updateMaxSpeed();
					rdCxt.updateGraphics();
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast("Max speed is not valid", rdCxt.getToastDurationMilis());
			}
		}
	}

	public void setBlocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(locked)return;
		JSlider src=null;
		if(e.getSource() instanceof JSlider)src=(JSlider)e.getSource();
		if(!src.getValueIsAdjusting()){
			if(src==rdCxt.getLanesPnl().getPosition()){
				rdCxt.getLanesPnl().getSelectedLane().getSpeed().get(0).setSOffset(src.getValue());
				RoadNetworkUtils.refresh(rdCxt);
				rdCxt.updateGraphics();
			}
		}
	}

}
