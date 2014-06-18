package org.tde.tdescenariodeveloper.eventhandling;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.LaneChangeModelType;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Class used to listening changes made to {@link LaneChangeModelType}
 * @author Shmeel
 * @see LaneChangeModelType
 */
public class LaneChangeModelListener implements DocumentListener,Blockable {
	private boolean blocked=true;
	MovsimConfigContext mvCxt;
	JTextField safeDec,minGap,thAc,rba,plt;
	LaneChangeModelType laneChangeModelType;
	/**
	 * 
	 * @param mv contains reference to loaded .xprj and other added panels in it
	 * @param laneChangeModelType lane change model to which this listener is attached
	 */
	public LaneChangeModelListener(MovsimConfigContext mv, LaneChangeModelType laneChangeModelType) {
		mvCxt=mv;
		this.laneChangeModelType=laneChangeModelType;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);
	}
	public void textChanged(DocumentEvent e){
		if(blocked)return;
		Document src=e.getDocument();
		if(src==safeDec.getDocument()){
			if(!Conditions.isValid(safeDec,laneChangeModelType.getModelParameterMOBIL().getSafeDeceleration()))
				return;
			try{
				double d=Double.parseDouble(safeDec.getText());
				GraphicsHelper.makeBlack(safeDec);
				laneChangeModelType.getModelParameterMOBIL().setSafeDeceleration(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(safeDec);
			}
		}
		else if(src==minGap.getDocument()){
			if(!Conditions.isValid(minGap,laneChangeModelType.getModelParameterMOBIL().getMinimumGap()))
				return;
			try{
				double d=Double.parseDouble(minGap.getText());
				GraphicsHelper.makeBlack(minGap);
				laneChangeModelType.getModelParameterMOBIL().setMinimumGap(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(minGap);
			}
		}else if(src==thAc.getDocument()){
			if(!Conditions.isValid(thAc,laneChangeModelType.getModelParameterMOBIL().getThresholdAcceleration()))
				return;
			try{
				double d=Double.parseDouble(thAc.getText());
				GraphicsHelper.makeBlack(thAc);
				laneChangeModelType.getModelParameterMOBIL().setThresholdAcceleration(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(thAc);
			}
		}else if(src==rba.getDocument()){
			if(!Conditions.isValid(rba,laneChangeModelType.getModelParameterMOBIL().getRightBiasAcceleration()))
				return;
			try{
				double d=Double.parseDouble(rba.getText());
				GraphicsHelper.makeBlack(rba);
				laneChangeModelType.getModelParameterMOBIL().setRightBiasAcceleration(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(rba);
			}
		}else if(src==plt.getDocument()){
			if(!Conditions.isValid(plt,laneChangeModelType.getModelParameterMOBIL().getPoliteness()))
				return;
			try{
				double d=Double.parseDouble(plt.getText());
				GraphicsHelper.makeBlack(plt);
				laneChangeModelType.getModelParameterMOBIL().setPoliteness(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(plt);
			}
		}
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public void setSafeDec(JTextField safeDec) {
		this.safeDec = safeDec;
	}

	public void setMinGap(JTextField minGap) {
		this.minGap = minGap;
	}

	public void setThAc(JTextField thAc) {
		this.thAc = thAc;
	}

	public void setRba(JTextField rba) {
		this.rba = rba;
	}

	public void setPlt(JTextField plt) {
		this.plt = plt;
	}
}
