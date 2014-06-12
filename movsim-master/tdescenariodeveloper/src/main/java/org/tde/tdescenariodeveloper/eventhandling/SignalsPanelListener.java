package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;

import org.movsim.autogen.ControllerGroup;
import org.movsim.autogen.Phase;
import org.movsim.autogen.TrafficLightCondition;
import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLightStatus;
import org.movsim.autogen.TrafficLights;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class SignalsPanelListener implements ActionListener,Blockable {
	boolean blocked=true;
	RoadContext rdCxt;
	public SignalsPanelListener(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked || rdCxt.getSelectedRoad()==null)return;
		JButton src=null;
		if(e.getSource() instanceof JButton)src=(JButton)e.getSource();
		if(src==rdCxt.getSignalsPanel().getAddNew()){
			if(!rdCxt.getSelectedRoad().getOdrRoad().isSetSignals()){
				rdCxt.getSelectedRoad().getOdrRoad().setSignals(new Signals());
			}
			Signal s=new Signal();
			s.setId(ControllerPanelListener.getUniqueId(rdCxt, "Signal"));
			s.setName(s.getId());
			s.setS(rdCxt.getSelectedRoad().roadLength()/4);
			rdCxt.getSelectedRoad().getOdrRoad().getSignals().getSignal().add(s);
			Controller c=RoadNetworkUtils.getFirstController(rdCxt);
//			ControllerGroup cg=TrafficLightsPanel.getControllerGroup(c, rdCxt.getMvCxt());
			if(c==null){
				c=new Controller();
				ControllerGroup cg=new ControllerGroup();
				Phase p=new Phase();
				p.setDuration(30);
				TrafficLightState st=new TrafficLightState();
				st.setName(s.getId());
				st.setCondition(TrafficLightCondition.NONE);
				st.setStatus(TrafficLightStatus.GREEN);
				p.getTrafficLightState().add(st);
				cg.getPhase().add(p);
				String id=ControllerPanelListener.getUniqueId(rdCxt, "Plan");
				c.setId(id);
				cg.setId(id);
				if(!rdCxt.getMvCxt().getMovsim().getScenario().isSetTrafficLights())
					rdCxt.getMvCxt().getMovsim().getScenario().setTrafficLights(new TrafficLights());
				rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup().add(cg);
			}
			Control cn=new Control();
			cn.setSignalId(s.getId());
			c.getControl().add(cn);
			rdCxt.getRn().getOdrNetwork().getController().add(c);
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.getMvCxt().updatePanels();
			rdCxt.updatePanel();
		}
	}
	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}
	public static String getNextId(Signals signals){
		int id=0;
		if(signals.getSignal().size()>0){
			Signal max=Collections.max(signals.getSignal(), new Comparator<Signal>() {

				@Override
				public int compare(Signal o1, Signal o2) {
					return Integer.parseInt(o1.getId())-Integer.parseInt(o2.getId());
				}
			});
			id=Integer.parseInt(max.getId())+1;
		}
		return id+"";
	}
}
