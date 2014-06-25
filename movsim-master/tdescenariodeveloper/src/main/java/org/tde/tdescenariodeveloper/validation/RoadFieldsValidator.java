package org.tde.tdescenariodeveloper.validation;

import java.util.ArrayList;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection.LaneLink;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.tde.tdescenariodeveloper.exception.NotFoundException;
import org.tde.tdescenariodeveloper.ui.LaneLinkPanel;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * class is used to validate data to be updated to a {@link Road}
 * @author Shmeel
 *
 */
public class RoadFieldsValidator {
	RoadContext rdCxt;
	/**
	 * 
	 * @param rdPrpPnl contains reference to loaded .xodr file and other panels added to it
	 */
	public RoadFieldsValidator(RoadContext rdPrpPnl) {
		this.rdCxt=rdPrpPnl;
	}
	/**
	 * validates if jucntion id of road is valid
	 * @return
	 */
	public boolean[] isJunctionValid(){
		boolean isValid[]=new boolean[]{false,false};
		ArrayList<String>jncs=new ArrayList<>();
		for(Junction jn:rdCxt.getRdFldPnl().getRn().getOdrNetwork().getJunction())
			jncs.add(jn.getId()+"");
		String jnc=(String)rdCxt.getRdFldPnl().getCbJunction().getSelectedItem();
		isValid[0]=jncs.contains(jnc);
		if(!isValid[0]) return isValid;
		ArrayList<String>rds=new ArrayList<>();
		try {
			Junction jn=getJunctionByid(jnc);
			for(Connection cn:jn.getConnection()){
				LaneLinkPanel.putOrReject(rds, cn.getConnectingRoad());
				LaneLinkPanel.putOrReject(rds, cn.getIncomingRoad());
			}
			isValid[1]=rds.contains(rdCxt.getSelectedRoad().userId());
			
		} catch (NotFoundException e) {
			GraphicsHelper.showToast("Junction with id "+jnc+" not found", rdCxt.getToastDurationMilis());
		}
		return isValid;
	}
	/**
	 * explained by name
	 * @param id
	 * @return
	 * @throws NotFoundException
	 */
	public Junction getJunctionByid(String id) throws NotFoundException{
		for(Junction j:rdCxt.getRdFldPnl().getRn().getOdrNetwork().getJunction()){
			if(j.getId().equals(id))
				return j;
		}
		throw new NotFoundException("Junction not found");
	}
}
