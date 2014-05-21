package org.tde.tdescenariodeveloper.validation;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.tde.tdescenariodeveloper.exception.InvalidInputException;
import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;

public class GeometryValidator {
	RoadPropertiesPanel rdPrPnl;
	public GeometryValidator(RoadPropertiesPanel rdPrPnl) {
		this.rdPrPnl=rdPrPnl;
	}

	public boolean isValidS() {
		double endLimit,startLimit,s;
		if(rdPrPnl.getGmPnl().getSelectedIndex()==0){
			s=Double.parseDouble(rdPrPnl.getGmPnl().getS().getText());
			if(s==0.0)return true;
			else throw new InvalidInputException("First geometry can only have 0.0 offset");
		}
		if(isPrevGmExist()){
			startLimit=rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdPrPnl.getGmPnl().getSelectedIndex()-1).getS();
		}
		else startLimit=0;
		Geometry g=rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdPrPnl.getGmPnl().getSelectedIndex());
		endLimit=g.getLength()+g.getS();
		try{
			s=Double.parseDouble(rdPrPnl.getGmPnl().getS().getText());
			if(s<endLimit && s>startLimit)return true;
		}catch(NumberFormatException e){
			return false;
		}
		return false;
	}

	public boolean isNextGmExist(){
		return rdPrPnl.getGmPnl().getSelectedIndex()+1<rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size();
	}
	public boolean isPrevGmExist(){
		return rdPrPnl.getGmPnl().getSelectedIndex()>0;
	}
	public boolean isValidXY(){
		try{
			Double.parseDouble(rdPrPnl.getGmPnl().getTfX().getText());
			Double.parseDouble(rdPrPnl.getGmPnl().getTfY().getText());
		}catch(NumberFormatException e){
			throw new InvalidInputException("Coordinates are not parsable");
		}
		return rdPrPnl.getGmPnl().getSelectedIndex()==0;
	}

	public boolean isValidHdg() {
		double d;
		try{
			d=Double.parseDouble(rdPrPnl.getGmPnl().getHdg().getText());
		}catch(NumberFormatException e){
			throw new InvalidInputException("Direction isn't parsable");
		}
		return d>-6.2831853071796 && d<6.2831853071796;
	}

	public boolean isValidGmType() {
		if(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdPrPnl.getGmPnl().getSelectedIndex()).isSetLine()){
			return true;
		}else if(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdPrPnl.getGmPnl().getSelectedIndex()).isSetArc()){
			try{
				Double.parseDouble(rdPrPnl.getGmPnl().getCurvature().getText());
				return true;
			}catch(NumberFormatException e){
				throw new InvalidInputException("curvature isn't parsable");
			}
		}
		return false;
	}

	public boolean isValidCurv() {
		if(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdPrPnl.getGmPnl().getSelectedIndex()).isSetArc()){
			try{
				Double.parseDouble(rdPrPnl.getGmPnl().getCurvature().getText());
				return true;
			}catch(NumberFormatException e){
				throw new InvalidInputException("curvature isn't parsable");
			}
		}
		return false;
	}
}
