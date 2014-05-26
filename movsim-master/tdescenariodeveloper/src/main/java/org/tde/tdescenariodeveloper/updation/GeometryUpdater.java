package org.tde.tdescenariodeveloper.updation;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry.Arc;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry.Line;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.exception.InvalidInputException;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class GeometryUpdater {
	RoadContext rdCxt;
	public GeometryUpdater(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}

	public void updateSoffset() {
		if(rdCxt.getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
			Geometry g=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex());
			Geometry prevG=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()-1);
			
			double s=Double.parseDouble(rdCxt.getGmPnl().getS().getText());
			g.setS(s);
			g.setLength(getNext()!=null?getNext().getS()-s:rdCxt.getSelectedRoad().roadLength()-s);
			prevG.setLength(s-prevG.getS());
			RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
			RoadNetworkUtils.refresh(rdCxt);
		}
	}
	public void updateLength() throws InvalidInputException{
		double l=Double.parseDouble(rdCxt.getGmPnl().getL().getText());
		if(rdCxt.getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
			RoadMappingPoly rmp=(RoadMappingPoly) rdCxt.getSelectedRoad().roadMapping();
			try{
				if(l<0.0)throw new InvalidInputException("Length can't be -ve");
				rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).setLength(l);
				for(int i=rdCxt.getGmPnl().getSelectedIndex()+1;i<rmp.getRoadMappings().size();i++){
					rmp=(RoadMappingPoly) OpenDriveHandlerJaxb.createRoadMapping(rdCxt.getSelectedRoad().getOdrRoad());
					Geometry preGm=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(i-1);
					RoadMapping prerm=rmp.getRoadMappings().get(i-1);
					Geometry gm=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(i);
					gm.setS(preGm.getS()+preGm.getLength());
					PosTheta p=prerm.map(prerm.roadLength());
					gm.setX(p.x);
					gm.setY(p.y);
					gm.setHdg(p.theta());
				}
				rdCxt.getSelectedRoad().getOdrRoad().setLength(getGmSum());
				RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
				RoadNetworkUtils.refresh(rdCxt);
			}catch(NumberFormatException e){
				GraphicsHelper.showToast(e.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
		else{
			try{
				rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setLength(l);
				rdCxt.getSelectedRoad().getOdrRoad().setLength(l);
				RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
				RoadNetworkUtils.refresh(rdCxt);
			}catch(NumberFormatException e){
				GraphicsHelper.showToast(e.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
	}
	public double getGmSum(){
		double sum=0.0;
		for(Geometry g:rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry()){
			sum+=g.getLength();
		}
		return sum;
	}


	public boolean isNextGmExist(){
		return rdCxt.getGmPnl().getSelectedIndex()+1<rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size();
	}
	public boolean isPrevGmExist(){
		return rdCxt.getGmPnl().getSelectedIndex()>0;
	}
	public Geometry getNext(){
		if(isNextGmExist()){
			return rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()+1);
		}
		return null;
	}

	public void addnew() {
		Geometry g=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size()-1);
		double s=g.getS()+g.getLength();
		Geometry g2=new Geometry();
		RoadSegment rs=rdCxt.getSelectedRoad();
		if(rs.roadMapping() instanceof RoadMappingPoly){
			RoadMappingPoly rmp=(RoadMappingPoly)rs.roadMapping();
			PosTheta p=rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1).map(rmp.getRoadMappings().get(rmp.getRoadMappings().size()-1).roadLength());
			g2.setX(p.x);
			g2.setY(p.y);
			g2.setHdg(p.theta());
		}else{
			RoadMapping rm=rs.roadMapping();
			PosTheta p=rm.map(rm.roadLength());
			g2.setX(p.x);
			g2.setY(p.y);
			g2.setHdg(p.theta());
		}
		g2.setS(s);
		g2.setLine(new Line());
		g2.setLength(g.getLength());
		rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().add(g2);
		rdCxt.getSelectedRoad().getOdrRoad().setLength(getGmSum());
		RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
		RoadNetworkUtils.refresh(rdCxt);
	}

	public void removeCurrent() {
		if(rdCxt.getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
			if(rdCxt.getGmPnl().getSelectedIndex()>0){
				rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().remove(rdCxt.getGmPnl().getSelectedIndex());
				rdCxt.getSelectedRoad().getOdrRoad().setLength(getGmSum());
				RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
				RoadNetworkUtils.refresh(rdCxt);
			}
			else{
				GraphicsHelper.showToast("Remove geometries otherthan first one.", rdCxt.getToastDurationMilis());
			}
		}
		else{
			GraphicsHelper.showToast("Not deleted: A road must have one geometry.", rdCxt.getToastDurationMilis());
			rdCxt.getGmPnl().getRemove().setEnabled(false);
		}
	}

	public void updateXY() {
		double x=Double.parseDouble(rdCxt.getGmPnl().getTfX().getText());
		double y=Double.parseDouble(rdCxt.getGmPnl().getTfY().getText());
		rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setX(x);
		rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setY(y);
		RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
		RoadNetworkUtils.refresh(rdCxt);
	}
	public void updateHdg(){
		double hdg=Double.parseDouble(rdCxt.getGmPnl().getHdg().getText());
		rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setHdg(hdg);
		RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
		RoadNetworkUtils.refresh(rdCxt);
	}
	public void updateCurv(){
		double curv=Double.parseDouble(rdCxt.getGmPnl().getCurvature().getText());
		rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).getArc().setCurvature(curv);
		RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
		RoadNetworkUtils.refresh(rdCxt);
	}
	public void updateGmType(){
		if(((String)rdCxt.getGmPnl().getCbGmType().getSelectedItem()).equals("line")){
			rdCxt.getGmPnl().getarcTypePnl().setVisible(false);
			if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).isSetLine()){
				return;
			}
			else{
				rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).setArc(null);
				rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).setLine(new Line());
				RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
				RoadNetworkUtils.refresh(rdCxt);
			}
		}else{
			rdCxt.getGmPnl().getarcTypePnl().setVisible(true);
			if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).isSetArc()){
				rdCxt.getGmPnl().getCurvature().setText(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).getArc().getCurvature()+"");
				return;
			}
			else{
				Arc arc=new Arc();
				String s=GraphicsHelper.valueFromUser("Enter curvature: e.g -0.001456");
				if(GraphicsHelper.isDouble(s)){
					arc.setCurvature(Double.parseDouble(s));
					rdCxt.getGmPnl().getCurvature().setText(arc.getCurvature()+"");
					rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).setLine(null);
					rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).setArc(arc);
					RoadNetworkUtils.updateCoordinatesAndHeadings(rdCxt);
					RoadNetworkUtils.refresh(rdCxt);
				}else{
					rdCxt.getGmPnl().getCbGmType().setSelectedItem("line");
					GraphicsHelper.showToast("Value entered is not a number", rdCxt.getToastDurationMilis());
				}
			}
		}
	}
}
