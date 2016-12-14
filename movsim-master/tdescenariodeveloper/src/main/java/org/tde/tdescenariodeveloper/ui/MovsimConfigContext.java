package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
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
import org.movsim.autogen.OutputConfiguration;
import org.movsim.autogen.Routes;
import org.movsim.autogen.Simulation;
import org.movsim.autogen.TrafficLights;
import org.movsim.autogen.VehiclePrototypes;
import org.tde.tdescenariodeveloper.eventhandling.Blockable;
import org.tde.tdescenariodeveloper.eventhandling.Shortcuts;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Class used to hold all the input of {@link Movsim} .xprj file and also other panels related to this input
 * @author Shmeel
 * @see Simulation
 * @see TrafficLights
 * @see VehiclePrototypes
 * @see Routes
 * @see OutputConfiguration
 */
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
	/**
	 * 
	 * @param m input .xprj file
	 * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
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
		JScrollPane sp1,sp2;
		sp1=new JScrollPane(prototypes);
		sp2=new JScrollPane(routes);
		sp1.setOpaque(false);
		sp2.setOpaque(false);
		sp1.getViewport().setOpaque(false);
		sp2.getViewport().setOpaque(false);
		addTab("Simulation configuration",TDEResources.getResources().getSimulation(),simulation,"Edit parameters for simulation");
		addTab("Vehicle prototypes",TDEResources.getResources().getPrototypes(),sp1,"Decide which type of traffic to run on roads");
		addTab("Traffic signals",TDEResources.getResources().getTraffic(),trafficLights,"Control traffic with signals");
		addTab("Routes configuration",TDEResources.getResources().getRoutes(),sp2,"Tell simulator what fraction of vehicles should follow which route");
		addTab("Output configuration",TDEResources.getResources().getOutput(),output,"Scenario statistics and result output for analysis");
		updatePanels();
	}
	/**
	 * updates all the panels added to this {@link JTabbedPane}
	 */
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
		Shortcuts.setMvCxt(this);
		Shortcuts.saveAction();
	}
	
	/**
	 * updates all the panels added to this {@link JTabbedPane}  called only in Set Scenario and don't save the history
	 */
	public void updatePanels2()
	{
		blockListeners(true);
		prototypes.updatePanel();
		simulation.updateSimPanel();
		routes.updateRoutesPanel();
		trafficLights.updateTrafficLightsPanel();
		output.updateOutputPanels();
		blockListeners(false);
		revalidate();
		repaint();
		//Shortcuts.setMvCxt(this);
		//Shortcuts.saveAction();
	}
	/**
	 * Used to block/unblock all ({@DocumentListener}, {@link ActionListener}, {@link Blockable}, {@link ItemListener}) listeners of all the components
	 * Depth first search is used to traverse all the components and then related listeners are blocked or unblocked 
	 * @param b if true listners are blocked, unblocked otherwise
	 */
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
}
