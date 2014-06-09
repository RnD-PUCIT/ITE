package org.tde.tdescenariodeveloper.validation;

import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.JunctionsUpdater;

public class JunctionsValidator {
	RoadContext rdCxt;
	public JunctionsValidator(RoadContext r) {
		rdCxt=r;
	}
	public boolean exists(String jn){
		rdCxt.getAppFrame().getJp();
		return JunctionsUpdater.getJunction(jn, rdCxt)!=null;
	}
	public boolean existsRoad(String rd){
		return rdCxt.getRn().findByUserId(rd)!=null;
	}
}
