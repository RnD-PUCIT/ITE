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
import org.movsim.autogen.Route;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Class used to listen for changes made to related {@link Route}
 * @author Shmeel
 * @see Route
 * @see Road
 */
public class RouteListener implements DocumentListener, ActionListener ,Blockable{

	private JButton removeRoute;
	MovsimConfigContext mvCxt;
	Route rt;
	private JTextField routeLabel;
	private boolean blocked=true;
	private JButton addRoad;
	/**
	 * 
	 * @param mvCxt contains reference to loaded .xprj and other added panels in it
	 * @param r {@link Route}
	 */
	public RouteListener(MovsimConfigContext mvCxt,Route r) {
		super();
		this.mvCxt = mvCxt;
		rt=r;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton b=null;
		if(e.getSource() instanceof JButton)b=(JButton)e.getSource();
		if(b==removeRoute){
			if(mvCxt.getMovsim().getScenario().getRoutes().getRoute().remove(rt)){
				mvCxt.updatePanels();
			}else GraphicsHelper.showToast("Couldn't be removed", mvCxt.getRdCxt().getToastDurationMilis());
		}else if(b==addRoad){
			String id=DataToViewerConverter.getNotUsedRoadRouteId(mvCxt, rt);
			if(id!=null){
				Road r=new Road();
				r.setId(id);
				if(rt.getRoad().add(r)){
					mvCxt.updatePanels();
				}else GraphicsHelper.showToast("Couldn't be added", mvCxt.getRdCxt().getToastDurationMilis());
			}else{
				GraphicsHelper.showToast("All roads are already added", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}
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
	 * called when text of related {@link JTextField} changes
	 * @param e
	 */
	private void textChanged(DocumentEvent e){
		if(blocked)return;
		Document doc=e.getDocument();
		if(doc==routeLabel.getDocument()){
			if(!Conditions.isValid(routeLabel, rt.getLabel()))
				return;
			final String newlbl=routeLabel.getText();
			if(!Conditions.existsLabelInRoutes(newlbl, mvCxt)){
				String oldlbl=rt.getLabel();
				rt.setLabel(newlbl);
				mvCxt.getSimulation().updateRoutesLabel(oldlbl, newlbl);
			}
			else
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						routeLabel.setText(newlbl+((int)(Math.random()*10)));
					}
				});
		}
	}
	public void setRemoveRoute(JButton removeRoute) {
		this.removeRoute = removeRoute;
	}
	public void setRouteLabel(JTextField routeLabel) {
		this.routeLabel=routeLabel;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public void setAddRoad(JButton addRoad) {
		this.addRoad=addRoad;
	}
}
