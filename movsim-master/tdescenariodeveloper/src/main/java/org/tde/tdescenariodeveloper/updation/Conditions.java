package org.tde.tdescenariodeveloper.updation;

import org.movsim.autogen.Route;
import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;

public class Conditions {
	public static boolean existsLabelInVPC(String lbl,MovsimConfigContext mvCxt) {
		for(VehiclePrototypeConfiguration v:mvCxt.getMovsim().getVehiclePrototypes().getVehiclePrototypeConfiguration())
			if(v.getLabel().equals(lbl))return true;
		return false;
	}
	public static boolean existsLabelInRoutes(String lbl,MovsimConfigContext mvCxt) {
		for(Route v:mvCxt.getMovsim().getScenario().getRoutes().getRoute())
			if(v.getLabel().equals(lbl))return true;
		return false;
	}
	public static boolean existsIdInRoads(String id,MovsimConfigContext mvCxt) {
		for(Road r:mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad())
			if(r.getId().equals(id))return true;
		return false;
	}
	public static boolean existsIdInRoadsCustomizations(String id,MovsimConfigContext mvCxt) {
		for(org.movsim.autogen.Road r:mvCxt.getMovsim().getScenario().getSimulation().getRoad())
			if(r.getId().equals(id))return true;
		return false;
	}
}
