package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.autogen.AccelerationModelType;
import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.autogen.VehiclePrototypes;
import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.simulator.vehicles.VehicleType;
import org.tde.tdescenariodeveloper.eventhandling.LaneChangeModelListener;
import org.tde.tdescenariodeveloper.eventhandling.ModelParamTextFieldListener;
import org.tde.tdescenariodeveloper.eventhandling.ModelSelectorListener;
import org.tde.tdescenariodeveloper.eventhandling.PrototypesListener;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;

/**
 * This class is used to represent information of tag {@link VehiclePrototypeConfiguration}. It also shows information about driving models and lane change models
 * @author Shmeel
 * @see VehiclePrototypeConfiguration
 * @see Vehicle
 * @see VehiclePrototypes
 * @see VehicleType
 */
public class VehiclePrototypesPanel extends JPanel {
	GridBagConstraints gbcV=new GridBagConstraints();
	private static final long serialVersionUID = 2416945240385622157L;
	String[]mdls={"IDM","ACC","Gipps","Krauss","Newell","NSM"};
	MovsimConfigContext mvCxt;
	JButton addPrototypeConfig;
	JPanel prPnl;
	/**
	 * 
	 * @param movsimConfigPane contains reference to loaded .xodr file and other panels added to it
	 */
	public VehiclePrototypesPanel(MovsimConfigContext movsimConfigPane) {
		GridBagConstraints gbcH=new GridBagConstraints();
		gbcV = new GridBagConstraints();
		gbcV.insets = new Insets(2, 15, 2, 15);
		gbcV.fill=GridBagConstraints.BOTH;
		gbcV.weightx=1;
		gbcV.gridwidth=GridBagConstraints.REMAINDER;
		this.mvCxt=movsimConfigPane;
		setLayout(new GridBagLayout());
		addPrototypeConfig=new JButton("<html><i>New prototype</i></html>",TDEResources.getResources().getAddIcon());
		addPrototypeConfig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VehiclePrototypeConfiguration vp=MovsimScenario.getVehiclePrototypeConfiguration();
				vp.setLabel(DataToViewerConverter.getUniquePrototypeLabel(mvCxt));
				mvCxt.getMovsim().getVehiclePrototypes().getVehiclePrototypeConfiguration().add(vp);
				mvCxt.updatePanels();
			}
		});
		prPnl=new JPanel(new GridBagLayout());
		prPnl.setOpaque(false);
		gbcH.gridwidth=GridBagConstraints.REMAINDER;
		gbcH.weightx=1;
		gbcH.weighty=1;
		gbcH.fill=GridBagConstraints.BOTH;
		add(prPnl,gbcH);
		
		gbcH=new GridBagConstraints();
		gbcH.gridwidth=GridBagConstraints.REMAINDER;
		gbcH.insets=new Insets(10, 10,10, 10);
		gbcH.anchor=GridBagConstraints.NORTH;
		gbcH.fill=GridBagConstraints.HORIZONTAL;
		add(addPrototypeConfig,gbcH);
	}
	/**
	 * updates this {@link VehiclePrototypesPanel} from memory
	 */
	public void updatePanel(){
		prPnl.removeAll();
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		mvCxt.setTitleAt(1, "Vehicle Prototypes ("+mvCxt.getMovsim().getVehiclePrototypes().getVehiclePrototypeConfiguration().size()+")");
		gbc.weightx=1;
		gbc.weighty=1;
		gbc.insets=new Insets(10, 10,10, 10);
		gbc.fill=GridBagConstraints.BOTH;
		prPnl.add(prototypeConfigToPanel(mvCxt.getMovsim().getVehiclePrototypes().getVehiclePrototypeConfiguration().get(0),false),gbc);
		for (int i = 1; i < mvCxt.getMovsim().getVehiclePrototypes()
				.getVehiclePrototypeConfiguration().size(); i++) {
			prPnl.add(prototypeConfigToPanel(mvCxt.getMovsim()
					.getVehiclePrototypes().getVehiclePrototypeConfiguration()
					.get(i), true),gbc);
		}
	}
	/**
	 * Converts {@link VehiclePrototypeConfiguration} to {@link JPanel}
	 * @param prCn {@link VehiclePrototypeConfiguration} to be converted
	 * @param removable if true this {@link VehiclePrototypeConfiguration} can be removed
	 * @return {@link JPanel}
	 */
	public JPanel prototypeConfigToPanel(final VehiclePrototypeConfiguration prCn,boolean removable) {
		JPanel main=new JPanel(new GridBagLayout());
		main.setOpaque(false);
		main.setBorder(new LineBorder(Color.DARK_GRAY, 2));
		JPanel fields=new JPanel(new GridBagLayout());
		fields.setOpaque(false);
		JPanel longitudenalModel=new JPanel(new GridBagLayout());
		longitudenalModel.setOpaque(false);
		JPanel laneChangeModel=new JPanel(new GridBagLayout());
		laneChangeModel.setOpaque(false);
		Font f=new Font(null,Font.ITALIC,13);
		fields.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1, true),"Properties" , TitledBorder.LEADING, TitledBorder.TOP, f, Color.DARK_GRAY));
		longitudenalModel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1, true),"Driving Models", TitledBorder.LEADING, TitledBorder.TOP, f, Color.DARK_GRAY));
		laneChangeModel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1, true),"Lane Change Model", TitledBorder.LEADING, TitledBorder.TOP, f, Color.DARK_GRAY));
		PrototypesListener ptl=new PrototypesListener(mvCxt);
		ptl.setVpc(prCn);
		if(removable){
			gbcV.gridwidth=GridBagConstraints.REMAINDER;
			JButton remove=new JButton("Remove prototype",TDEResources.getResources().getRem());
			remove.setToolTipText("Remove prototype: "+prCn.getLabel());
			remove.addActionListener(ptl);
			ptl.setRemovePrototype(remove);
			main.add(remove,gbcV);
		}
		gbcV.gridwidth=1;
		JLabel lbl=new JLabel("Prototype name");
		fields.add(lbl,gbcV);
		JTextField label=new JTextField(10);
		label.getDocument().addDocumentListener(ptl);
		ptl.setLabel(label);
		gbcV.gridwidth=GridBagConstraints.REMAINDER;
		label.setText(prCn.getLabel());
		fields.add(label,gbcV);

		gbcV.gridwidth=1;
		lbl=new JLabel("Vehicle specifications:");
		lbl.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,12));
		fields.add(lbl,gbcV);
		JLabel empty=new JLabel();
		gbcV.gridwidth=GridBagConstraints.REMAINDER;
		fields.add(empty,gbcV);
		
		lbl=new JLabel("Vehicle length");
		gbcV.gridwidth=1;
		fields.add(lbl,gbcV);
		JTextField lengthTf=new JTextField(10);
		lengthTf.getDocument().addDocumentListener(ptl);
		ptl.setLengthTf(lengthTf);
		gbcV.gridwidth=GridBagConstraints.REMAINDER;
		lengthTf.setText(prCn.isSetLength()?prCn.getLength()+"":"");
		fields.add(lengthTf,gbcV);
		
		lbl=new JLabel("Vehicle width");
		gbcV.gridwidth=1;
		fields.add(lbl,gbcV);
		JTextField widthTf=new JTextField(10);
		widthTf.getDocument().addDocumentListener(ptl);
		ptl.setWidthTf(widthTf);
		gbcV.gridwidth=GridBagConstraints.REMAINDER;
		widthTf.setText(prCn.isSetWidth()?prCn.getWidth()+"":"");
		fields.add(widthTf,gbcV);
		
		lbl=new JLabel("Max. Deceleration");
		gbcV.gridwidth=1;
		fields.add(lbl,gbcV);
		JTextField maxDec=new JTextField(10);
		maxDec.getDocument().addDocumentListener(ptl);
		ptl.setMaxDec(maxDec);
		gbcV.gridwidth=GridBagConstraints.REMAINDER;
		maxDec.setText(prCn.isSetMaximumDeceleration()?prCn.getMaximumDeceleration()+"":"");
		fields.add(maxDec,gbcV);
		gbcV.weighty=1;
		fields.add(new JLabel(),gbcV);
		gbcV.weighty=0;
		
		
		JComboBox<String>cbselectMdl=new JComboBox<>(mdls);
		ModelSelectorListener msl=new ModelSelectorListener(mvCxt,prCn.getAccelerationModelType());
		msl.setSrc(cbselectMdl);
		cbselectMdl.addActionListener(msl);
		longitudenalModel.add(cbselectMdl,gbcV);
		if(prCn.getAccelerationModelType().isSetModelParameterACC()){
			cbselectMdl.setSelectedItem("ACC");
		}else if(prCn.getAccelerationModelType().isSetModelParameterGipps()){
			cbselectMdl.setSelectedItem("Gipps");
		}else if(prCn.getAccelerationModelType().isSetModelParameterIDM()){
			cbselectMdl.setSelectedItem("IDM");
		}else if(prCn.getAccelerationModelType().isSetModelParameterKrauss()){
			cbselectMdl.setSelectedItem("Krauss");
		}else if(prCn.getAccelerationModelType().isSetModelParameterNewell()){
			cbselectMdl.setSelectedItem("Newell");
		}else if(prCn.getAccelerationModelType().isSetModelParameterNSM()){
			cbselectMdl.setSelectedItem("NSM");
		}
		msl.setBlocked(false);
		ptl.setBlocked(false);
		
		HashMap<String, JTextField>hm=getParameters(prCn.getAccelerationModelType());
		Set<String> keys=hm.keySet();
		for(String key:keys){
			lbl=new JLabel(key);
			gbcV.gridwidth=1;
			longitudenalModel.add(lbl,gbcV);
			gbcV.gridwidth=GridBagConstraints.REMAINDER;
			longitudenalModel.add(hm.get(key),gbcV);
		}
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridheight=GridBagConstraints.REMAINDER;
		gbc.weighty=1;
		gbc.weightx=1;
		gbc.insets=new Insets(2, 15, 2, 15);
		gbc.fill=GridBagConstraints.BOTH;
		main.add(fields,gbc);
		main.add(longitudenalModel,gbc);
		
		if(prCn.isSetLaneChangeModelType() && prCn.getLaneChangeModelType().isSetModelParameterMOBIL()){
			JButton removeLaneMdl=new JButton("Remove",TDEResources.getResources().getRem());
			removeLaneMdl.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					prCn.setLaneChangeModelType(null);
					mvCxt.revalidate();
					mvCxt.updatePanels();
				}
			});
			laneChangeModel.add(removeLaneMdl,gbcV);
			
			LaneChangeModelListener lcml=new LaneChangeModelListener(mvCxt,prCn.getLaneChangeModelType());
			lbl=new JLabel("Safe deceleration");
			gbcV.gridwidth=1;
			laneChangeModel.add(lbl,gbcV);
			JTextField safeDec=new JTextField(10);
			safeDec.getDocument().addDocumentListener(lcml);
			lcml.setSafeDec(safeDec);
			gbcV.gridwidth=GridBagConstraints.REMAINDER;
			safeDec.setText(prCn.getLaneChangeModelType().getModelParameterMOBIL().isSetSafeDeceleration()?prCn.getLaneChangeModelType().getModelParameterMOBIL().getSafeDeceleration()+"":"");
			laneChangeModel.add(safeDec,gbcV);

			lbl=new JLabel("Minimum gap");
			gbcV.gridwidth=1;
			laneChangeModel.add(lbl,gbcV);
			JTextField minGap=new JTextField(10);
			minGap.getDocument().addDocumentListener(lcml);
			lcml.setMinGap(minGap);
			gbcV.gridwidth=GridBagConstraints.REMAINDER;
			minGap.setText(prCn.getLaneChangeModelType().getModelParameterMOBIL().isSetMinimumGap()?prCn.getLaneChangeModelType().getModelParameterMOBIL().getMinimumGap()+"":"");
			laneChangeModel.add(minGap,gbcV);
			
			lbl=new JLabel("Threshold acceleration");
			gbcV.gridwidth=1;
			laneChangeModel.add(lbl,gbcV);
			JTextField thAc=new JTextField(10);
			thAc.getDocument().addDocumentListener(lcml);
			lcml.setThAc(thAc);
			gbcV.gridwidth=GridBagConstraints.REMAINDER;
			thAc.setText(prCn.getLaneChangeModelType().getModelParameterMOBIL().isSetThresholdAcceleration()?prCn.getLaneChangeModelType().getModelParameterMOBIL().getThresholdAcceleration()+"":"");
			laneChangeModel.add(thAc,gbcV);
			
			lbl=new JLabel("Right bias acceleration");
			gbcV.gridwidth=1;
			laneChangeModel.add(lbl,gbcV);
			JTextField rba=new JTextField(10);
			rba.getDocument().addDocumentListener(lcml);
			lcml.setRba(rba);
			gbcV.gridwidth=GridBagConstraints.REMAINDER;
			rba.setText(prCn.getLaneChangeModelType().getModelParameterMOBIL().isSetRightBiasAcceleration()?prCn.getLaneChangeModelType().getModelParameterMOBIL().getRightBiasAcceleration()+"":"");
			laneChangeModel.add(rba,gbcV);
			
			lbl=new JLabel("Politeness");
			gbcV.gridwidth=1;
			laneChangeModel.add(lbl,gbcV);
			JTextField pl=new JTextField(10);
			pl.getDocument().addDocumentListener(lcml);
			lcml.setPlt(pl);
			gbcV.gridwidth=GridBagConstraints.REMAINDER;
			pl.setText(prCn.getLaneChangeModelType().getModelParameterMOBIL().isSetPoliteness()?prCn.getLaneChangeModelType().getModelParameterMOBIL().getPoliteness()+"":"");
			laneChangeModel.add(pl,gbcV);
			lcml.setBlocked(false);
		}else{
			JButton addLaneMdl=new JButton("Set lane change model - MOBIL",TDEResources.getResources().getAddIcon());
			addLaneMdl.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					prCn.setLaneChangeModelType(MovsimScenario.getLaneChangeModelType());
					mvCxt.updatePanels();
				}
			});
			laneChangeModel.add(addLaneMdl,gbcV);
		}
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(laneChangeModel,gbc);
		
		return main;
	}
	/**
	 *  used to get parameters of model selected
	 * @param accT {@link AccelerationModelType}
	 * @return {@link HashMap} <String,JtextField> key value pairs where String becomes label of related {@link JTextField}
	 */
	public HashMap<String, JTextField> getParameters(final AccelerationModelType accT){
		HashMap<String, JTextField>hm=new HashMap<>();
		if(accT.isSetModelParameterACC()){
			putACCData(hm,accT);
		}else if(accT.isSetModelParameterGipps()){
			putGippsData(hm, accT);
		}else if(accT.isSetModelParameterIDM()){
			putIDMData(hm, accT);
		}else if(accT.isSetModelParameterKrauss()){
			putKrauss(hm,accT);
		}else if(accT.isSetModelParameterNewell()){
			putNewell(hm,accT);
		}else if(accT.isSetModelParameterNSM()){
			putNSM(hm,accT);
		}
		return hm;
	}
	private void putNSM(HashMap<String, JTextField> hm,
			AccelerationModelType accT) {
		JTextField tf=new JTextField(10);
		tf.setText(accT.getModelParameterNSM().isSetV0()?accT.getModelParameterNSM().getV0()+"":"");
		tf.setName("v0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("v0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterNSM().isSetS0()?accT.getModelParameterNSM().getS0()+"":"");
		tf.setName("s0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s0", tf);

		tf=new JTextField(10);
		tf.setText(accT.getModelParameterNSM().isSetPSlowdown()?accT.getModelParameterNSM().getPSlowdown()+"":"");
		tf.setName("pSlowDown");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("pSlowDown", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterNSM().isSetPSlowStart()?accT.getModelParameterNSM().getPSlowStart()+"":"");
		tf.setName("pSlowStart");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("pSlowStart",tf);
	}
	private void putNewell(HashMap<String, JTextField> hm,
			AccelerationModelType accT) {
		JTextField tf=new JTextField(10);
		tf.setText(accT.getModelParameterNewell().isSetV0()?accT.getModelParameterNewell().getV0()+"":"");
		tf.setName("v0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("v0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterNewell().isSetS0()?accT.getModelParameterNewell().getS0()+"":"");
		tf.setName("s0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s0", tf);
	}
	private void putKrauss(HashMap<String, JTextField> hm,
			AccelerationModelType accT) {
		JTextField tf=new JTextField(10);
		tf.setText(accT.getModelParameterKrauss().isSetV0()?accT.getModelParameterKrauss().getV0()+"":"");
		tf.setName("v0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("v0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterKrauss().isSetS0()?accT.getModelParameterKrauss().getS0()+"":"");
		tf.setName("s0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterKrauss().isSetA()?accT.getModelParameterKrauss().getA()+"":"");
		tf.setName("a");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("a", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterKrauss().isSetB()?accT.getModelParameterKrauss().getB()+"":"");
		tf.setName("b");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("b", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterKrauss().isSetB()?accT.getModelParameterKrauss().getB()+"":"");
		tf.setName("Epsilon");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("Epsilon", tf);
	}
	private void putGippsData(HashMap<String, JTextField> hm,
			AccelerationModelType accT) {
		JTextField tf=new JTextField(10);
		tf.setText(accT.getModelParameterGipps().isSetV0()?accT.getModelParameterGipps().getV0()+"":"");
		tf.setName("v0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("v0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterGipps().isSetS0()?accT.getModelParameterGipps().getS0()+"":"");
		tf.setName("s0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterGipps().isSetA()?accT.getModelParameterGipps().getA()+"":"");
		tf.setName("a");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("a", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterGipps().isSetB()?accT.getModelParameterGipps().getB()+"":"");
		tf.setName("b");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("b", tf);
	}
	private void putACCData(HashMap<String, JTextField> hm,
			AccelerationModelType accT) {
		JTextField tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetV0()?accT.getModelParameterACC().getV0()+"":"");
		tf.setName("v0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("v0", tf);

		tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetS0()?accT.getModelParameterACC().getS0()+"":"");
		tf.setName("s0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetS1()?accT.getModelParameterACC().getS1()+"":"");
		tf.setName("s1");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s1", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetT()?accT.getModelParameterACC().getT()+"":"");
		tf.setName("T");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("T", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetA()?accT.getModelParameterACC().getA()+"":"");
		tf.setName("a");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("a", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetB()?accT.getModelParameterACC().getB()+"":"");
		tf.setName("b");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("b", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetDelta()?accT.getModelParameterACC().getDelta()+"":"");
		tf.setName("Delta");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("Delta", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterACC().isSetCoolness()?accT.getModelParameterACC().getCoolness()+"":"");
		tf.setName("Coolness");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("Coolness", tf);
	}
	private void putIDMData(HashMap<String, JTextField> hm,
			AccelerationModelType accT) {
		JTextField tf=new JTextField(10);
		tf.setText(accT.getModelParameterIDM().isSetV0()?accT.getModelParameterIDM().getV0()+"":"");
		tf.setName("v0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("v0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterIDM().isSetS0()?accT.getModelParameterIDM().getS0()+"":"");
		tf.setName("s0");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s0", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterIDM().isSetS1()?accT.getModelParameterIDM().getS1()+"":"");
		tf.setName("s1");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("s1", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterIDM().isSetT()?accT.getModelParameterIDM().getT()+"":"");
		tf.setName("T");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("T", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterIDM().isSetA()?accT.getModelParameterIDM().getA()+"":"");
		tf.setName("a");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("a", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterIDM().isSetB()?accT.getModelParameterIDM().getB()+"":"");
		tf.setName("b");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("b", tf);
		
		tf=new JTextField(10);
		tf.setText(accT.getModelParameterIDM().isSetDelta()?accT.getModelParameterIDM().getDelta()+"":"");
		tf.setName("Delta");
		tf.getDocument().addDocumentListener(new ModelParamTextFieldListener(tf, accT));
		hm.put("Delta", tf);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
