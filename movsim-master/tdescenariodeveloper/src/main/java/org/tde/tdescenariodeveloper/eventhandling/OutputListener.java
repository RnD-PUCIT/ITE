package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.movsim.autogen.FloatingCarOutput;
import org.movsim.autogen.OutputConfiguration;
import org.movsim.autogen.Route;
import org.movsim.autogen.Trajectories;
import org.movsim.autogen.TravelTimes;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class OutputListener implements ActionListener,Blockable{
	boolean blocked=true;
	JButton addNewTravelTimes,addNewFloatingCars;
	MovsimConfigContext mvCxt;
	public OutputListener(MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;		
	}
	@Override
	public void setBlocked(boolean b) {
		blocked=b;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=(JButton)e.getSource();
		if(b==addNewFloatingCars){
			Route r=null;
			if(mvCxt.getMovsim().getScenario().isSetRoutes()){
				for(Route rt:mvCxt.getMovsim().getScenario().getRoutes().getRoute()){
					r=rt;
					break;
				}
			}
			if(r==null){
				GraphicsHelper.showToast("No route found", mvCxt.getRdCxt().getToastDurationMilis());
				return;
			}
			if(!mvCxt.getMovsim().getScenario().isSetOutputConfiguration()){
				mvCxt.getMovsim().getScenario().setOutputConfiguration(new OutputConfiguration());
			}
			FloatingCarOutput fco=new FloatingCarOutput();
			fco.setNTimestep(2);
			fco.setRandomFraction(0.5);
			fco.setRoute(r.getLabel());
			mvCxt.getMovsim().getScenario().getOutputConfiguration().getFloatingCarOutput().add(fco);
			mvCxt.updatePanels();
		}else if(b==addNewTravelTimes){
			Route r=null;
			if(mvCxt.getMovsim().getScenario().isSetRoutes()){
				for(Route rt:mvCxt.getMovsim().getScenario().getRoutes().getRoute()){
					r=rt;
					break;
				}
			}
			if(r==null){
				GraphicsHelper.showToast("No route found", mvCxt.getRdCxt().getToastDurationMilis());
				return;
			}
			if(!mvCxt.getMovsim().getScenario().isSetOutputConfiguration()){
				mvCxt.getMovsim().getScenario().setOutputConfiguration(new OutputConfiguration());
			}
			final TravelTimes t=new TravelTimes();
			t.setDt(0.2);
			t.setRoute(r.getLabel());
			t.setTauEMA(0.5);
			mvCxt.getMovsim().getScenario().getOutputConfiguration().getTravelTimes().add(t);
			mvCxt.updatePanels();
		}
	}
	public void setAddNewFloatingCars(JButton addNewFloatingCars) {
		this.addNewFloatingCars=addNewFloatingCars;
	}
	public void setAddNewTravelTimes(JButton addNewTravalTimes) {
		this.addNewTravelTimes = addNewTravalTimes;
	}
}
