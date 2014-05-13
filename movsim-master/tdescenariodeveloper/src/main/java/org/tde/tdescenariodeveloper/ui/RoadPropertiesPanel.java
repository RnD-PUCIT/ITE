package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

public class RoadPropertiesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1683090113017092656L;
	private JTextField tfId;
	private JTextField tfName;
	private JTextField tfLength;
	private JTextField tfJunction;
	private RoadSegment selectedRoad;
	private JComboBox cbElementType;
	private JComboBox cbElementId;
	private JComboBox cbContactPoint;
	private JComboBox cbSelectLink;
	private JPanel linkPanel;
	private RoadNetwork roadNetwork;
	public RoadPropertiesPanel(RoadNetwork rn) {
		roadNetwork=rn;
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblId = new JLabel("Id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 1;
		add(lblId, gbc_lblId);
		
		tfId = new JTextField();
		lblId.setLabelFor(tfId);
		GridBagConstraints gbc_tfId = new GridBagConstraints();
		gbc_tfId.insets = new Insets(0, 0, 5, 0);
		gbc_tfId.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfId.gridx = 1;
		gbc_tfId.gridy = 1;
		add(tfId, gbc_tfId);
		tfId.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 2;
		add(lblName, gbc_lblName);
		
		tfName = new JTextField();
		lblName.setLabelFor(tfName);
		GridBagConstraints gbc_tfName = new GridBagConstraints();
		gbc_tfName.insets = new Insets(0, 0, 5, 0);
		gbc_tfName.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfName.gridx = 1;
		gbc_tfName.gridy = 2;
		add(tfName, gbc_tfName);
		tfName.setColumns(10);
		
		JLabel lblLength = new JLabel("Length");
		GridBagConstraints gbc_lblLength = new GridBagConstraints();
		gbc_lblLength.insets = new Insets(0, 0, 5, 5);
		gbc_lblLength.gridx = 0;
		gbc_lblLength.gridy = 3;
		add(lblLength, gbc_lblLength);
		
		tfLength = new JTextField();
		lblLength.setLabelFor(tfLength);
		GridBagConstraints gbc_tfLength = new GridBagConstraints();
		gbc_tfLength.insets = new Insets(0, 0, 5, 0);
		gbc_tfLength.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfLength.gridx = 1;
		gbc_tfLength.gridy = 3;
		add(tfLength, gbc_tfLength);
		tfLength.setColumns(10);
		
		JLabel lblJunction = new JLabel("Junction");
		GridBagConstraints gbc_lblJunction = new GridBagConstraints();
		gbc_lblJunction.insets = new Insets(0, 0, 5, 5);
		gbc_lblJunction.gridx = 0;
		gbc_lblJunction.gridy = 4;
		add(lblJunction, gbc_lblJunction);
		
		tfJunction = new JTextField();
		lblJunction.setLabelFor(tfJunction);
		GridBagConstraints gbc_tfJunction = new GridBagConstraints();
		gbc_tfJunction.insets = new Insets(0, 0, 5, 0);
		gbc_tfJunction.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfJunction.gridx = 1;
		gbc_tfJunction.gridy = 4;
		add(tfJunction, gbc_tfJunction);
		tfJunction.setColumns(10);
		
		linkPanel = new JPanel();
		linkPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Link", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_linkPanel = new GridBagConstraints();
		gbc_linkPanel.gridwidth = 2;
		gbc_linkPanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_linkPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_linkPanel.gridx = 0;
		gbc_linkPanel.gridy = 5;
		if(selectedRoad!=null && selectedRoad.getOdrRoad().getLink()!=null)
			add(linkPanel, gbc_linkPanel);
		linkPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		cbSelectLink = new JComboBox(new String[]{"Predecessor","Successor"});
		cbSelectLink.setToolTipText("Predecessor/Sucsessor road");
		cbSelectLink.setRequestFocusEnabled(false);
		cbSelectLink.setMaximumSize(new Dimension(32767, 50));
		cbSelectLink.setMaximumRowCount(2);
		cbSelectLink.setFocusable(false);
		cbSelectLink.setPreferredSize(new Dimension(100,25));
		cbSelectLink.setBorder(new EmptyBorder(5,5,5,5));
		cbSelectLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateRoadLink();
			}
		});
		JLabel dummy = new JLabel("");
		linkPanel.add(cbSelectLink);
		linkPanel.add(dummy);
		JLabel lblElementId = new JLabel("Element id");
		linkPanel.add(lblElementId);
		cbElementId = new JComboBox();
		lblElementId.setLabelFor(cbElementId);
		cbElementId.setToolTipText("Id of predecessor/successor road");
		cbElementId.setRequestFocusEnabled(false);
		cbElementId.setFocusable(false);
		cbElementId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		linkPanel.add(cbElementId);
		JLabel lblType = new JLabel("Element type");
		linkPanel.add(lblType);
		cbElementType = new JComboBox(new String[]{"road","junction"});
		cbElementType.setMaximumRowCount(2);
		cbElementType.setFocusable(false);
		cbElementType.setActionCommand("elementTypeChanged");
		cbElementType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		lblType.setLabelFor(cbElementType);
		linkPanel.add(cbElementType);
		JLabel lblContactPoint = new JLabel("Contact Point");
		linkPanel.add(lblContactPoint);
		cbContactPoint = new JComboBox(new String[]{"start","end"});
		cbContactPoint.setToolTipText("Where the road should be attached");
		cbContactPoint.setFocusable(false);
		cbContactPoint.setRequestFocusEnabled(false);
		cbContactPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		linkPanel.add(cbContactPoint);
	}
	
	public void updateFields() {
		if(selectedRoad==null)return;
		tfId.setText(selectedRoad.getOdrRoad().getJunction());
		tfName.setText(selectedRoad.getRoadName());
		tfLength.setText(selectedRoad.getRoadLength()+"");
		tfJunction.setText(selectedRoad.getOdrRoad().getJunction());
	}
	public void updatePanel(){
		updateFields();
		updateRoadLink();
	}
	private boolean isAdded(Component c){
		if(c.getParent()==this)return true;
		return false;
	}
	private void updateRoadLink() {
		boolean linkAdded=isAdded(linkPanel);
		if(selectedRoad.getOdrRoad().getLink()!=null){
			if(!linkAdded){
				GridBagConstraints gbc_linkPanel = new GridBagConstraints();
				gbc_linkPanel.gridwidth = 2;
				gbc_linkPanel.anchor = GridBagConstraints.NORTHWEST;
				gbc_linkPanel.fill = GridBagConstraints.HORIZONTAL;
				gbc_linkPanel.gridx = 0;
				gbc_linkPanel.gridy = 5;
				add(linkPanel, gbc_linkPanel);
				revalidate();
			}
			boolean pred=selectedRoad.getOdrRoad().getLink().getPredecessor()!=null;
			boolean succ=selectedRoad.getOdrRoad().getLink().getSuccessor()!=null;
			if(!pred && !succ)throw new IllegalStateException("No predecessor or successor found");
			int curLinkSelected=cbSelectLink.getSelectedIndex();
			if(roadNetwork.isModified()){
				cbElementId.removeAllItems();
				for(RoadSegment rs:roadNetwork)
					cbElementId.addItem(rs.id()+"");
			}
			switch(curLinkSelected){
			case 0:
				if(pred){
					setLinkFields("Predecessor");
					break;
				}
			case 1:
				if(succ){
					setLinkFields("Successor");
					break;
				}
			default:
				if(pred)setLinkFields("Predecessor");
				else setLinkFields("Successor");
			}
		} else if (linkAdded){
			remove(linkPanel);
			revalidate();
		}
	
	}
	
	private void setLinkFields(String linkType) {
		cbSelectLink.setSelectedItem(linkType);
		String id="",type="",cntPnt="";
		if(linkType.equals("Predecessor")){
			id=selectedRoad.getOdrRoad().getLink().getPredecessor().getElementId();
			type=selectedRoad.getOdrRoad().getLink().getPredecessor().getElementType();
			cntPnt=selectedRoad.getOdrRoad().getLink().getPredecessor().getContactPoint();
		}
		else{
			id=selectedRoad.getOdrRoad().getLink().getSuccessor().getElementId();
			type=selectedRoad.getOdrRoad().getLink().getSuccessor().getElementType();
			cntPnt=selectedRoad.getOdrRoad().getLink().getSuccessor().getContactPoint();
		}
		cbElementId.setSelectedItem(id);
		cbElementType.setSelectedItem(type);
		cbContactPoint.setSelectedItem(cntPnt);
	}

	public void setSelectedRoad(RoadSegment selectedRoad) {
		if(selectedRoad==null)return;
		this.selectedRoad = selectedRoad;
		updatePanel();
	}

	public RoadSegment getSelectedRoad() {
		return selectedRoad;
	}
	
}
