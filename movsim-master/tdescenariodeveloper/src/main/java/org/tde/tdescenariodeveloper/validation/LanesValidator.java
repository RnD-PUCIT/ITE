package org.tde.tdescenariodeveloper.validation;

import org.tde.tdescenariodeveloper.ui.RoadContext;

public class LanesValidator {
	RoadContext rdCxt;
	public LanesValidator(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}
	public boolean isValidRemove(){
		return rdCxt.getLanesPnl().getOdrLanes().size()>1;
	}
}
