package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.movsim.autogen.TravelTimes;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.ui.SignalsPanel;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class TravelTimesListener implements Blockable, DocumentListener,ActionListener {
	boolean blocked=true;
	MovsimConfigContext mvCxt;
	TravelTimes tt;
	JComboBox<String>cbRoute;
	JTextField dt,ema;
	JButton remove;
	public void setRemove(JButton remove) {
		this.remove = remove;
	}

	public TravelTimesListener(TravelTimes s, MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		this.tt=s;
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
		if(doc==dt.getDocument()){
			if(!Conditions.isValid(dt,tt.getDt()))
				return;
			try{
				tt.setDt(Double.parseDouble(dt.getText()));
			}catch(NumberFormatException ee){
				GraphicsHelper.showToast("delta time not valid", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}else if(doc==ema.getDocument()){
			if(!Conditions.isValid(ema,tt.getTauEMA()))
				return;
			try{
				tt.setTauEMA(Double.parseDouble(ema.getText()));
			}catch(NumberFormatException ee){
				GraphicsHelper.showToast("EMA value not valid", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}
	}

	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=null;
		if(e.getSource() instanceof JButton)b=(JButton)e.getSource();
		JComboBox<String>cb=null;
		if(e.getSource() instanceof JComboBox<?>)cb=(JComboBox<String>)e.getSource();
		if(b==remove){
			mvCxt.getMovsim().getScenario().getOutputConfiguration().getTravelTimes().remove(tt);
			mvCxt.updatePanels();
		}else if(cb==cbRoute){
			if(!((String)cb.getSelectedItem()).equals("")){
				tt.setRoute((String)cb.getSelectedItem());
			}
		}
	}

	public void setCbRoute(JComboBox<String> cbRoute) {
		this.cbRoute=cbRoute;
	}

	public void setDt(JTextField dt) {
		this.dt=dt;
	}

	public void setEMA(JTextField ema) {
		this.ema=ema;
	}

}
