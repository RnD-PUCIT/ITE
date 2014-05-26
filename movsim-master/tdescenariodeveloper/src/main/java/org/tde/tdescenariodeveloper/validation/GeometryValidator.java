package org.tde.tdescenariodeveloper.validation;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.tde.tdescenariodeveloper.exception.InvalidInputException;
import org.tde.tdescenariodeveloper.ui.RoadContext;

public class GeometryValidator {
	RoadContext rdCxt;
	public GeometryValidator(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
	}

	public boolean isValidS() {
		double s;
		if(rdCxt.getGmPnl().getSelectedIndex()==0){
			s=Double.parseDouble(rdCxt.getGmPnl().getS().getText());
			if(s==0.0)return true;
			else throw new InvalidInputException("First geometry can only have 0.0 offset");
		}

		try{
			s=Double.parseDouble(rdCxt.getGmPnl().getS().getText());
			if(s<getEndLimit() && s>getStartLimit())return true;
		}catch(NumberFormatException e){
			return false;
		}
		return false;
	}
	public double getStartLimit(){
		if(isPrevGmExist()){
			return rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()-1).getS();
		}
		return 0.0;
	}
	public double getEndLimit(){
		Geometry g=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex());
		return g.getLength()+g.getS();
	}
	public boolean isNextGmExist(){
		return rdCxt.getGmPnl().getSelectedIndex()+1<rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size();
	}
	public boolean isPrevGmExist(){
		return rdCxt.getGmPnl().getSelectedIndex()>0;
	}
	public boolean isValidXY(){
		try{
			Double.parseDouble(rdCxt.getGmPnl().getTfX().getText());
			Double.parseDouble(rdCxt.getGmPnl().getTfY().getText());
		}catch(NumberFormatException e){
			throw new InvalidInputException("Coordinates are not parsable");
		}
		return rdCxt.getGmPnl().getSelectedIndex()==0;
	}

	public boolean isValidHdg() {
		double d;
		try{
			d=Double.parseDouble(rdCxt.getGmPnl().getHdg().getText());
		}catch(NumberFormatException e){
			throw new InvalidInputException("Direction isn't parsable");
		}
		return d>-6.2831853071796 && d<6.2831853071796;
	}

	public boolean isValidGmType() {
		if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).isSetLine()){
			return true;
		}else if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).isSetArc()){
			try{
				Double.parseDouble(rdCxt.getGmPnl().getCurvature().getText());
				return true;
			}catch(NumberFormatException e){
				throw new InvalidInputException("curvature isn't parsable");
			}
		}
		return false;
	}

	public boolean isValidCurv() {
		if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).isSetArc()){
			try{
				Double.parseDouble(rdCxt.getGmPnl().getCurvature().getText());
				return true;
			}catch(NumberFormatException e){
				throw new InvalidInputException("curvature isn't parsable");
			}
		}
		return false;
	}
}
