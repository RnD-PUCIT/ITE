package org.tde.tdescenariodeveloper.validation;

import org.tde.tdescenariodeveloper.exception.InvalidInputException;
import org.tde.tdescenariodeveloper.ui.RoadContext;
/**
 * Class used to validate data to be updated to Lanes
 * @author Shmeel
 *
 */
public class LanesValidator {
	RoadContext rdCxt;
	/**
	 * 
	 * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public LanesValidator(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}
	/**
	 * checks if it is valid to remove a lane
	 * @return
	 */
	public boolean isValidRemove(){
		return rdCxt.getLanesPnl().getOdrLanes().size()>1;
	}
	/**
	 * checks if lane width entered is valid
	 * @return
	 */
	public boolean isValidWidth(){
		double d=Double.parseDouble(rdCxt.getLanesPnl().getTfWidth().getText());
		if(d>0 && d<=100){
			return true;
		}
		else throw new InvalidInputException("Lane width should be within range of 1 to 100");
	}
	/**
	 * checks if max speed entered is valid
	 * @return
	 */
	public boolean isValidMaxSpeed() {
		Double.parseDouble(rdCxt.getLanesPnl().getMaxSpeed().getText());//only checks if it is valid to be parsed if not throws exception
		return true;
	}
}
