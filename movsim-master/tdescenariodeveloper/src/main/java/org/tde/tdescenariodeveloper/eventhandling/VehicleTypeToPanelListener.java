package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.DistributionTypeEnum;
import org.movsim.autogen.TrafficComposition;
import org.movsim.autogen.VehicleType;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class VehicleTypeToPanelListener implements ActionListener,DocumentListener{
	private boolean blocked=true;
	private VehicleType vt;
	private MovsimConfigContext mvCxt;
	JComboBox<String>label,distType,routeLabel;
	JTextField relV0Rand,fraction;
	JButton remove;
	TrafficComposition tc;
	public VehicleTypeToPanelListener(VehicleType vt, MovsimConfigContext mvCxt,TrafficComposition tc) {
		this.vt = vt;
		this.mvCxt = mvCxt;
		this.tc=tc;
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
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton srcBtn=null;
		JComboBox<String>src=null;
		String s=null;
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		if(e.getSource() instanceof JComboBox<?>){
			src=(JComboBox<String>)e.getSource();
			s=(String)src.getSelectedItem();
		}
		if(src==label){
			if(((String)label.getSelectedItem()).equals(vt.getLabel()))return;
			if(Conditions.existsLabelInVPC(s, mvCxt)){
				if(!DataToViewerConverter.getUsedPrototypes(tc.getVehicleType()).contains(s)){
					vt.setLabel(s);
				}
				else {
					GraphicsHelper.showToast(s+" prototype already in use", mvCxt.getRdCxt().getToastDurationMilis());
					label.setSelectedItem(vt.getLabel());
				}
			}
			else GraphicsHelper.showToast(s+" doesn't exist", mvCxt.getRdCxt().getToastDurationMilis());
		}else if(src==distType){
			if(!s.equals("Default")){
				vt.setV0DistributionType(s.equals("gaussian")?DistributionTypeEnum.GAUSSIAN:DistributionTypeEnum.UNIFORM);
			}
		}else if(src==routeLabel){
			if(!s.equals("None")){
				if(Conditions.existsLabelInRoutes(s, mvCxt))vt.setRouteLabel(s);
				else GraphicsHelper.showToast(s+" doesn't exist", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}else if(srcBtn==remove){
			tc.getVehicleType().remove(vt);
			mvCxt.updatePanels();
		}
	}
	private void textChanged(DocumentEvent e){
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==fraction.getDocument()){
			try{
				double d2=Double.parseDouble(fraction.getText());
				GraphicsHelper.makeBlack(fraction);
				vt.setFraction(d2);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(fraction);
			}
		}else if(doc==relV0Rand.getDocument()){
			try{
				double d2=Double.parseDouble(relV0Rand.getText());
				GraphicsHelper.makeBlack(relV0Rand);
				vt.setRelativeV0Randomization(d2);
			}catch(NumberFormatException e2){
				GraphicsHelper.makeRed(relV0Rand);
			}
		}
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public void setLabel(JComboBox<String> label) {
		this.label = label;
	}
	public void setDistType(JComboBox<String> distType) {
		this.distType = distType;
	}
	public void setRouteLabel(JComboBox<String> routeLabel) {
		this.routeLabel = routeLabel;
	}
	public void setRelV0Rand(JTextField relV0Rand) {
		this.relV0Rand = relV0Rand;
	}
	public void setFraction(JTextField fraction) {
		this.fraction = fraction;
	}
	public void setRemove(JButton remove) {
		this.remove = remove;
	}
}
