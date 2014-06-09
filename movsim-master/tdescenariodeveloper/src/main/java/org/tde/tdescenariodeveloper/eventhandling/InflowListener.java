package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.Inflow;
import org.movsim.autogen.TrafficSource;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class InflowListener implements DocumentListener ,ActionListener,Blockable{
	boolean blocked=true;
	MovsimConfigContext mvCxt;
	Inflow inflow;
	TrafficSource ts;
	private JButton remove;
	private JTextField t;
	private JTextField qPerH;
	private JTextField v;
	public InflowListener(MovsimConfigContext mvCxt2, Inflow inflow,
			TrafficSource trafficSource) {
		this.mvCxt=mvCxt2;
		ts=trafficSource;
		this.inflow=inflow;
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
	private void textChanged(DocumentEvent e) {
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==t.getDocument()){
			if(!Conditions.isValid(t,inflow.getT()))
				return;
			try{
				double d2=Double.parseDouble(t.getText());
				GraphicsHelper.makeBlack(t);
				inflow.setT(d2);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(t);
			}
		}else if(doc==v.getDocument()){
			if(!Conditions.isValid(v,inflow.getV()))
				return;
			try{
				double d2=Double.parseDouble(v.getText());
				GraphicsHelper.makeBlack(v);
				inflow.setV(d2);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(v);
			}
		}else if(doc==qPerH.getDocument()){
			if(!Conditions.isValid(qPerH,inflow.getQPerHour()))
				return;
			try{
				double d2=Double.parseDouble(qPerH.getText());
				GraphicsHelper.makeBlack(qPerH);
				inflow.setQPerHour(d2);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(qPerH);
			}
		}
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=(JButton)e.getSource();
		if(b==remove){
			ts.getInflow().remove(inflow);
			mvCxt.updatePanels();
		}
	}

	public void setRemove(JButton remove) {
		this.remove=remove;
	}

	public void setT(JTextField t) {
		this.t=t;
	}

	public void setQPerH(JTextField qPerH) {
		this.qPerH=qPerH;
	}

	public void setV(JTextField v) {
		this.v=v;
	}
	
}
