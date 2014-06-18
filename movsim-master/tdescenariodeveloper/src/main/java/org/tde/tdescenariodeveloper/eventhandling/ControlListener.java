package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.movsim.autogen.ControllerGroup;
import org.movsim.autogen.Phase;
import org.movsim.autogen.TrafficLightCondition;
import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLightStatus;
import org.movsim.autogen.TrafficLights;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.tde.tdescenariodeveloper.ui.ControllerPanel;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.ui.TrafficLightsPanel;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Class used to listen changes made to {@link Control}
 * @author Shmeel
 * @see Control
 */
public class ControlListener implements  ActionListener,Blockable {
	RoadContext rdCxt;
	Controller controller;
	Control control;
//	JButton remove;
	JComboBox<String> move;
	public void setMove(JComboBox<String> move) {
		this.move = move;
	}

	private boolean blocked=true;
//	JComboBox<String>cbId;
	/**
	 * 
	 * @param s {@link Control} to which this listener is attached
	 * @param controller {@link Controller} which contains above control
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 */
	public ControlListener(Control s, Controller controller, RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		this.controller=controller;
		this.control=s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=null;
		JComboBox<String>srcCb=null;
		if(e.getSource() instanceof JButton)b=(JButton)e.getSource();
		if(e.getSource() instanceof JComboBox<?>)srcCb=(JComboBox<String>)e.getSource();
//		if(b==remove){
//			//remove related signals first
//			ControllerPanel.removeRelatedSignals(control.getSignalId(), rdCxt);
//			controller.getControl().remove(control);
//			RoadNetworkUtils.refresh(rdCxt);
//			rdCxt.getMvCxt().updatePanels();
//		}else
//		if(srcCb==cbId){
//			if(((String)cbId.getSelectedItem()).equals("") || ((String)cbId.getSelectedItem()).equals("None") || ((String)cbId.getSelectedItem()).equals(control.getSignalId()) )
//				return;
//			control.setSignalId((String)cbId.getSelectedItem());
//		}else 
		if(srcCb==move){
			if(((String)move.getSelectedItem()).equals("") || ((String)move.getSelectedItem()).equals("This"))
				return;
			String slct=(String)move.getSelectedItem();
			ControllerGroup curCg=TrafficLightsPanel.getControllerGroup(controller, rdCxt.getMvCxt());
			controller.getControl().remove(control);
			removeState(curCg, control.getSignalId(), rdCxt.getMvCxt());
			if(controller.getControl().size()<1){
				rdCxt.getRn().getOdrNetwork().getController().remove(controller);
				rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup().remove(curCg);
			}
			switch(slct){
			case "New controller":
				Controller c=new Controller();
				ControllerGroup cg=new ControllerGroup();
				String id=ControllerPanelListener.getUniqueId(rdCxt,"plan");
				c.setId(id);
				cg.setId(id);
				Phase p=new Phase();
				p.setDuration(30);
				cg.getPhase().add(p);
				rdCxt.getRn().getOdrNetwork().getController().add(c);
				c.getControl().add(control);
				TrafficLightState st=new TrafficLightState();
				st.setCondition(TrafficLightCondition.NONE);
				st.setStatus(TrafficLightStatus.GREEN);
				st.setName(control.getSignalId());
				p.getTrafficLightState().add(st);
				if(!rdCxt.getMvCxt().getMovsim().getScenario().isSetTrafficLights())
					rdCxt.getMvCxt().getMovsim().getScenario().setTrafficLights(new TrafficLights());
				rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup().add(cg);
				RoadNetworkUtils.syncTrafficPanels(rdCxt.getMvCxt());
				break;
			default:
				Controller c2=TrafficLightsPanel.getController(slct, rdCxt);
				ControllerGroup cg2=TrafficLightsPanel.getControllerGroup(c2, rdCxt.getMvCxt());
				TrafficLightState st2=new TrafficLightState();
				st2.setCondition(TrafficLightCondition.NONE);
				st2.setStatus(TrafficLightStatus.GREEN);
				st2.setName(control.getSignalId());
				cg2.getPhase().get(0).getTrafficLightState().add(st2);
				c2.getControl().add(control);
				RoadNetworkUtils.syncTrafficPanels(rdCxt.getMvCxt());
			}
		}
	}
	/**
	 * Removes {@link TrafficLightState}(s) from related phase(s)
	 * @param cg {@link ControllerGroup}
	 * @param id id of the signal
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
	public static void removeState(ControllerGroup cg,String id,MovsimConfigContext mvCxt){
		if(cg.isSetPhase()){
			HashSet<Phase>phases=new HashSet<>();
			for(Phase p:cg.getPhase()){
				if(p.isSetTrafficLightState()){
					HashSet<TrafficLightState>sts=new HashSet<>();
					for(TrafficLightState st:p.getTrafficLightState()){
						if(st.getName().equals(id)){
							sts.add(st);
						}
					}
					p.getTrafficLightState().removeAll(sts);
					if(p.getTrafficLightState().size()<1)phases.add(p);
				}
			}
			cg.getPhase().removeAll(phases);
			if(cg.getPhase().size()<1)mvCxt.getMovsim().getScenario().getTrafficLights().getControllerGroup().remove(cg);
		}else{
			mvCxt.getMovsim().getScenario().getTrafficLights().getControllerGroup().remove(cg);
		}
	}
//	public void setRemove(JButton remove) {
//		this.remove = remove;
//	}
//
//	public void setIdCb(JComboBox<String>cbId) {
//		this.cbId=cbId;
//	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

}
