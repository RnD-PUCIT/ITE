package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.tde.tdescenariodeveloper.eventhandling.SignalListener;
import org.tde.tdescenariodeveloper.eventhandling.SignalsPanelListener;

public class SignalsPanel extends JPanel {
	RoadContext rdCxt;
	JPanel sigPnl;
	JButton addNew;
	SignalsPanelListener spl;
	public SignalsPanel(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		sigPnl=new JPanel(new GridBagLayout());
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
	private static JPanel signalToPanel(Signal s, List<Signal> signals,RoadContext rdCxt) {
		JPanel main=new JPanel(new GridBagLayout());
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
	public static void adjustControllersAfterSignalRemove(RoadContext rdCxt,
			String old) {
		for(Controller clr:rdCxt.getRn().getOdrNetwork().getController()){
			if(clr.isSetControl()){
				for(Control c:clr.getControl()){
					if(old.equals(c.getSignalId())){
						clr.getControl().remove(c);
					}
				}
			}
		}
		
	}
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
	
}
