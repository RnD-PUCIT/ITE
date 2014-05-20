package org.tde.tdescenariodeveloper.validation;

import java.util.ArrayList;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection.LaneLink;
import org.tde.tdescenariodeveloper.exception.NotFoundException;
import org.tde.tdescenariodeveloper.ui.LaneLinkPanel;
import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class RoadFieldsValidator {
	RoadPropertiesPanel rdPrPnl;
	public RoadFieldsValidator(RoadPropertiesPanel rdPrpPnl) {
		this.rdPrPnl=rdPrpPnl;
	}
	public boolean[] isJunctionValid(){
		boolean isValid[]=new boolean[]{false,false};
		ArrayList<String>jncs=new ArrayList<>();
		for(Junction jn:rdPrPnl.getRdFldPnl().getRn().getOdrNetwork().getJunction())
			jncs.add(jn.getId()+"");
		String jnc=(String)rdPrPnl.getRdFldPnl().getCbJunction().getSelectedItem();
		isValid[0]=jncs.contains(jnc);
		if(!isValid[0]) return isValid;
		ArrayList<String>rds=new ArrayList<>();
		try {
			Junction jn=getJunctionByid(jnc);
			for(Connection cn:jn.getConnection()){
				LaneLinkPanel.putOrReject(rds, cn.getConnectingRoad());
				LaneLinkPanel.putOrReject(rds, cn.getIncomingRoad());
			}
			isValid[1]=rds.contains(rdPrPnl.getSelectedRoad().userId());
			
		} catch (NotFoundException e) {
			GraphicsHelper.showToast("Junction with id "+jnc+" not found", 3000);
		}
		return isValid;
	}
	public Junction getJunctionByid(String id) throws NotFoundException{
		for(Junction j:rdPrPnl.getRdFldPnl().getRn().getOdrNetwork().getJunction()){
			if(j.getId().equals(id))
				return j;
		}
		throw new NotFoundException("Junction not found");
	}
}
