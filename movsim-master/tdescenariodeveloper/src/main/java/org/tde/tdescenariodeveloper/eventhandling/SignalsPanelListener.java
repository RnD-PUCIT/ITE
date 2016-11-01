package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;

import org.movsim.autogen.ControllerGroup;
import org.movsim.autogen.Phase;
import org.movsim.autogen.TrafficComposition;
import org.movsim.autogen.TrafficLightCondition;
import org.movsim.autogen.TrafficLightState;
import org.movsim.autogen.TrafficLightStatus;
import org.movsim.autogen.TrafficLights;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.movsim.simulator.trafficlights.TrafficLight;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.ui.TrafficLightsPanel;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Class used to add new signal to the selected {@link Road}
 * @author Shmeel
 * @see Road
 * @see SignalListener
 * @see TrafficLightsPanel
 * @see TrafficLights
 * @see TrafficLightState
 * @see TrafficLightCondition
 * @see TrafficLightStatus
 * @see TrafficLight
 * @see TrafficComposition
 * @see Signal
 * @see Signals
 */
public class SignalsPanelListener implements ActionListener,Blockable {
	boolean blocked=true;
	RoadContext rdCxt;
	/**
	 * 
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 */
	public SignalsPanelListener(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked || rdCxt.getSelectedRoad()==null){
			if(rdCxt.getSelectedRoad()==null)GraphicsHelper.showToast("Select road first", rdCxt.getToastDurationMilis());
			return;
		}
		JButton src=null;
		if(e.getSource() instanceof JButton)
			src=(JButton)e.getSource();
		if(src==rdCxt.getSignalsPanel().getAddNew()){
			if(!rdCxt.getSelectedRoad().getOdrRoad().isSetSignals()){
				rdCxt.getSelectedRoad().getOdrRoad().setSignals(new Signals());
			}
			Signal s=new Signal();
			s.setId(ControllerPanelListener.getUniqueId(rdCxt, "Signal"));
			s.setName(s.getId());
			s.setS(rdCxt.getSelectedRoad().getOdrRoad().getSignals().getSignal().size()*40+rdCxt.getSelectedRoad().roadLength()/4);
			rdCxt.getSelectedRoad().getOdrRoad().getSignals().getSignal().add(s);
			Controller c=RoadNetworkUtils.getFirstController(rdCxt);
			ControllerGroup cg=TrafficLightsPanel.getControllerGroup(c, rdCxt.getMvCxt());
			if(c==null){
				c=new Controller();
				cg=new ControllerGroup();
				Phase p=new Phase();
				p.setDuration(30);
				cg.getPhase().add(p);
				String id=ControllerPanelListener.getUniqueId(rdCxt, "Plan");
				c.setId(id);
				cg.setId(id);
				if(!rdCxt.getMvCxt().getMovsim().getScenario().isSetTrafficLights())
					rdCxt.getMvCxt().getMovsim().getScenario().setTrafficLights(new TrafficLights());
				rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup().add(cg);
				rdCxt.getRn().getOdrNetwork().getController().add(c);
			}
			TrafficLightState st=new TrafficLightState();
			st.setName(s.getId());
			st.setCondition(TrafficLightCondition.NONE);
			st.setStatus(TrafficLightStatus.GREEN);
			cg.getPhase().get(0).getTrafficLightState().add(st);
			Control cn=new Control();
			cn.setSignalId(s.getId());
			c.getControl().add(cn);
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.getMvCxt().updatePanels();
		}
	}
	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}
	/**
	 * used to get not used id for {@link Signal}
	 * @param signals {@link Signals} tag containing signals
	 * @return returns not used id for {@link Signal}
	 */
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
