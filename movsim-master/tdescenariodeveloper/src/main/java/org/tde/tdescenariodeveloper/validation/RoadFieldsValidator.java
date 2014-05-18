package org.tde.tdescenariodeveloper.validation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;
import org.tde.tdescenariodevelopment.utils.GraphicsHelper;

public class RoadFieldsValidator {
	RoadPropertiesPanel rdPrPnl;
	public RoadFieldsValidator(RoadPropertiesPanel rdPrpPnl) {
		this.rdPrPnl=rdPrpPnl;
	}
	public boolean isJunctionValid(){
		ArrayList<String>jncs=new ArrayList<>();
		for(Junction jn:rdPrPnl.getRdFldPnl().getRn().getOdrNetwork().getJunction())
			jncs.add(jn.getId()+"");
		return jncs.contains((String)rdPrPnl.getRdFldPnl().getCbJunction().getSelectedItem());
	}
}
