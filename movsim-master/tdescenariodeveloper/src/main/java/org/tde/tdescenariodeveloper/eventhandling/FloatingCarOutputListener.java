package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.FloatingCar;
import org.movsim.autogen.FloatingCarOutput;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class FloatingCarOutputListener implements Blockable, ActionListener,
		DocumentListener {
	boolean blocked=true;
	JComboBox<String>cbRoute;
	JTextField timeStep,randomFraction;
	JButton addnewCar,remove;
	public void setRemove(JButton remove) {
		this.remove = remove;
	}

	MovsimConfigContext mvCxt;
	FloatingCarOutput fco;
	public FloatingCarOutputListener(FloatingCarOutput fco,
			MovsimConfigContext mvCxt) {
		super();
		this.mvCxt = mvCxt;
		this.fco = fco;
	}

	public void setCbRoute(JComboBox<String> cbRoute) {
		this.cbRoute = cbRoute;
	}

	public void setTimeStep(JTextField timeStep) {
		this.timeStep = timeStep;
	}

	public void setRandomFraction(JTextField randomFraction) {
		this.randomFraction = randomFraction;
	}

	public void setAddnewCar(JButton addnewCar) {
		this.addnewCar = addnewCar;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged(e);
	}

	private void textChanged(DocumentEvent e) {
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==timeStep.getDocument()){
			if(!Conditions.isValid(timeStep,fco.getNTimestep()))
				return;
			try{
				fco.setNTimestep(Integer.parseInt(timeStep.getText()));
			}catch(NumberFormatException ee){
				GraphicsHelper.showToast("Time step is not valid", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}else if(doc==randomFraction.getDocument()){
			if(!Conditions.isValid(randomFraction,fco.getRandomFraction()))
				return;
			try{
				fco.setRandomFraction(Double.parseDouble(randomFraction.getText()));
			}catch(NumberFormatException ee){
				GraphicsHelper.showToast("EMA value not valid", mvCxt.getRdCxt().getToastDurationMilis());
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=null;
		if(e.getSource() instanceof JButton)b=(JButton)e.getSource();
		JComboBox<String>cb=null;
		if(e.getSource() instanceof JComboBox<?>)cb=(JComboBox<String>)e.getSource();
		if(b==remove){
			mvCxt.getMovsim().getScenario().getOutputConfiguration().getFloatingCarOutput().remove(fco);
			mvCxt.getOutput().updateOutputPanels();
		}else if(cb==cbRoute){
			if(!((String)cb.getSelectedItem()).equals("")){
				fco.setRoute((String)cb.getSelectedItem());
			}
		}else if(b==addnewCar){
			FloatingCar fc=new FloatingCar();
			fc.setNumber((int)((Math.random()*100))+"");
			fco.getFloatingCar().add(fc);
			mvCxt.updatePanels();
		}
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

}
