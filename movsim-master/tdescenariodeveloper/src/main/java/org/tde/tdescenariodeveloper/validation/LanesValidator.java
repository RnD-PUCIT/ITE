package org.tde.tdescenariodeveloper.validation;

import org.tde.tdescenariodeveloper.exception.InvalidInputException;
import org.tde.tdescenariodeveloper.ui.RoadContext;

public class LanesValidator {
	RoadContext rdCxt;
	public LanesValidator(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}
	public boolean isValidRemove(){
		return rdCxt.getLanesPnl().getOdrLanes().size()>1;
	}
	public boolean isValidWidth(){
		double d=Double.parseDouble(rdCxt.getLanesPnl().getTfWidth().getText());
		if(d>0 && d<=100){
			return true;
		}
		else throw new InvalidInputException("Lane width should be within range of 1 to 100");
	}
	public boolean isValidMaxSpeed() {
		Double.parseDouble(rdCxt.getLanesPnl().getMaxSpeed().getText());//only checks if it is valid to be parsed if not throws exception
		return true;
	}
}
