package org.tde.tdescenariodeveloper.utils;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.movsim.autogen.AccelerationModelType;
import org.movsim.autogen.Inflow;
import org.movsim.autogen.LaneChangeModelType;
import org.movsim.autogen.ModelParameterACC;
import org.movsim.autogen.ModelParameterGipps;
import org.movsim.autogen.ModelParameterIDM;
import org.movsim.autogen.ModelParameterKrauss;
import org.movsim.autogen.ModelParameterMOBIL;
import org.movsim.autogen.ModelParameterNSM;
import org.movsim.autogen.ModelParameterNewell;
import org.movsim.autogen.Movsim;
import org.movsim.autogen.Road;
import org.movsim.autogen.Scenario;
import org.movsim.autogen.Simulation;
import org.movsim.autogen.TrafficComposition;
import org.movsim.autogen.TrafficSource;
import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.autogen.VehiclePrototypes;
import org.movsim.autogen.VehicleType;
import org.movsim.input.network.OpenDriveReader;
import org.movsim.simulator.trafficlights.TrafficLights;
import org.movsim.xml.MovsimInputLoader;
import org.tde.tdescenariodeveloper.eventhandling.Shortcuts;
import org.tde.tdescenariodeveloper.jaxbhandler.Marshalling;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.xml.sax.SAXException;

public class MovsimScenario {
	/**
	 * loads and sets scenario from given file
	 * @param f .xprj {@link File} to be loaded
	 * @param mvCxt contains reference to loaded .xprj file and other panels added to it
	 */
	public static void setScenario(File f,MovsimConfigContext mvCxt){
		try {
			mvCxt.setMovsim(MovsimInputLoader.getInputData(f));
			mvCxt.getRdCxt().setSelectedRoadNull();
			mvCxt.getRdCxt().getRn().reset();
			OpenDriveReader.loadRoadNetwork(mvCxt.getRdCxt().getRn(),f.getAbsolutePath().replace( f.getName() , mvCxt.getMovsim().getScenario().getNetworkFilename()));
			RoadNetworkUtils.SetupLights(mvCxt);
			mvCxt.getRdCxt().getAppFrame().getJl().setBlocked(true);
			if(mvCxt.getRdCxt().getRn().getOdrNetwork().getJunction().size()>0)mvCxt.getRdCxt().getAppFrame().getJp().setSelectedJn(mvCxt.getRdCxt().getRn().getOdrNetwork().getJunction().get(0).getId());
			mvCxt.getRdCxt().getAppFrame().getJp().updateJunction();
			mvCxt.getRdCxt().getAppFrame().getJl().setBlocked(false);
			mvCxt.updatePanels();
			mvCxt.getRdCxt().updateGraphics();
			mvCxt.getRdCxt().getAppFrame().revalidate();
			mvCxt.getRdCxt().getAppFrame().repaint();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * loads and sets scenario from History file
	 * @param f .xprj {@link File} to be loaded
	 * @param mvCxt contains reference to loaded .xprj file and other panels added to it
	 */
	public static void setScenario2(File f,MovsimConfigContext mvCxt){
		try {
			mvCxt.setMovsim(MovsimInputLoader.getInputData(f));
			mvCxt.getRdCxt().setSelectedRoadNull();
			mvCxt.getRdCxt().getRn().reset();
			OpenDriveReader.loadRoadNetwork(mvCxt.getRdCxt().getRn(),f.getAbsolutePath().replace( f.getName() , mvCxt.getMovsim().getScenario().getNetworkFilename()));
			RoadNetworkUtils.SetupLights(mvCxt);
			mvCxt.getRdCxt().getAppFrame().getJl().setBlocked(true);
			if(mvCxt.getRdCxt().getRn().getOdrNetwork().getJunction().size()>0)mvCxt.getRdCxt().getAppFrame().getJp().setSelectedJn(mvCxt.getRdCxt().getRn().getOdrNetwork().getJunction().get(0).getId());
			mvCxt.getRdCxt().getAppFrame().getJp().updateJunction();
			mvCxt.getRdCxt().getAppFrame().getJl().setBlocked(false);
			mvCxt.updatePanels2();
			mvCxt.getRdCxt().updateGraphics();
			mvCxt.getRdCxt().getAppFrame().revalidate();
			mvCxt.getRdCxt().getAppFrame().repaint();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reloads default scenario
	 * @param mvCxt
	 */
	public static void resetScenario(MovsimConfigContext mvCxt){
		try {
			mvCxt.setMovsim(getMovsim());
			mvCxt.getRdCxt().setSelectedRoadNull();
			mvCxt.getRdCxt().getRn().reset();
			
			String path = System.getProperty("java.io.tmpdir") + "TDE_History.tmp//"  + "tmp.xodr" ;
		    ClassLoader classLoader = Shortcuts.class.getClassLoader();
		    File f = new File(path);
			Marshalling.writeToXml(RoadNetworkUtils.getNewOdr(), f);
			OpenDriveReader.loadRoadNetwork(mvCxt.getRdCxt().getRn(),f.getAbsolutePath());
			RoadNetworkUtils.SetupLights(mvCxt);
			mvCxt.getRdCxt().getAppFrame().getJl().setBlocked(true);
			mvCxt.getRdCxt().getAppFrame().getJp().updateJunction();
			mvCxt.getRdCxt().getAppFrame().getJl().setBlocked(false);
			mvCxt.updatePanels();
			mvCxt.getRdCxt().updateGraphics();
			mvCxt.getRdCxt().getAppFrame().revalidate();
			mvCxt.getRdCxt().getAppFrame().repaint();
			mvCxt.getRdCxt().getDrawingArea().paint(mvCxt.getRdCxt().getGraphics());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	/**
	 * saves scenario to given file
	 * @param f {@link File} to be stored on
	 * @param mvCxt contains reference to loaded .xprj file and other panels added to it
	 */
	public static void saveScenario(File f, MovsimConfigContext mvCxt) {
		RoadNetworkUtils.removeUncontrolledSignals(mvCxt);
		//RoadNetworkUtils.cleanJunctions(mvCxt);
		Marshalling.writeToXml(mvCxt.getMovsim(),f);
		Marshalling.writeToXml(mvCxt.getRdCxt().getRn().getOdrNetwork(), new File(f.getAbsolutePath().replace(".xprj", ".xodr")));
	}
	public static Movsim getMovsim(){
		Movsim m=new Movsim();
		m.setScenario(getScenario());
		m.setVehiclePrototypes(getVehiclePrototypes());
		return m;
	}
	public static Scenario getScenario() {
		Scenario s=new Scenario();
		s.setSimulation(getSimulation());
		return s;
	}
	public static Simulation getSimulation() {
		Simulation sm=new Simulation();
		sm.setCrashExit(false);
		sm.setTimestep(0.2);
		sm.setSeed(42);
		sm.setTrafficComposition(getTrafficComposition());
		sm.getRoad().add(getRoad());
		return sm;
	}
	public static Road getRoad() {
		Road r=new Road();
		r.setId("1");
		r.setTrafficSource(getTrafficSource());
		return r;
	}
	public static TrafficSource getTrafficSource() {
		TrafficSource ts=new TrafficSource();
		ts.getInflow().add(getInflow());
		return ts;
	}
	public static Inflow getInflow() {
		Inflow i=new Inflow();
		i.setQPerHour(1100.0);
		i.setV(24.0);
		i.setT(0);
		return i;
	}
	public static TrafficComposition getTrafficComposition() {
		TrafficComposition tc=new TrafficComposition();
		tc.getVehicleType().add(getVehicleType());
		return tc;
	}
	public static VehicleType getVehicleType() {
		VehicleType vt=new VehicleType();
		vt.setFraction(0.5);
		vt.setLabel("ACC");
		vt.setRelativeV0Randomization(0.2);
		return vt;
	}
	public static VehiclePrototypes getVehiclePrototypes(){
		VehiclePrototypes vp=new VehiclePrototypes();
		vp.getVehiclePrototypeConfiguration().add(getVehiclePrototypeConfiguration());
		return vp;
	}

	public static VehiclePrototypeConfiguration getVehiclePrototypeConfiguration() {
		VehiclePrototypeConfiguration vpc=new VehiclePrototypeConfiguration();
		vpc.setAccelerationModelType(getAccelerationModelType());
		vpc.setLabel("ACC");
		vpc.setLength(6);
		vpc.setMaximumDeceleration(9.0);
		return vpc;
	}

	public static AccelerationModelType getAccelerationModelType() {
		AccelerationModelType amt=new AccelerationModelType();
		amt.setModelParameterACC(getModelParameterACC());
		return amt;
	}
	public static ModelParameterACC getModelParameterACC() {
		ModelParameterACC mdl=new ModelParameterACC();
		mdl.setA(1.2);
		mdl.setB(2.0);
		mdl.setCoolness(1);
		mdl.setDelta(4);
		mdl.setS0(3);
		mdl.setS1(2);
		mdl.setV0(35);
		mdl.setT(1.2);
		return mdl;
	}
	public static ModelParameterIDM getModelParameterIDM() {
		ModelParameterIDM idm=new ModelParameterIDM();
		idm.setA(1.2);
		idm.setB(2.0);
		idm.setDelta(4);
		idm.setS0(3);
		idm.setS1(2);
		idm.setV0(35);
		idm.setT(1.2);
		return idm;
	}
	public static ModelParameterNSM getModelParameterNSM() {
		ModelParameterNSM nsm=new ModelParameterNSM();
		nsm.setS0(3);
		nsm.setV0(35);
		nsm.setPSlowdown(0.5);
		nsm.setPSlowStart(0.5);
		return nsm;
	}
	public static ModelParameterKrauss getModelParameterKrauss() {
		ModelParameterKrauss kr=new ModelParameterKrauss();
		kr.setA(1.2);
		kr.setB(2.0);
		kr.setS0(3);
		kr.setV0(35);
		kr.setEpsilon(0.5);
		return kr;
	}
	public static ModelParameterNewell getModelParameterNewell() {
		ModelParameterNewell nl=new ModelParameterNewell();
		nl.setS0(3);
		nl.setV0(35);
		return nl;
	}
	public static ModelParameterGipps getModelParameterGipps() {
		ModelParameterGipps gps=new ModelParameterGipps();
		gps.setA(1.2);
		gps.setB(2.0);
		gps.setS0(3);
		gps.setV0(35);
		return gps;
	}
	public static LaneChangeModelType getLaneChangeModelType(){
		LaneChangeModelType mt=new LaneChangeModelType();
		mt.setEuropeanRules(false);
		mt.setCritSpeedEur(20);
		mt.setModelParameterMOBIL(getModelParameterMOBIL());
		return mt;
	}
	public static ModelParameterMOBIL getModelParameterMOBIL() {
		ModelParameterMOBIL mb=new ModelParameterMOBIL();
		mb.setMinimumGap(2);
		mb.setSafeDeceleration(5);
		mb.setThresholdAcceleration(0.1);
		mb.setRightBiasAcceleration(0.05);
		mb.setPoliteness(0.1);
		return mb;
	}
	
}
