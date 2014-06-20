package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.autogen.ControllerGroup;
import org.movsim.autogen.Phase;
import org.movsim.autogen.TrafficLightState;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.movsim.simulator.trafficlights.TrafficLight;
import org.tde.tdescenariodeveloper.eventhandling.SignalListener;
import org.tde.tdescenariodeveloper.eventhandling.SignalsPanelListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Show the signals of selected road found in {@link RoadContext} Tab
 * @author Shmeel
 * @see RoadContext
 * @see Road
 * @see Signal
 * @see Signals
 * @see TrafficLightsPanel
 * @see TrafficLightState
 * @see TrafficLight
 * @see Controller
 * @see Control
 */
public class SignalsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RoadContext rdCxt;
	JPanel sigPnl;
	JButton addNew;
	SignalsPanelListener spl;
	/**
	 * 
	 * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public SignalsPanel(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		sigPnl=new JPanel(new GridBagLayout());
		sigPnl.setOpaque(false);
		addNew=new JButton("New signal",TDEResources.getResources().getAddIcon());
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Signals", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=1;
		
		spl=new SignalsPanelListener(rdCxt);
		addNew.addActionListener(spl);
		add(sigPnl,c);
		c.weighty=0;
		add(addNew,c);
		spl.setBlocked(false);
	}
	public JButton getAddNew() {
		return addNew;
	}
	/**
	 * updates the this {@link SignalsPanel}
	 */
	public void updateSignalPanel(){
		sigPnl.removeAll();
		if(rdCxt.getSelectedRoad()!=null && rdCxt.getSelectedRoad().getOdrRoad().getSignals()!=null)fillSignalPanel(rdCxt.getSelectedRoad().getOdrRoad().getSignals().getSignal(),sigPnl, rdCxt);
	}
	private static void fillSignalPanel(List<Signal>signals, JPanel sigPnl,RoadContext rdCxt) {
		sigPnl.removeAll();
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		for(Signal s:signals){
			sigPnl.add(signalToPanel(s,signals, rdCxt),c);
		}
	}
	/**
	 * Converts {@link Signal} to {@link JPanel}
	 * @param s {@link Signal} to be converted
	 * @param signals {@link List} of {@link Signal}s
	 * @param rdCxt contains reference to loaded .xprj file and other panels added to it
	 * @return
	 */
	public static JPanel signalToPanel(Signal s, List<Signal> signals,RoadContext rdCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		main.setOpaque(false);
		main.setBorder(new LineBorder(TDEResources.getResources().SIGNALS_BORDER_COLOR, 1, true));
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 5, 2, 5);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		
		JButton remove=new JButton("Remove this signal",TDEResources.getResources().getRem());
		SignalListener sl=new SignalListener(s,signals,rdCxt);
		remove.addActionListener(sl);
		sl.setRemove(remove);
		main.add(remove,gbc);
		
		
		gbc.gridwidth=1;
		main.add(new JLabel("Name"),gbc);
		JTextField id=new JTextField(10);
		id.getDocument().addDocumentListener(sl);
		sl.setId(id);
		id.setText(s.isSetId()?s.getId():"");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(id,gbc);

		gbc.gridwidth=1;
		main.add(new JLabel("Position"),gbc);
		JSlider slider=new JSlider();
		slider.addChangeListener(sl);
		sl.setSlider(slider);
		slider.setMinimum(0);
		slider.setMaximum((int)rdCxt.getSelectedRoad().roadLength());
		slider.setValue(s.isSetS()?(int)s.getS():0);
		slider.setPreferredSize(new Dimension(100,30));
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(slider,gbc);
		
		sl.setBlocked(false);
		return main;
	}
	public SignalsPanelListener getListener() {
		return spl;
	}
	public void reset() {
		sigPnl.removeAll();
	}
/**
 * used to rename related {@link Control}s found in {@link Controller} after a {@link Signal} is renamed 
 * @param rdCxt contains reference to loaded .xprj file and other panels added to it
 * @param old old id of the {@link Signal} which is modified 
 * @param id new id of the signal which is to be updated
 */
	public static void adjustControllers(RoadContext rdCxt, String old,
			String id) {
		for(Controller clr:rdCxt.getRn().getOdrNetwork().getController()){
			if(clr.isSetControl()){
				for(Control c:clr.getControl()){
					if(old.equals(c.getSignalId())){
						c.setSignalId(id);
					}
				}
			}
		}
	}
	/**
	 * removes related {@link Control} from {@link Controller}s and related {@link ControllerGroup}s
	 * @param rdCxt contains reference to loaded .xprj file and other panels added to it
	 * @param old id of the removed {@link Signal}
	 */
	public static void adjustControllersAfterSignalRemove(RoadContext rdCxt,
			String old) {
		ArrayList<Controller>ctrls=new ArrayList<>();
		for(Controller clr:rdCxt.getRn().getOdrNetwork().getController()){
			if(clr.isSetControl()){
				ArrayList<Control>ctrl=new ArrayList<>();
				for(Control c:clr.getControl()){
					if(old.equals(c.getSignalId())){
						ctrl.add(c);
					}
				}
				clr.getControl().removeAll(ctrl);
				if(clr.getControl().size()<1)ctrls.add(clr);
			}
		}
		rdCxt.getRn().getOdrNetwork().getController().removeAll(ctrls);
		HashSet<ControllerGroup>cgs=new HashSet<>();
		for(ControllerGroup clr:rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup()){
			if(clr.isSetPhase()){
				ArrayList<Phase>phses=new ArrayList<>();
				for(Phase c:clr.getPhase()){
					if(c.isSetTrafficLightState()){
						ArrayList<TrafficLightState>sts=new ArrayList<>();
						for(TrafficLightState tls:c.getTrafficLightState())
							if(tls.getName().equals(old))sts.add(tls);
						c.getTrafficLightState().removeAll(sts);
						if(c.getTrafficLightState().size()<1)phses.add(c);
					}
				}
				clr.getPhase().removeAll(phses);
				if(clr.getPhase().size()<1)cgs.add(clr);
			}
		}
		rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup().removeAll(cgs);
		if(rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup().size()<1)rdCxt.getMvCxt().getMovsim().getScenario().setTrafficLights(null);
	}
	/**
	 * used to rename related {@link ControllerGroup} when an {@link Controller} if renamed
	 * @param rdCxt contains reference to loaded .xprj file and other panels added to it
	 * @param old id of the controller to be modified
	 * @param id new id to be set
	 */
	public static void adjustControllersGroups(RoadContext rdCxt, String old,
			String id) {
		if(!rdCxt.getMvCxt().getMovsim().getScenario().isSetTrafficLights())return;
		for(ControllerGroup clr:rdCxt.getMvCxt().getMovsim().getScenario().getTrafficLights().getControllerGroup()){
			for(Phase p:clr.getPhase()){
				for(TrafficLightState st:p.getTrafficLightState()){
					if(st.getName().equals(old))st.setName(id);
				}
			}
		}		
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
