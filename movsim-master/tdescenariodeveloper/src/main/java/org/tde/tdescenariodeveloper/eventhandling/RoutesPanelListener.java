package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.movsim.autogen.Route;
import org.movsim.autogen.Routes;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.ui.RoutesPanel;
import org.tde.tdescenariodeveloper.updation.Conditions;
/**
 * Class used to add new routes and to listen for changes made to routes.
 * @author Shmeel
 * @see RoutesPanel
 * @see Route
 */
public class RoutesPanelListener implements ActionListener,Blockable {
	JButton addRoute,setRoutes,clearRoutes;
	MovsimConfigContext mvCxt;
	boolean blocked=true;
	/**
	 * 
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 */
	public RoutesPanelListener(MovsimConfigContext mvCxt) {
		this.mvCxt = mvCxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=(JButton)e.getSource();
		if(b==addRoute){
			Route r=new Route();
			r.setLabel(getUniqueRouteLabel());
			mvCxt.getMovsim().getScenario().getRoutes().getRoute().add(r);
			mvCxt.updatePanels();
		}else if(b==clearRoutes){
			mvCxt.getMovsim().getScenario().setRoutes(null);
			mvCxt.updatePanels();
		}else if(b==setRoutes){
			Routes r=new Routes();
			mvCxt.getMovsim().getScenario().setRoutes(r);
			mvCxt.updatePanels();
		}
	}
	/**
	 * used to get id of the road which is not already in this route
	 * @return returns id of route not added in route yet
	 */
	public String getUniqueRouteLabel(){
		String s="Route"+((int)(Math.random()*10));
		while(Conditions.existsLabelInRoutes(s, mvCxt)){
			s="Route"+((int)(Math.random()*10));
		}
		return s;
	}
	
	public void setAddRoute(JButton addRoute) {
		this.addRoute = addRoute;
	}

	public void setSetRoutes(JButton setRoutes) {
		this.setRoutes = setRoutes;
	}

	public void setClearRoutes(JButton clearRoutes) {
		this.clearRoutes = clearRoutes;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

}
