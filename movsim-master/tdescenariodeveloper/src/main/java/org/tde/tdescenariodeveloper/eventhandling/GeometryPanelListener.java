package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.GeometryUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.validation.GeometryValidator;

public class GeometryPanelListener implements DocumentListener,ActionListener {
	RoadContext rdCxt;
	GeometryValidator validator;
	GeometryUpdater updater;
	boolean docListLocked=true;
	boolean inDocUpdate=false;
	boolean inActionUpdate=false;
	boolean blocked=true;
	public GeometryPanelListener(RoadContext roadPropertiesPanel) {
		rdCxt=roadPropertiesPanel;
		validator=new GeometryValidator(rdCxt);
		updater=new GeometryUpdater(rdCxt);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		if(blocked)return;
		inDocUpdate=true;
		textChnaged(e);
		inDocUpdate=false;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(blocked)return;
		inDocUpdate=true;
		textChnaged(e);
		inDocUpdate=false;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(blocked)return;
		inDocUpdate=true;
		textChnaged(e);
		inDocUpdate=false;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(blocked)return;
		if(inDocUpdate || rdCxt.getSelectedRoad()==null)return;
		inActionUpdate=true;
		JButton srcBtn=null;
		JComboBox<String>srcCb=null;
		if(evt.getSource() instanceof JButton)srcBtn=(JButton)evt.getSource();
		if(evt.getSource() instanceof JComboBox<?>)srcCb=(JComboBox<String>)evt.getSource();
		if(srcCb==rdCxt.getGmPnl().getCbGeom()){
			if(rdCxt.getGmPnl().getCbGeom().getSelectedItem()==null)return;
			rdCxt.getGmPnl().setSelectedGeometry(rdCxt.getGmPnl().getCbGeom().getSelectedIndex(),false);
			rdCxt.getGmPnl().geometryChanged();
		}
		else if(srcBtn==rdCxt.getGmPnl().getAdd()){
			updater.addnew();
			rdCxt.updateGraphics();
		}
		else if(srcBtn==rdCxt.getGmPnl().getRemove()){
			updater.removeCurrent();
			rdCxt.updateGraphics();
		}
		else if(srcCb==rdCxt.getGmPnl().getCbGmType()){
			try{
				if(validator.isValidGmType()){
					updater.updateGmType();
					rdCxt.updateGraphics();
				}else{
					GraphicsHelper.makeRed(rdCxt.getGmPnl().getCurvature());
				}
			}catch(NumberFormatException e){
				GraphicsHelper.showToast(e.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
		inActionUpdate=false;
	}
	public void textChnaged(DocumentEvent e){
		if(docListLocked || rdCxt.getSelectedRoad()==null)return;
		Document doc=e.getDocument();
		if(doc==rdCxt.getGmPnl().getS().getDocument()){
			if(rdCxt.getGmPnl().getS().getText().equals(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).getS()) || rdCxt.getGmPnl().getS().getText().equals(""))
				return;
			try{
				if(validator.isValidS()){
					GraphicsHelper.makeBlack(rdCxt.getGmPnl().getS());
					updater.updateSoffset();
				}
				else{
					GraphicsHelper.makeRed(rdCxt.getGmPnl().getS());
				}
			}catch (NumberFormatException e2) {
				GraphicsHelper.makeRed(rdCxt.getGmPnl().getS());
				GraphicsHelper.showToast(e2.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
		else if(doc==rdCxt.getGmPnl().getTfX().getDocument() || doc==rdCxt.getGmPnl().getTfY().getDocument()){
			try{
				if(validator.isValidXY()){
					GraphicsHelper.makeBlack(rdCxt.getGmPnl().getTfX(),rdCxt.getGmPnl().getTfY());
					updater.updateXY();
					rdCxt.updateGraphics();
				}else{
					GraphicsHelper.makeRed(rdCxt.getGmPnl().getTfX(),rdCxt.getGmPnl().getTfY());
					GraphicsHelper.showToast("Only first geometry's coordinates can be adjusted manually", rdCxt.getToastDurationMilis());
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast(e2.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
		else if(doc==rdCxt.getGmPnl().getL().getDocument()){
			try{
				if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).getLength()==Double.parseDouble(rdCxt.getGmPnl().getL().getText())|| rdCxt.getGmPnl().getS().getText().equals(""))
					return;
				GraphicsHelper.makeBlack(rdCxt.getGmPnl().getL());
				updater.updateLength();
				rdCxt.updateGraphics();
			}catch (NumberFormatException e2) {
				GraphicsHelper.makeRed(rdCxt.getGmPnl().getL());
				GraphicsHelper.showToast(e2.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
		else if(doc==rdCxt.getGmPnl().getCurvature().getDocument()){
			try{
				if(validator.isValidCurv()){
					GraphicsHelper.makeBlack(rdCxt.getGmPnl().getCurvature());
					updater.updateCurv();
					rdCxt.updateGraphics();
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast(e2.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
		else if(doc==rdCxt.getGmPnl().getHdg().getDocument()){
			try{
				if(validator.isValidHdg()){
					GraphicsHelper.makeBlack(rdCxt.getGmPnl().getHdg());
					updater.updateHdg();
					rdCxt.updateGraphics();
				}else{
					GraphicsHelper.makeRed(rdCxt.getGmPnl().getHdg());
					GraphicsHelper.showToast("Enter value b/w -6.2831853071796 to 6.2831853071796", rdCxt.getToastDurationMilis());
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast(e2.getMessage(), rdCxt.getToastDurationMilis());
			}
		}
	}

	public boolean isDocListLocked() {
		return docListLocked;
	}

	public void setDocListLocked(boolean docListLocked) {
		this.docListLocked = docListLocked;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
