package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;

import org.movsim.autogen.Movsim;
import org.tde.tdescenariodeveloper.eventhandling.Blockable;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class MovsimConfigContext extends JTabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3144674339973903226L;
	VehiclePrototypesPanel prototypes;
	SimulationPanel simulation;
	TrafficLightsPanel trafficLights;
	RoutesPanel routes;
	OutputPanel output;
	public OutputPanel getOutput() {
		return output;
	}
	Movsim movsim;
	private RoadContext rdCxt;
	private boolean updateCanvas=false;
	public MovsimConfigContext(Movsim m, RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		this.movsim=m;
		prototypes=new VehiclePrototypesPanel(this);
		simulation=new SimulationPanel(this);
		trafficLights=new TrafficLightsPanel(this);
		routes=new RoutesPanel(this);
		output=new OutputPanel(this);
		setPreferredSize(new Dimension(1024,200));
//		MovsimConfigContextMouseListener ml=new MovsimConfigContextMouseListener(this);
//		addMouseListener(ml);
//		addMouseMotionListener(ml);
		
		addTab("Simulation configuration",TDEResources.getResources().getSimulation(),simulation,"Edit parameters for simulation");
		addTab("Vehicle prototypes",TDEResources.getResources().getPrototypes(),new JScrollPane(prototypes),"Decide which type of traffic to run on roads");
		addTab("Traffic signals",TDEResources.getResources().getTraffic(),trafficLights,"Control traffic with signals");
		addTab("Routes configuration",TDEResources.getResources().getRoutes(),new JScrollPane(routes),"Tell simulator what fraction of vehicles should follow which route");
		addTab("Output configuration",TDEResources.getResources().getOutput(),output,"Scenario statistics and result output for analysis");
		updatePanels();
	}
	public void updatePanels(){
		blockListeners(true);
		prototypes.updatePanel();
		simulation.updateSimPanel();
		routes.updateRoutesPanel();
		trafficLights.updateTrafficLightsPanel();
		output.updateOutputPanels();
		blockListeners(false);
		revalidate();
		repaint();
		if(updateCanvas){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					rdCxt.updateGraphics();
				}
			});
		}
	}
	private void blockListeners(boolean b) {
		Stack<Component>stack=new Stack<>();
		stack.push(this);
		while(!stack.isEmpty()){
			Component c=stack.pop();
			blockListenersOf(c, b);
			if(c instanceof Container){
				Container cnt=(Container)c;
				for(Component cc:cnt.getComponents()){
					stack.push(cc);
				}
			}
		}
	}
	private void blockListenersOf(Component c,boolean b){
		ActionListener listeners[]=(ActionListener[])c.getListeners(ActionListener.class);
		for(ActionListener blockable:listeners){
			if(blockable instanceof Blockable)((Blockable)blockable).setBlocked(b);
		}
		ItemListener listeners2[]=(ItemListener[])c.getListeners(ItemListener.class);
		for(ItemListener blockable:listeners2){
			if(blockable instanceof Blockable)((Blockable)blockable).setBlocked(b);
		}
		DocumentListener listeners3[]=(DocumentListener[])c.getListeners(DocumentListener.class);
		for(DocumentListener blockable:listeners3){
			if(blockable instanceof Blockable)((Blockable)blockable).setBlocked(b);
		}
	}
	public Movsim getMovsim() {
		return movsim;
	}
	public void setMovsim(Movsim movsim) {
		this.movsim = movsim;
	}
	public VehiclePrototypesPanel getPrototypes() {
		return prototypes;
	}
	public SimulationPanel getSimulation() {
		return simulation;
	}
	public TrafficLightsPanel getTrafficLights() {
		return trafficLights;
	}
	public RoutesPanel getRoutes() {
		return routes;
	}
	public RoadContext getRdCxt() {
		return rdCxt;
	}
	public void setUpdateCanvas(boolean updateCanvas) {
		this.updateCanvas = updateCanvas;
	}
}
