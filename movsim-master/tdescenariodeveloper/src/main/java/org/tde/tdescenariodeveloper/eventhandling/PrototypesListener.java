package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.Road;
import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.autogen.VehicleType;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Class used to listen for changes made to data of {@link VehiclePrototypeConfiguration}
 * @author Shmeel
 * @see VehiclePrototypeConfiguration
 */
public class PrototypesListener implements ActionListener, DocumentListener ,Blockable{
	private boolean blocked=true;
	private MovsimConfigContext mvCxt;
	private JButton removePrototype=null;
	JTextField label;
	private VehiclePrototypeConfiguration vpc;
	private JTextField lengthTf,widthTf,maxDec;
	/**
	 * 
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
	public PrototypesListener(MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
	}

	private boolean isPrototypeInUse(){
		boolean retval = false;
		String labelRemoved = vpc.getLabel();
		try{
			for(VehicleType v : mvCxt.getMovsim().getScenario().getSimulation().getTrafficComposition().getVehicleType()){
				if(labelRemoved.equals(v.getLabel())){
					retval = true;
					return retval;
				}
			}			
		}catch(Exception e){}
		try{			
			for(Road r : mvCxt.getMovsim().getScenario().getSimulation().getRoad()){
				for(VehicleType v : r.getTrafficComposition().getVehicleType()){
					if(labelRemoved.equals(v.getLabel())){
						retval = true;
						return retval;
					}
				}
			}
		}catch(Exception e){}
		return retval;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton srcBtn=null;
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		if(srcBtn==removePrototype){
			if (isPrototypeInUse())
				GraphicsHelper.showToast("The prortype is in use, cannot be deleted", mvCxt.getRdCxt().getToastDurationMilis());
			else{
				mvCxt.getMovsim().getVehiclePrototypes().getVehiclePrototypeConfiguration().remove(vpc);
				mvCxt.updatePanels();				
			}
		}
	}

	public void setRemovePrototype(JButton removePrototype) {
		this.removePrototype = removePrototype;
	}

	public void setLabel(JTextField label) {
		this.label=label;
	}

	public void setVpc(VehiclePrototypeConfiguration vpc) {
		this.vpc = vpc;
	}

	public void setLengthTf(JTextField lengthTf) {
		this.lengthTf=lengthTf;
	}

	public void setWidthTf(JTextField widthTf) {
		this.widthTf=widthTf;
	}

	public void setMaxDec(JTextField maxDec) {
		this.maxDec=maxDec;
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
	/**
	 * Called when text of related field is changed
	 * @param e document event
	 */
	public void textChanged(DocumentEvent e){
		if(blocked)return;
		Document src=e.getDocument();
		if(src==label.getDocument()){
			if(!Conditions.isValid(label, vpc.getLabel()))
				return;
			final String newlbl=label.getText();
			if(!Conditions.existsLabelInVPC(newlbl,mvCxt)){
				String oldlbl=vpc.getLabel();
				vpc.setLabel(newlbl);
				mvCxt.getSimulation().updateTrafficCompostionLabel(oldlbl,newlbl);
			}
			else
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						label.setText(newlbl+((int)(Math.random()*1000)));
					}
				});
		}
		else if(src==lengthTf.getDocument()){
			if(!Conditions.isValid(lengthTf, vpc.getLength()))
				return;
			try{
				double d=Double.parseDouble(lengthTf.getText());
				GraphicsHelper.makeBlack(lengthTf);
				vpc.setLength(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(lengthTf);
			}
		}else if(src==widthTf.getDocument()){
			if(!Conditions.isValid(widthTf, vpc.getWidth()))
				return;
			try{
				double d=Double.parseDouble(widthTf.getText());
				GraphicsHelper.makeBlack(widthTf);
				vpc.setWidth(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(widthTf);
			}
		}else if(src==maxDec.getDocument()){
			if(!Conditions.isValid(maxDec, vpc.getMaximumDeceleration()))
				return;
			try{
				double d=Double.parseDouble(maxDec.getText());
				GraphicsHelper.makeBlack(maxDec);
				vpc.setMaximumDeceleration(d);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(maxDec);
			}
		}
	}
	@Override
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

}
