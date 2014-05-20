package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.tde.tdescenariodeveloper.ui.RoadPropertiesPanel;
import org.tde.tdescenariodeveloper.updation.GeometryUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.validation.GeometryValidator;

public class GeometryPanelListener implements DocumentListener,ActionListener {
	RoadPropertiesPanel rdPrPnl;
	GeometryValidator validator;
	GeometryUpdater updater;
	boolean docListLocked=true;
	boolean inDocUpdate=false;
	boolean inActionUpdate=false;
	boolean blocked=false;
	public GeometryPanelListener(RoadPropertiesPanel roadPropertiesPanel) {
		rdPrPnl=roadPropertiesPanel;
		validator=new GeometryValidator(rdPrPnl);
		updater=new GeometryUpdater(rdPrPnl);
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
		if(inDocUpdate || rdPrPnl.getSelectedRoad()==null)return;
		inActionUpdate=true;
		JButton srcBtn=null;
		JComboBox<String>srcCb=null;
		if(evt.getSource() instanceof JButton)srcBtn=(JButton)evt.getSource();
		if(evt.getSource() instanceof JComboBox<?>)srcCb=(JComboBox<String>)evt.getSource();
		if(srcCb==rdPrPnl.getGmPnl().getCbGeom()){
			if(rdPrPnl.getGmPnl().getCbGeom().getSelectedItem()==null)return;
			rdPrPnl.getGmPnl().setGmInd(rdPrPnl.getGmPnl().getCbGeom().getSelectedIndex());
			rdPrPnl.getGmPnl().geometryChanged();
		}
		else if(srcBtn==rdPrPnl.getGmPnl().getAdd()){
			updater.addnew();
			rdPrPnl.updateGraphics();
		}
		else if(srcBtn==rdPrPnl.getGmPnl().getRemove()){
			updater.removeCurrent();
			rdPrPnl.updateGraphics();
		}
		else if(srcCb==rdPrPnl.getGmPnl().getCbGmType()){
			updater.updateGmType();
			rdPrPnl.updateGraphics();
		}
		inActionUpdate=false;
	}
	public void textChnaged(DocumentEvent e){
		if(docListLocked || rdPrPnl.getSelectedRoad()==null)return;
		Document doc=e.getDocument();
		if(doc==rdPrPnl.getGmPnl().getS().getDocument()){
			if(rdPrPnl.getGmPnl().getS().getText().equals(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdPrPnl.getGmPnl().getSelectedIndex()).getS()) || rdPrPnl.getGmPnl().getS().getText().equals(""))
				return;
			try{
				if(validator.isValidS()){
					GraphicsHelper.makeBlack(rdPrPnl.getGmPnl().getS());
					updater.updateSoffset();
				}
				else{
					GraphicsHelper.makeRed(rdPrPnl.getGmPnl().getS());
				}
			}catch (NumberFormatException e2) {
				GraphicsHelper.makeRed(rdPrPnl.getGmPnl().getS());
				GraphicsHelper.showToast(e2.getMessage(), 7000);
			}
		}
		else if(doc==rdPrPnl.getGmPnl().getTfX().getDocument() || doc==rdPrPnl.getGmPnl().getTfY().getDocument()){
			try{
				if(validator.isValidXY()){
					GraphicsHelper.makeBlack(rdPrPnl.getGmPnl().getTfX(),rdPrPnl.getGmPnl().getTfY());
					updater.updateXY();
					rdPrPnl.updateGraphics();
				}else{
					GraphicsHelper.makeRed(rdPrPnl.getGmPnl().getTfX(),rdPrPnl.getGmPnl().getTfY());
					GraphicsHelper.showToast("Only first geometry's coordinates can be adjusted manually", 5000);
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast(e2.getMessage(), 5000);
			}
		}
		else if(doc==rdPrPnl.getGmPnl().getL().getDocument()){
			try{
				if(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdPrPnl.getGmPnl().getSelectedIndex()).getLength()==Double.parseDouble(rdPrPnl.getGmPnl().getL().getText())|| rdPrPnl.getGmPnl().getS().getText().equals(""))
					return;
				GraphicsHelper.makeBlack(rdPrPnl.getGmPnl().getL());
				updater.updateLength();
				rdPrPnl.updateGraphics();
			}catch (NumberFormatException e2) {
				GraphicsHelper.makeRed(rdPrPnl.getGmPnl().getL());
				GraphicsHelper.showToast(e2.getMessage(), 7000);
			}
		}
		else if(doc==rdPrPnl.getGmPnl().getCurvature().getDocument()){
			try{
				if(validator.isValidCurv()){
					GraphicsHelper.makeBlack(rdPrPnl.getGmPnl().getHdg());
					updater.updateCurv();
					rdPrPnl.updateGraphics();
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast(e2.getMessage(), 5000);
			}
		}
		else if(doc==rdPrPnl.getGmPnl().getHdg().getDocument()){
			try{
				if(validator.isValidHdg()){
					GraphicsHelper.makeBlack(rdPrPnl.getGmPnl().getHdg());
					updater.updateHdg();
					rdPrPnl.updateGraphics();
				}else{
					GraphicsHelper.makeRed(rdPrPnl.getGmPnl().getHdg());
					GraphicsHelper.showToast("Enter value b/w -6.2831853071796 to 6.2831853071796", 5000);
				}
			}catch(NumberFormatException e2){
				GraphicsHelper.showToast(e2.getMessage(), 5000);
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
