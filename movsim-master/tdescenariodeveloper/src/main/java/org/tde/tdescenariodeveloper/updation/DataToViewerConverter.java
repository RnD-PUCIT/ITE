package org.tde.tdescenariodeveloper.updation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.autogen.Inflow;
import org.movsim.autogen.Road;
import org.movsim.autogen.Route;
import org.movsim.autogen.TrafficComposition;
import org.movsim.autogen.TrafficSource;
import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.autogen.VehicleType;
import org.tde.tdescenariodeveloper.eventhandling.InflowListener;
import org.tde.tdescenariodeveloper.eventhandling.RoadToPanelListener;
import org.tde.tdescenariodeveloper.eventhandling.VehicleTypeToPanelListener;
import org.tde.tdescenariodeveloper.ui.TDEResources;
import org.tde.tdescenariodeveloper.ui.LaneLinkPanel;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;

public class DataToViewerConverter {
	private static JPanel roadToPanel(MovsimConfigContext mvCxt2, Road r,List<Road>rdList) {
		JPanel main=new JPanel(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 10, 2, 10);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		RoadToPanelListener rtpl=new RoadToPanelListener(mvCxt2,r,rdList);
		JButton remove=new JButton("Remove this road customization",TDEResources.getResources().getRem());
		remove.addActionListener(rtpl);
		rtpl.setRemove(remove);
		main.add(remove,gbc);
		gbc.gridwidth=1;
		
		ArrayList<String>rdNames=new ArrayList<>();
		for(org.movsim.network.autogen.opendrive.OpenDRIVE.Road rr:mvCxt2.getRdCxt().getRn().getOdrNetwork().getRoad())
			LaneLinkPanel.putOrReject(rdNames, rr.getId());

		
		main.add(new JLabel("Select road"),gbc);
		JComboBox<String> id=new JComboBox<String>(rdNames.toArray(new String[rdNames.size()]));
		id.addActionListener(rtpl);
		rtpl.setId(id);
		id.setSelectedItem(r.getId());
		main.add(id,gbc);
		
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		JCheckBox logging=new JCheckBox("Logging");
		logging.addItemListener(rtpl);
		rtpl.setLogging(logging);
		logging.setSelected(r.isSetLogging()?r.isLogging():false);
		main.add(logging,gbc);
		
		JPanel trfCompPnl=new JPanel(new GridBagLayout());
		if(r.isSetTrafficComposition()){
			trfCompPnl.setBorder(new TitledBorder(new LineBorder(TDEResources.TRAFFIC_COMP_BORDER_COLOR, 1, true),"Traffic composition for road "+r.getId() , TitledBorder.LEADING, TitledBorder.TOP, null, TDEResources.TRAFFIC_COMP_BORDER_FONT_COLOR));
			fillTrafficCompositionPnl(mvCxt2, trfCompPnl, r.getTrafficComposition());
			
			JButton newType=new JButton("New type",TDEResources.getResources().getAddIcon());
			newType.addActionListener(rtpl);
			rtpl.setNewType(newType);
			trfCompPnl.add(newType,gbc);
			
			JButton remTrafficComp=new JButton("Remove traffic composition",TDEResources.getResources().getRem());
			remTrafficComp.addActionListener(rtpl);
			rtpl.setRemoveTrafficComp(remTrafficComp);
			trfCompPnl.add(remTrafficComp,gbc);
		}else{
			JButton addTrafficComp=new JButton("Add traffic composition",TDEResources.getResources().getAddIcon());
			addTrafficComp.addActionListener(rtpl);
			rtpl.setAddTrafficComp(addTrafficComp);
			trfCompPnl.add(addTrafficComp,gbc);
		}
		main.add(trfCompPnl,gbc);
		JPanel trfSrc=new JPanel(new GridBagLayout());
		if(r.isSetTrafficSource()){
			trfSrc.setBorder(new TitledBorder(new LineBorder(TDEResources.TRAFFIC_SRC_BORDER_COLOR, 1, true),"Traffic source for road "+r.getId() , TitledBorder.LEADING, TitledBorder.TOP, null, TDEResources.TRAFFIC_SRC_BORDER_FONT_COLOR));
			fillTrafficSourcePanel(mvCxt2, trfSrc, r.getTrafficSource());
			
			JButton addInflow=new JButton("New inflow",TDEResources.getResources().getAddIcon());
			addInflow.addActionListener(rtpl);
			rtpl.setAddInflow(addInflow);
			trfSrc.add(addInflow,gbc);
			
			JButton remTrafficSrc=new JButton("Remove traffic source",TDEResources.getResources().getRem());
			remTrafficSrc.addActionListener(rtpl);
			rtpl.setRemoveTrafficSrc(remTrafficSrc);
			trfSrc.add(remTrafficSrc,gbc);
		}else{
			JButton addTrafficSrc=new JButton("Add traffic traffic source",TDEResources.getResources().getAddIcon());
			addTrafficSrc.addActionListener(rtpl);
			rtpl.setAddTrafficSrc(addTrafficSrc);
			trfSrc.add(addTrafficSrc,gbc);
		}
		main.add(trfSrc,gbc);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		rtpl.setBlocked(false);
		return main;
	}
	private static void fillTrafficSourcePanel(MovsimConfigContext mvCxt2,
			JPanel trfSrc, final TrafficSource trafficSource) {
		trfSrc.removeAll();
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 10, 2, 10);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		
		JCheckBox logging=new JCheckBox("Logging");
		logging.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				trafficSource.setLogging(((JCheckBox)e.getSource()).isSelected());
			}
		});
		logging.setSelected(trafficSource.isSetLogging()?trafficSource.isLogging():false);
		trfSrc.add(logging,gbc);
		
		JPanel p=inflowToPnl(mvCxt2,trafficSource.getInflow().get(0),trafficSource,false);
		p.setBorder(new TitledBorder(new LineBorder(TDEResources.INFLOW_BORDER_COLOR, 1, true),"Inflow-1" , TitledBorder.LEADING, TitledBorder.TOP, null, TDEResources.INFLOW_BORDER_FONT_COLOR));
		trfSrc.add(p,gbc);
		for(int i=1;i<trafficSource.getInflow().size();i++){
			p=inflowToPnl(mvCxt2,trafficSource.getInflow().get(i),trafficSource,true);
			p.setBorder(new TitledBorder(new LineBorder(TDEResources.INFLOW_BORDER_COLOR, 1, true),"Inflow-"+(i+1) , TitledBorder.LEADING, TitledBorder.TOP, null, TDEResources.INFLOW_BORDER_FONT_COLOR));
			trfSrc.add(p,gbc);
		}
	}
	private static JPanel inflowToPnl(MovsimConfigContext mvCxt2,Inflow inflow, TrafficSource trafficSource, boolean removable) {
		JPanel main=new JPanel(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		InflowListener il=new InflowListener(mvCxt2,inflow,trafficSource);
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 5, 2, 5);
		gbc.weightx=1;
		if(removable){
			gbc.gridwidth=GridBagConstraints.REMAINDER;
			JButton remove=new JButton("Remove this Inflow",TDEResources.getResources().getRem());
			remove.addActionListener(il);
			il.setRemove(remove);
			main.add(remove,gbc);
			gbc.gridwidth=1;
		}
		
		
		main.add(new JLabel("T"),gbc);
		JTextField t=new JTextField(10);
		t.getDocument().addDocumentListener(il);
		il.setT(t);
		t.setText(inflow.isSetT()?inflow.getT()+"":"");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(t,gbc);
		
		gbc.gridwidth=1;
		main.add(new JLabel("Quantity/hour"),gbc);
		JTextField qPerH=new JTextField(10);
		qPerH.getDocument().addDocumentListener(il);
		il.setQPerH(qPerH);
		qPerH.setText(inflow.isSetQPerHour()?inflow.getQPerHour()+"":"");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(qPerH,gbc);
		
		gbc.gridwidth=1;
		main.add(new JLabel("V"),gbc);
		JTextField v=new JTextField(10);
		v.getDocument().addDocumentListener(il);
		il.setV(v);
		v.setText(inflow.isSetV()?inflow.getV()+"":"");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(v,gbc);
		il.setBlocked(false);
		return main;
	}
	public static void fillTrafficCompositionPnl(MovsimConfigContext mvCxt,JPanel trafficCompositionPnl,TrafficComposition tc){
		trafficCompositionPnl.removeAll();
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 10, 2, 10);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		
		JPanel p=VehTypeToPnl(mvCxt,tc.getVehicleType().get(0),tc,false);
		p.setBorder(new TitledBorder(new LineBorder(TDEResources.VEHICLE_TYPE_BORDER_COLOR, 1, true),"Type-1" , TitledBorder.LEADING, TitledBorder.TOP, null, TDEResources.VEHICLE_TYPE_BORDER_FONT_COLOR));
		trafficCompositionPnl.add(p,gbc);
		for(int i=1;i<tc.getVehicleType().size();i++){
			p=VehTypeToPnl(mvCxt,tc.getVehicleType().get(i),tc,true);
			p.setBorder(new TitledBorder(new LineBorder(TDEResources.VEHICLE_TYPE_BORDER_COLOR,1, true),"Type-"+(i+1) , TitledBorder.LEADING, TitledBorder.TOP, null, TDEResources.VEHICLE_TYPE_BORDER_FONT_COLOR));
			trafficCompositionPnl.add(p,gbc);
		}
	}
	public static void updateFractions(MovsimConfigContext mvCxt) {
		double sum=0.0;
		for(VehicleType vt:mvCxt.getMovsim().getScenario().getSimulation().getTrafficComposition().getVehicleType()){
			sum+=vt.getFraction();
		}
		double unit=1.0/sum;
		for(VehicleType vt:mvCxt.getMovsim().getScenario().getSimulation().getTrafficComposition().getVehicleType()){
			vt.setFraction(unit*vt.getFraction());
		}
		for(Road r:mvCxt.getMovsim().getScenario().getSimulation().getRoad()){
			if(r.isSetTrafficComposition()){
				sum=0.0;
				for(VehicleType vt:r.getTrafficComposition().getVehicleType()){
					sum+=vt.getFraction();
				}
				unit=1.0/sum;
				for(VehicleType vt:r.getTrafficComposition().getVehicleType()){
					vt.setFraction(unit*vt.getFraction());
				}
			}
		}
	}
	public static String getNotUsedPrototypeLabel(MovsimConfigContext mvCxt2,TrafficComposition tc){
		String s=null;
		List<VehicleType>vtList=tc.getVehicleType();
		List<VehiclePrototypeConfiguration>vpcList=mvCxt2.getMovsim().getVehiclePrototypes().getVehiclePrototypeConfiguration();
		if(vtList.size()<vpcList.size()){
			ArrayList<String>usedPrototypes=getUsedPrototypes(vtList);
			for(VehiclePrototypeConfiguration vpc:vpcList){
				if(!usedPrototypes.contains(vpc.getLabel())){
					s=vpc.getLabel();
					break;
				}
			}
		}
		return s;
	}
	public static String getNotUsedRoadCustomizationId(MovsimConfigContext mvCxt2,List<Road>rdList){
		String s=null;
		List<org.movsim.network.autogen.opendrive.OpenDRIVE.Road>odrRdList=mvCxt2.getRdCxt().getRn().getOdrNetwork().getRoad();
		if(rdList.size()<odrRdList.size()){
			ArrayList<String>used=getusedRoadCustomizations(rdList);
			for(org.movsim.network.autogen.opendrive.OpenDRIVE.Road odrRd:odrRdList){
				if(!used.contains(odrRd.getId())){
					s=odrRd.getId();
					break;
				}
			}
		}
		return s;
	}
	public static String getNotUsedRoadRouteId(MovsimConfigContext mvCxt2,Route r){
		String s=null;
		List<org.movsim.network.autogen.opendrive.OpenDRIVE.Road>odrRdList=mvCxt2.getRdCxt().getRn().getOdrNetwork().getRoad();
		if(r.getRoad().size()<odrRdList.size()){
			ArrayList<String>used=getusedRoadCustomizations(r.getRoad());
			for(org.movsim.network.autogen.opendrive.OpenDRIVE.Road odrRd:odrRdList){
				if(!used.contains(odrRd.getId())){
					s=odrRd.getId();
					break;
				}
			}
		}
		return s;
	}
	public static ArrayList<String> getUsedPrototypes(List<VehicleType>vtList) {
		ArrayList<String>usedPrototypes=new ArrayList<>();
		for(VehicleType vt:vtList)
			LaneLinkPanel.putOrReject(usedPrototypes, vt.getLabel());
		return usedPrototypes;
	}
	public static ArrayList<String> getUsedRoadIds(List<Road>rdList) {
		ArrayList<String>used=new ArrayList<>();
		for(Road rd:rdList)
			LaneLinkPanel.putOrReject(used, rd.getId());
		return used;
	}
	public static ArrayList<String> getusedRoadCustomizations(List<Road>rdList) {
		ArrayList<String>used=new ArrayList<>();
		for(Road r:rdList)
			LaneLinkPanel.putOrReject(used, r.getId());
		return used;
	}
	
	public static JPanel VehTypeToPnl(MovsimConfigContext mvCxt,VehicleType vt,TrafficComposition tc, boolean removable) {
		ArrayList<String>prototypesNames=new ArrayList<>();
		ArrayList<String>routeNames=new ArrayList<>();
		String[]DistEnum={"Default","uniform","gaussian"};
		routeNames.add("None");
		for(VehiclePrototypeConfiguration v:mvCxt.getMovsim().getVehiclePrototypes().getVehiclePrototypeConfiguration())
			prototypesNames.add(v.getLabel());
		if(mvCxt.getMovsim().getScenario().isSetRoutes()){
			for(Route v:mvCxt.getMovsim().getScenario().getRoutes().getRoute())
				routeNames.add(v.getLabel());
		}
		JPanel main=new JPanel(new GridBagLayout());
		VehicleTypeToPanelListener vtl=new VehicleTypeToPanelListener(vt, mvCxt,tc);
		
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 5, 2, 5);
		gbc.weightx=1;
		if(removable){
			gbc.gridwidth=GridBagConstraints.REMAINDER;
			JButton remove=new JButton("Remove this type",TDEResources.getResources().getRem());
			remove.addActionListener(vtl);
			vtl.setRemove(remove);
			main.add(remove,gbc);
			gbc.gridwidth=1;
		}
		main.add(new JLabel("Select prototype"),gbc);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		JComboBox<String>label=new JComboBox<String>(prototypesNames.toArray(new String[prototypesNames.size()]));
		label.addActionListener(vtl);
		vtl.setLabel(label);
		label.setSelectedItem(vt.getLabel());
		main.add(label,gbc);
		
		gbc.gridwidth=1;
		main.add(new JLabel("Distribution type"),gbc);
		JComboBox<String>distType=new JComboBox<String>(DistEnum);
		distType.addActionListener(vtl);
		vtl.setDistType(distType);
		distType.setSelectedItem(vt.isSetV0DistributionType()?vt.getV0DistributionType().value():"Default");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(distType,gbc);
		
		gbc.gridwidth=1;
		main.add(new JLabel("Select route"),gbc);
		JComboBox<String>routeLabel=new JComboBox<String>(routeNames.toArray(new String[routeNames.size()]));
		routeLabel.addActionListener(vtl);
		vtl.setRouteLabel(routeLabel);
		routeLabel.setSelectedItem(vt.isSetRouteLabel()?vt.getRouteLabel():"None");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(routeLabel,gbc);
		
		gbc.gridwidth=1;
		main.add(new JLabel("Fraction"),gbc);
		JTextField fraction=new JTextField(10);
		fraction.getDocument().addDocumentListener(vtl);
		vtl.setFraction(fraction);
		fraction.setText(vt.isSetFraction()?vt.getFraction()+"":"");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(fraction,gbc);
		
		gbc.gridwidth=1;
		main.add(new JLabel("Relative v0 randomization"),gbc);
		JTextField relV0Rand=new JTextField(10);
		relV0Rand.getDocument().addDocumentListener(vtl);
		vtl.setRelV0Rand(relV0Rand);
		relV0Rand.setText(vt.isSetRelativeV0Randomization()?vt.getRelativeV0Randomization()+"":"");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(relV0Rand,gbc);
		
		vtl.setBlocked(false);
		return main;
	}
	public static void fillroadsPnl(MovsimConfigContext mvCxt2, JPanel roadsPnl2,
			List<Road> roads) {
		roadsPnl2.removeAll();
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 10, 2, 10);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		Font f=new Font(Font.SANS_SERIF,Font.ITALIC,11);
		for(Road r:roads){
			JPanel p=roadToPanel(mvCxt2,r,roads);
			p.setBorder(new TitledBorder(new LineBorder(TDEResources.ROAD_BORDER_COLOR, 1, true),"Road-"+r.getId() , TitledBorder.LEADING, TitledBorder.TOP, f, TDEResources.ROAD_BORDER_FONT_COLOR));
			roadsPnl2.add(p,gbc);
		}
	}
	public static String getUniquePrototypeLabel(MovsimConfigContext mvCxt){
		String s="Label";
		while(Conditions.existsLabelInVPC(s,mvCxt));
			s="Label"+((int)(Math.random()*10));
		return s;
	}
}
