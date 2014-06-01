package org.tde.tdescenariodeveloper.ui;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.movsim.autogen.Movsim;

public class MovsimConfigContext extends JTabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3144674339973903226L;
	VehiclePrototypesPanel prototypes;
	SimulationPanel simulation;
	TrafficLightsPanel trafficLights;
	RoutesPanel routes;
	Movsim movsim;
	ImageIcon sim,prt,trf,rts;
	private RoadContext rdCxt;
	public MovsimConfigContext(Movsim m, RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		this.movsim=m;
		prototypes=new VehiclePrototypesPanel(this);
		simulation=new SimulationPanel(this);
		trafficLights=new TrafficLightsPanel(this);
		routes=new RoutesPanel(this);
		setPreferredSize(new Dimension(1024,200));
//		MovsimConfigContextMouseListener ml=new MovsimConfigContextMouseListener(this);
//		addMouseListener(ml);
//		addMouseMotionListener(ml);
		sim=new ImageIcon(getClass().getClassLoader().getResource("simulation_icon.png"));
		sim.setImage(sim.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		prt=new ImageIcon(getClass().getClassLoader().getResource("car_icon.png"));
		prt.setImage(prt.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		trf=new ImageIcon(getClass().getClassLoader().getResource("trafficlight_icon.png"));
		trf.setImage(trf.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		rts=new ImageIcon(getClass().getClassLoader().getResource("routes_icon.png"));
		rts.setImage(rts.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		addTab("Simulation configuration",sim,simulation,"Edit parameters for simulation");
		addTab("Vehicle prototypes",prt,new JScrollPane(prototypes),"Decide which type of traffic to run on roads");
		addTab("Traffic signals",trf,new JScrollPane(trafficLights),"Control traffic with signals");
		addTab("Routes configuration",rts,new JScrollPane(routes),"Tell simulator what fraction of vehicles should follow which route");
		updatePanels();
	}
	public void updatePanels(){
		prototypes.updatePanel();
		simulation.updateSimPanel();
		revalidate();
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
