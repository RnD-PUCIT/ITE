package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.GeometryUpdater;
import org.tde.tdescenariodeveloper.updation.LanesUpdater;
import org.tde.tdescenariodeveloper.validation.GeometryValidator;
import org.tde.tdescenariodeveloper.validation.LanesValidator;

public class DrawingAreaPopupListener implements ActionListener, ChangeListener {
	boolean blocked=true;
	GeometryValidator gmVl;
	RoadContext rdCxt;
	public DrawingAreaPopupListener(RoadContext rdCxt){
		this.rdCxt=rdCxt;
		gmVl=new GeometryValidator(rdCxt);
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if(blocked)return;
		JSlider src=null;
		if(e.getSource() instanceof JSlider)src=(JSlider)e.getSource();
		if(!src.getValueIsAdjusting()){
			if(src==rdCxt.getDrawingArea().getPopup().getLaneWidthSlider()){
				rdCxt.getLanesPnl().getTfWidth().setText(src.getValue()+"");
			}else if(src==rdCxt.getDrawingArea().getPopup().getsOffsetSlider()){
				rdCxt.getGmPnl().getS().setText(src.getValue()+"");
			}else if(src==rdCxt.getDrawingArea().getPopup().getHdg()){
				rdCxt.getGmPnl().getHdg().setText((src.getValue()/100.0)+"");
			}else if(src==rdCxt.getDrawingArea().getPopup().getCurv()){
				rdCxt.getGmPnl().getCurvature().setText((src.getValue()/2000.0)+"");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JMenuItem src=null;
		if(e.getSource() instanceof JMenuItem)src=(JMenuItem)e.getSource();
		if(src==rdCxt.getDrawingArea().getPopup().getNewgeo()){
			rdCxt.getGmPnl().getAdd().doClick();
			
		}else if(src==rdCxt.getDrawingArea().getPopup().getNewlane()){
			rdCxt.getLanesPnl().getAdd().doClick();
		}else if(src==rdCxt.getDrawingArea().getPopup().getRemovegeo()){
			rdCxt.getGmPnl().getRemove().doClick();
		}else if(src==rdCxt.getDrawingArea().getPopup().getRemoveLane()){
			rdCxt.getLanesPnl().getRemove().doClick();
		}else if(src==rdCxt.getDrawingArea().getPopup().getToggleRotation()){
			rdCxt.getGmPnl().getCurvature().setText(-1*Double.parseDouble(rdCxt.getGmPnl().getCurvature().getText())+"");
		}
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public GeometryValidator getGmVl() {
		return gmVl;
	}
}
