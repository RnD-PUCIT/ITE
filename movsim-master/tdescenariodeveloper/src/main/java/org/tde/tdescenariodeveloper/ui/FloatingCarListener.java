package org.tde.tdescenariodeveloper.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.FloatingCar;
import org.movsim.autogen.FloatingCarOutput;
import org.movsim.autogen.OutputConfiguration;
import org.movsim.autogen.Route;
import org.tde.tdescenariodeveloper.eventhandling.Blockable;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class FloatingCarListener implements DocumentListener, ActionListener,
		Blockable {
	private boolean blocked=true;
	public void setNumber(JTextField number) {
		this.number = number;
	}

	public void setRemove(JButton remove) {
		this.remove = remove;
	}

	JTextField number;
	JButton remove;
	MovsimConfigContext mvCxt;
	private FloatingCar fc;
	private FloatingCarOutput fco;
	public FloatingCarListener(FloatingCar fc, FloatingCarOutput s,
			MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		this.fc=fc;
		this.fco=s;
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=(JButton)e.getSource();
		if(b==remove){
			fco.getFloatingCar().remove(fc);
			mvCxt.updatePanels();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged(e);
		
	}

	private void textChanged(DocumentEvent e) {
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==number.getDocument()){
			if(!Conditions.isValid(number, fc.getNumber()))
				return;
			try{
				Integer d=Integer.parseInt(fc.getNumber());
				fc.setNumber(d+"");
			}catch(NumberFormatException ee){
				GraphicsHelper.showToast("Number not valid", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);
	}

}
