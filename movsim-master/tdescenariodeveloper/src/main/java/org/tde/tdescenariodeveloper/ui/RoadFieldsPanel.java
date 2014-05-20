package org.tde.tdescenariodeveloper.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.eventhandling.RoadFieldsPanelListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class RoadFieldsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5593310264102814959L;
	RoadSegment selectedRoad;
	private JLabel tfId;
	private JTextField tfName;
	private JLabel tfLength;
	private JComboBox<String> cbJunction;
	RoadPropertiesPanel rdPrPnl;
	public RoadFieldsPanel(RoadPropertiesPanel rpp,RoadFieldsPanelListener rfl) {
		rdPrPnl=rpp;
		setLayout(new GridBagLayout());
		Insets ins=new Insets(5,5,5,5);
		JLabel lblId = new JLabel("Id");
		GridBagConstraints gbc_lbl = new GridBagConstraints();
		gbc_lbl.insets = ins;
		gbc_lbl.weightx=2;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		add(lblId, gbc_lbl);
		
		tfId = new JLabel();
		lblId.setLabelFor(tfId);
		GridBagConstraints gbc_tf = new GridBagConstraints();
		gbc_tf.insets = ins;
		gbc_tf.fill = GridBagConstraints.BOTH;
		gbc_tf.weightx=3;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		add(tfId, gbc_tf);
		
		JLabel lblName = new JLabel("Name");
		add(lblName, gbc_lbl);
		tfName = new JTextField();
		tfName.getDocument().addDocumentListener(rfl);
		lblName.setLabelFor(tfName);
		add(tfName, gbc_tf);
		
		JLabel lblLength = new JLabel("Length");
		add(lblLength, gbc_lbl);
		
		tfLength = new JLabel();
		lblLength.setLabelFor(tfLength);
		add(tfLength, gbc_tf);
		
		JLabel lblJunction = new JLabel("Junction");
		add(lblJunction, gbc_lbl);
		
		cbJunction = new JComboBox<String>();
		cbJunction.addActionListener(rfl);
		lblJunction.setLabelFor(cbJunction);
		add(cbJunction, gbc_tf);
		
	}
	public void setSelectedRoad(RoadSegment selectedRoad) {
		this.selectedRoad = selectedRoad;
	}
	public void updateFields() {
		if(selectedRoad==null)return;
		tfId.setText(selectedRoad.getOdrRoad().getId());
		tfName.setText(selectedRoad.getRoadName());
		tfLength.setText(selectedRoad.getRoadLength()+"");
		String jnc=selectedRoad.getOdrRoad().getJunction();
		if(jnc.equals("-1"))jnc="None";
		String[]jncs=new String[rdPrPnl.getRn().getOdrNetwork().getJunction().size()];
		for(int i=0;i<jncs.length;i++){
			jncs[i]=rdPrPnl.getRn().getOdrNetwork().getJunction().get(i).getId();
		}
		cbJunction.removeAllItems();
		cbJunction.addItem("None");
		for(String s:jncs)cbJunction.addItem(s);
		cbJunction.setSelectedItem(jnc);
	}
	public void updateFields(RoadSegment selectedRoad2) {
		selectedRoad=selectedRoad2;
		updateFields();
	}
	public RoadSegment getSelectedRoad() {
		return selectedRoad;
	}
	public RoadNetwork getRn() {
		return rdPrPnl.getRn();
	}
	public JLabel getTfId() {
		return tfId;
	}
	public JTextField getTfName() {
		return tfName;
	}
	public JComboBox<String> getCbJunction() {
		return cbJunction;
	}
	
}
