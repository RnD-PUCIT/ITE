package org.tde.tdescenariodeveloper.validation;

import org.tde.tdescenariodeveloper.ui.RoadContext;

public class JunctionsValidator {
	RoadContext rdCxt;
	public JunctionsValidator(RoadContext r) {
		rdCxt=r;
	}
	public boolean exists(String jn){
		return rdCxt.getAppFrame().getJp().getJunction(jn)!=null;
	}
	public boolean existsRoad(String rd){
		return rdCxt.getRn().findByUserId(rd)!=null;
	}
}
