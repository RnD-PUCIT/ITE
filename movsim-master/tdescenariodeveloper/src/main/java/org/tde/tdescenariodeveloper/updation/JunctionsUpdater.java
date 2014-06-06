package org.tde.tdescenariodeveloper.updation;

import java.util.Collections;
import java.util.Comparator;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.tde.tdescenariodeveloper.exception.InvalidInputException;
import org.tde.tdescenariodeveloper.ui.JunctionsPanel;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
import org.tde.tdescenariodeveloper.validation.JunctionsValidator;

public class JunctionsUpdater {
	RoadContext rdCxt;
	JunctionsValidator validator;
	public JunctionsUpdater(RoadContext rd) {
		this.rdCxt=rd;
		validator=new JunctionsValidator(rd);
	}
	public Junction addNewJunc() {
		int id=getNextId(rdCxt);
		Junction j=new Junction();
		j.setId(id+"");
		j.setName("");
		rdCxt.getRn().getOdrNetwork().getJunction().add(j);
		RoadNetworkUtils.refresh(rdCxt);
		return j;
	}
	public void removeJunc() {
		String id=rdCxt.getAppFrame().getJp().getSelectedJn();
		Junction j=rdCxt.getAppFrame().getJp().getJunction(id);
		if(rdCxt.getRn().getOdrNetwork().getJunction().remove(j)){
			rdCxt.getAppFrame().getJp().getCbSelectJunc().removeItem(id+"");
			RoadNetworkUtils.refresh(rdCxt);
		}
	}
	public void addNewConn() {
		String id=rdCxt.getAppFrame().getJp().getSelectedJn();
		Junction j=rdCxt.getAppFrame().getJp().getJunction(id);
		Connection max=null;
		int cnid=0;
		if(j.getConnection().size()>0){
			max=Collections.max(j.getConnection(),new Comparator<Connection>() {
				@Override
				public int compare(Connection o1, Connection o2) {
					return Integer.parseInt(o1.getId())-Integer.parseInt(o2.getId());
				}
			});
			cnid=Integer.parseInt(max.getId())+1;
		}
		Connection cn=new Connection();
		cn.setId(cnid+"");
		String[]vls=GraphicsHelper.valuesFromUser("Enter values for new connection","Connecting road id","Incoming road id");
		if(vls[0].equals("") && vls[1].equals(""))return;
		try{
			Integer.parseInt(vls[0]);
			Integer.parseInt(vls[1]);
			if(!validator.existsRoad(vls[0]) || !validator.existsRoad(vls[1]))throw new InvalidInputException("Roads reffered don't exist");
			JunctionsPanel.isPredecessorJunction(rdCxt.getRn().findByUserId(vls[0]).getOdrRoad());
			cn.setConnectingRoad(vls[0]);
			cn.setIncomingRoad(vls[1]);
			cn.setContactPoint("start");
			j.getConnection().add(cn);
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.getAppFrame().getJp().setSelectedJn(id+"");
		}catch(NumberFormatException e){
			GraphicsHelper.showToast(e.getMessage(), rdCxt.getToastDurationMilis());
		}catch(IllegalArgumentException e2){
			GraphicsHelper.showToast(e2.getMessage(), rdCxt.getToastDurationMilis());
		}
	}
	public static int getNextId(RoadContext rdCxt){
		int id=1001;
		if(rdCxt.getRn().getOdrNetwork().getJunction().size()>0){
			Junction max=Collections.max(rdCxt.getRn().getOdrNetwork().getJunction(),new Comparator<Junction>() {
				@Override
				public int compare(Junction o1, Junction o2) {
					return Integer.parseInt(o1.getId())-Integer.parseInt(o2.getId());
				}
			});
			int id2=Integer.parseInt(max.getId())+1;
			if(id<=id2)id=id2;
		}
		return id;
	}
}
