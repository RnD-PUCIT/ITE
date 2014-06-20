package org.tde.tdescenariodeveloper.ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.autogen.Road;
import org.movsim.autogen.Route;
import org.movsim.autogen.Routes;
import org.tde.tdescenariodeveloper.eventhandling.RouteListener;
import org.tde.tdescenariodeveloper.eventhandling.RoutesPanelListener;
import org.tde.tdescenariodeveloper.eventhandling.RoutesRoadToPanelListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Used to represent routes graphically found in {@link Routes}
 * @author Shmeel
 * @see Route 
 * @see Routes
 * @see RouteListener
 * @see RoutesPanel
 * @see RoutesRoadToPanelListener
 */
public class RoutesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8845422858845048336L;
	MovsimConfigContext mvCxt; 
	JPanel routesPanel;
	JButton addRoute,setRoutes,clearRoutes;
	/**
	 * 
	 * @param movsimConfigPane contains reference to loaded .xprj file and other panels added to it
	 */
	public RoutesPanel(MovsimConfigContext movsimConfigPane) {
		this.mvCxt=movsimConfigPane;
		
		addRoute=new JButton("New route",TDEResources.getResources().getAddIcon());
		setRoutes=new JButton("Set routes",TDEResources.getResources().getAddIcon());
		clearRoutes=new JButton("Clear all routes",TDEResources.getResources().getRem());
		RoutesPanelListener rpl=new RoutesPanelListener(mvCxt);
		addRoute.addActionListener(rpl);
		setRoutes.addActionListener(rpl);
		clearRoutes.addActionListener(rpl);
		routesPanel=new JPanel(new GridBagLayout());
		routesPanel.setOpaque(false);
		JScrollPane sp=new JScrollPane(routesPanel);
		sp.getViewport().setOpaque(false);
		sp.setOpaque(false);
		rpl.setAddRoute(addRoute);
		rpl.setClearRoutes(clearRoutes);
		rpl.setSetRoutes(setRoutes);
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.weightx=1;
		gbc.weighty=1;
		gbc.insets=new Insets(5, 5, 5, 5);
		setLayout(new GridBagLayout());
		add(sp,gbc);
		rpl.setBlocked(false);
	}
	/**
	 * udpates routes panel from loaded input in memory
	 */
	public void updateRoutesPanel(){
		fillRoutesPanel();
	}
	private void fillRoutesPanel() {
		routesPanel.removeAll();
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.weightx=1;
		gbc.insets=new Insets(15, 5, 15, 5);
		gbc.fill=GridBagConstraints.BOTH;
		Font f=new Font(Font.SANS_SERIF,Font.BOLD,14);
		if(mvCxt.getMovsim().getScenario().isSetRoutes()){
			routesPanel.add(clearRoutes,gbc);
			int c=1;
			for(Route rt:mvCxt.getMovsim().getScenario().getRoutes().getRoute()){
				JPanel p=routeToPanel(rt);
				p.setOpaque(false);
				p.setBorder(new TitledBorder(new LineBorder(TDEResources.ROUTE_BORDER_COLOR, 2, true),"Route "+c++ , TitledBorder.LEADING, TitledBorder.TOP, f, TDEResources.ROUTE_BORDER_FONT_COLOR));
				routesPanel.add(p,gbc);
			}
			routesPanel.add(addRoute,gbc);
		}else{
			routesPanel.add(setRoutes,gbc);
		}
	}
	/**
	 * converts {@link Route} to {@link JPanel}
	 * @param rt {@link Route} to be converted
	 * @return {@link JPanel}
	 */
	public JPanel routeToPanel(Route rt) {
		JPanel main=new JPanel(new GridBagLayout());
		main.setOpaque(false);
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.weightx=1;
		gbc.insets=new Insets(5, 5, 5, 5);
		gbc.fill=GridBagConstraints.BOTH;
		
		RouteListener rl=new RouteListener(mvCxt,rt);
		JButton removeRoute=new JButton("Remove this route",TDEResources.getResources().getRem());
		removeRoute.addActionListener(rl);
		rl.setRemoveRoute(removeRoute);
		main.add(removeRoute,gbc);
		
		gbc.gridwidth=1;
		main.add(new JLabel("Label"),gbc);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		JTextField routeLabel=new JTextField(10);
		routeLabel.getDocument().addDocumentListener(rl);
		rl.setRouteLabel(routeLabel);
		routeLabel.setText(rt.getLabel());
		main.add(routeLabel,gbc);
		int c=1;
		for(Road r:rt.getRoad()){
			if(c++%4==0 && c>4)gbc.gridwidth=GridBagConstraints.REMAINDER;
			else gbc.gridwidth=1;
			JPanel p=roadToPanel(r,rt);
			p.setOpaque(false);
			p.setBorder(new TitledBorder(new LineBorder(TDEResources.ROAD_BORDER_COLOR, 1, true),"Road "+c , TitledBorder.LEADING, TitledBorder.TOP, null, TDEResources.ROAD_BORDER_FONT_COLOR));
			main.add(p,gbc);
		}
		JButton addRoad=new JButton("Add road to this route",TDEResources.getResources().getAddIcon());
		addRoad.addActionListener(rl);
		rl.setAddRoad(addRoad);
		main.add(addRoad,gbc);
		
		
		rl.setBlocked(false);
		return main;
	}
	/**
	 * converts {@link Road} to {@link JPanel}
	 * @param r {@link Road}
	 * @param rt {@link Route}
	 * @return {@link JPanel}s
	 */
	public JPanel roadToPanel(Road r,Route rt) {
		JPanel main=new JPanel(new GridBagLayout());
		main.setOpaque(false);
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.weightx=1;
		gbc.insets=new Insets(10, 5, 10, 5);
		RoutesRoadToPanelListener rtpl=new RoutesRoadToPanelListener(mvCxt, r,rt);
		
		JButton removeRoad=new JButton("Remove this road from route",TDEResources.getResources().getRem());
		removeRoad.addActionListener(rtpl);
		rtpl.setRemoveRoad(removeRoad);
		main.add(removeRoad,gbc);
		
		ArrayList<String>rdNames=new ArrayList<>();
		for(org.movsim.network.autogen.opendrive.OpenDRIVE.Road rr:mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad())
			LaneLinkPanel.putOrReject(rdNames, rr.getId());
		
		gbc.gridwidth=1;
		main.add(new JLabel("Select road"),gbc);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		JComboBox<String> id=new JComboBox<String>(rdNames.toArray(new String[rdNames.size()]));
		id.setMaximumRowCount(4);
		id.addActionListener(rtpl);
		rtpl.setId(id);
		id.setSelectedItem(r.getId());
		main.add(id,gbc);
		rtpl.setBlocked(false);
		return main;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
