package org.tde.tdescenariodeveloper.validation;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.JunctionsUpdater;
/**
 * validates data to be updated to a {@link Junction}
 * @author Shmeel
 *
 */
public class JunctionsValidator {
	RoadContext rdCxt;
	/**
	 * 
	 * @param r {@link RoadContext} contains reference to loaded .xodr file and other panels added to it
	 */
	public JunctionsValidator(RoadContext r) {
		rdCxt=r;
	}
	/**
	 * checks if a given junction exists
	 * @param jn
	 * @return
	 */
	public boolean exists(String jn){
		rdCxt.getAppFrame().getJp();
		return JunctionsUpdater.getJunction(jn, rdCxt)!=null;
	}
	/**
	 * checks if a gieven road exists
	 * @param rd
	 * @return
	 */
	public boolean existsRoad(String rd){
		return rdCxt.getRn().findByUserId(rd)!=null;
	}
}
