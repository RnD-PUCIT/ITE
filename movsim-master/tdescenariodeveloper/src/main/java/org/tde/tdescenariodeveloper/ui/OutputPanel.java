package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
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

import org.movsim.autogen.FloatingCar;
import org.movsim.autogen.FloatingCarOutput;
import org.movsim.autogen.Route;
import org.movsim.autogen.TravelTimes;
import org.tde.tdescenariodeveloper.eventhandling.FloatingCarListener;
import org.tde.tdescenariodeveloper.eventhandling.FloatingCarOutputListener;
import org.tde.tdescenariodeveloper.eventhandling.OutputListener;
import org.tde.tdescenariodeveloper.eventhandling.TravelTimesListener;
public class OutputPanel extends JPanel {
	JPanel floatingCarPnl,travalTimesPnl;
	JButton addNewTravelTimes,addNewFloatingCars;
	
	MovsimConfigContext mvCxt;
	public OutputPanel(MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		floatingCarPnl=new JPanel(new GridBagLayout());
		travalTimesPnl=new JPanel(new GridBagLayout());
		setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.anchor=GridBagConstraints.CENTER;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=1;
		c.insets=new Insets(5,10,5,10);
		JScrollPane sp1=new JScrollPane(floatingCarPnl),sp4=new JScrollPane(travalTimesPnl);
		sp1.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Floating cars", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		sp4.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Traval times", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(sp1,c);
		add(sp4,c);
		
		
		OutputListener ol=new OutputListener(mvCxt);
		
		addNewFloatingCars=new JButton("New floating cars group",TDEResources.getResources().getAddIcon());
		addNewFloatingCars.addActionListener(ol);
		ol.setAddNewFloatingCars(addNewFloatingCars);
		
		addNewTravelTimes=new JButton("New travel time",TDEResources.getResources().getAddIcon());
		addNewTravelTimes.addActionListener(ol);
		ol.setAddNewTravelTimes(addNewTravelTimes);
	}
	public void updateOutputPanels(){
		floatingCarPnl.removeAll();
		travalTimesPnl.removeAll();

		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		floatingCarPnl.add(addNewFloatingCars,c);
		travalTimesPnl.add(addNewTravelTimes,c);
		if(!mvCxt.getMovsim().getScenario().isSetOutputConfiguration()){
			return;
		}
		if(mvCxt.getMovsim().getScenario().getOutputConfiguration().isSetFloatingCarOutput()){
			fillFloatingCarPnl(floatingCarPnl,mvCxt);
		}
		if(mvCxt.getMovsim().getScenario().getOutputConfiguration().isSetTravelTimes()){
			fillTravalTimesPnl(travalTimesPnl,mvCxt);
		}
	}
	private void fillTravalTimesPnl(JPanel travalTimesPnl,
			MovsimConfigContext mvCxt) {
		travalTimesPnl.removeAll();

		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		
		travalTimesPnl.add(addNewTravelTimes,c);
		for(TravelTimes s:mvCxt.getMovsim().getScenario().getOutputConfiguration().getTravelTimes()){
			JPanel p=travalTimeToPanel(s, mvCxt);
			p.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Traval time", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			travalTimesPnl.add(p,c);
		}
	}
	private JPanel travalTimeToPanel(TravelTimes s, MovsimConfigContext mvCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		TravelTimesListener tl=new TravelTimesListener(s,mvCxt);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.weightx=1;
		
		JButton remove=new JButton("Remove",TDEResources.getResources().getRem());
		remove.addActionListener(tl);
		tl.setRemove(remove);
		main.add(remove,c);
		
		c.gridwidth=1;
		main.add(new JLabel("Select route"),c);
		ArrayList<String>routes=new ArrayList<>();
		for(Route r:mvCxt.getMovsim().getScenario().getRoutes().getRoute())
			routes.add(r.getLabel());
		
		JComboBox<String>cbRoute=new JComboBox<String>(routes.toArray(new String[routes.size()]));
		cbRoute.addActionListener(tl);
		tl.setCbRoute(cbRoute);
		cbRoute.setSelectedItem(s.getRoute());
		c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(cbRoute,c);
		
		c.gridwidth=1;
		main.add(new JLabel("Delta time (dt)"),c);
		JTextField dt=new JTextField(5);
		dt.getDocument().addDocumentListener(tl);
		tl.setDt(dt);
		dt.setText(s.getDt()+"");
		c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(dt,c);
		
		c.gridwidth=1;
		main.add(new JLabel("TAO EMA"),c);
		JTextField ema=new JTextField(5);
		ema.getDocument().addDocumentListener(tl);
		tl.setEMA(ema);
		ema.setText(s.getTauEMA()+"");
		c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(ema,c);
		
		tl.setBlocked(false);
		return main;
	}
	private void fillFloatingCarPnl(JPanel floatingCarPnl,
			MovsimConfigContext mvCxt) {
		floatingCarPnl.removeAll();
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		floatingCarPnl.add(addNewFloatingCars,c);
		for(FloatingCarOutput s:mvCxt.getMovsim().getScenario().getOutputConfiguration().getFloatingCarOutput()){
			JPanel p=floatingCarOutputToPanel(s, mvCxt);
			p.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Floating cars", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			floatingCarPnl.add(p,c);
		}
	}
	private JPanel floatingCarOutputToPanel(FloatingCarOutput s,
			MovsimConfigContext mvCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		JPanel floatingcarPnl=new JPanel(new GridBagLayout());
		fillFloatingCarPnl(s,floatingcarPnl,mvCxt);
		FloatingCarOutputListener tl=new FloatingCarOutputListener(s,mvCxt);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.weightx=1;
		c.insets=new Insets(5, 5, 5, 5);
		
		
		JButton remove=new JButton("Remove this group",TDEResources.getResources().getRem());
		remove.addActionListener(tl);
		tl.setRemove(remove);
		main.add(remove,c);
		
		
		c.gridwidth=1;
		main.add(new JLabel("Select route"),c);
		ArrayList<String>routes=new ArrayList<>();
		for(Route r:mvCxt.getMovsim().getScenario().getRoutes().getRoute())
			routes.add(r.getLabel());
		
		JComboBox<String>cbRoute=new JComboBox<String>(routes.toArray(new String[routes.size()]));
		cbRoute.addActionListener(tl);
		tl.setCbRoute(cbRoute);
		cbRoute.setSelectedItem(s.getRoute());
		c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(cbRoute,c);
		
		c.gridwidth=1;
		main.add(new JLabel("Time step"),c);
		JTextField dt=new JTextField(5);
		dt.getDocument().addDocumentListener(tl);
		tl.setTimeStep(dt);
		dt.setText(s.getNTimestep()+"");
		c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(dt,c);
		
		c.gridwidth=1;
		main.add(new JLabel("Random fraction (Probability)"),c);
		JTextField rf=new JTextField(5);
		rf.getDocument().addDocumentListener(tl);
		tl.setRandomFraction(rf);
		rf.setText(s.getRandomFraction()+"");
		c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(rf,c);
		main.add(floatingcarPnl,c);
		
		
		JButton addnewcar=new JButton("Add new car",TDEResources.getResources().getAddIcon());
		addnewcar.addActionListener(tl);
		tl.setAddnewCar(addnewcar);
		main.add(addnewcar,c);
		
		tl.setBlocked(false);
		return main;
	}
	private void fillFloatingCarPnl(FloatingCarOutput s,
			JPanel floatingcarPnl, MovsimConfigContext mvCxt) {
		floatingcarPnl.removeAll();
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		for(FloatingCar fc:s.getFloatingCar()){
			JPanel p=floatingCarToPanel(fc,s, mvCxt);
			p.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Floating car", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			floatingcarPnl.add(p,c);
		}
	}
	private JPanel floatingCarToPanel(FloatingCar fc, FloatingCarOutput s,
			MovsimConfigContext mvCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		FloatingCarListener tl=new FloatingCarListener(fc,s,mvCxt);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.weightx=1;
		c.insets=new Insets(5, 5, 5, 5);
		
		
		JButton remove=new JButton("Remove this car",TDEResources.getResources().getRem());
		remove.addActionListener(tl);
		tl.setRemove(remove);
		main.add(remove,c);
		
		c.gridwidth=1;
		main.add(new JLabel("Number"),c);
		JTextField number=new JTextField(5);
		number.getDocument().addDocumentListener(tl);
		tl.setNumber(number);
		number.setText(fc.getNumber());
		c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(number,c);
		
		tl.setBlocked(false);
		return main;
	}
}
