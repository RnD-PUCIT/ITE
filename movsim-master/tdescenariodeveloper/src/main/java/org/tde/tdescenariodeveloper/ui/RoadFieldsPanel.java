package org.tde.tdescenariodeveloper.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

public class RoadFieldsPanel extends JPanel {
	RoadSegment selectedRoad;
	RoadNetwork rn;
	private JTextField tfId;
	private JTextField tfName;
	private JTextField tfLength;
	private JTextField tfJunction;
	public RoadFieldsPanel(RoadNetwork rn) {
		this.rn = rn;
		setLayout(new GridBagLayout());
		
		Insets ins=new Insets(5,5,5,5);
		JLabel lblId = new JLabel("Id");
		GridBagConstraints gbc_lbl = new GridBagConstraints();
		gbc_lbl.insets = ins;
		gbc_lbl.weightx=2;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		add(lblId, gbc_lbl);
		
		tfId = new JTextField();
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
		lblName.setLabelFor(tfName);
		add(tfName, gbc_tf);
		
		JLabel lblLength = new JLabel("Length");
		add(lblLength, gbc_lbl);
		
		tfLength = new JTextField();
		lblLength.setLabelFor(tfLength);
		add(tfLength, gbc_tf);
		
		JLabel lblJunction = new JLabel("Junction");
		add(lblJunction, gbc_lbl);
		
		tfJunction = new JTextField();
		lblJunction.setLabelFor(tfJunction);
		add(tfJunction, gbc_tf);
		
	}
	public void setSelectedRoad(RoadSegment selectedRoad) {
		this.selectedRoad = selectedRoad;
	}
	public void updateFields() {
		if(selectedRoad==null)return;
		tfId.setText(selectedRoad.getOdrRoad().getId());
		tfName.setText(selectedRoad.getRoadName());
		tfLength.setText(selectedRoad.getRoadLength()+"");
		tfJunction.setText(selectedRoad.getOdrRoad().getJunction());
	}
	public void updateFields(RoadSegment selectedRoad2) {
		selectedRoad=selectedRoad2;
		updateFields();
	}
	
}
