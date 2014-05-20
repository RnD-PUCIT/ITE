package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

public class LinkPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 76435681L;
	RoadSegment selectedRoad;
	private JComboBox<String> cbElementType;
	private JComboBox<String> cbElementId;
	private JComboBox<String> cbContactPoint;
	private JComboBox<String> cbSelectLink;
	GridBagConstraints c,gbc_lbl,gbc_tf;
	RoadPropertiesPanel rdPrPnl;
	public LinkPanel(RoadPropertiesPanel rpp) {
		rdPrPnl=rpp;
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Link", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		cbSelectLink = new JComboBox<>(new String[]{"Predecessor","Successor"});
		cbSelectLink.setToolTipText("Predecessor/Sucsessor road");
		cbSelectLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateLinkPanel();
			}
		});
		c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTHWEST;
		c.fill=GridBagConstraints.BOTH;
		gbc_lbl = new GridBagConstraints();
		Insets ins=new Insets(5,5,5,5);
		gbc_lbl.insets = ins;
		gbc_lbl.weightx=2;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		
		gbc_tf = new GridBagConstraints();
		gbc_tf.insets = ins;
		gbc_tf.fill = GridBagConstraints.BOTH;
		gbc_tf.weightx=3;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		
		
		add(cbSelectLink,c);
		JLabel lblElementId = new JLabel("Element id");
		add(lblElementId,gbc_lbl);
		cbElementId = new JComboBox<>();
		lblElementId.setLabelFor(cbElementId);
		cbElementId.setToolTipText("Id of predecessor/successor road");
		cbElementId.setRequestFocusEnabled(false);
		cbElementId.setFocusable(false);
		cbElementId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(cbElementId,gbc_tf);
		JLabel lblType = new JLabel("Element type");
		add(lblType,gbc_lbl);
		cbElementType = new JComboBox<>(new String[]{"road","junction"});
		cbElementType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		lblType.setLabelFor(cbElementType);
		add(cbElementType,gbc_tf);
		JLabel lblContactPoint = new JLabel("Contact Point");
		add(lblContactPoint,gbc_lbl);
		cbContactPoint = new JComboBox<>(new String[]{"start","end"});
		cbContactPoint.setToolTipText("Where the road should be attached");
		cbContactPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(cbContactPoint,gbc_tf);
	}

	public void updateLinkPanel() {
		if(selectedRoad==null){
			JOptionPane.showMessageDialog(null, "Selected road is null");
			return;
		}
		int curLinkSelected = cbSelectLink.getSelectedIndex();
		if (rdPrPnl.getRn().isModified()) {
			cbElementId.removeAllItems();
			for (RoadSegment rs : rdPrPnl.getRn())
				cbElementId.addItem(rs.getOdrRoad().getId() + "");
			for (Junction jc : rdPrPnl.getRn().getOdrNetwork().getJunction())
				cbElementId.addItem(jc.getId() + "");
		}
		switch (curLinkSelected) {
		case 0:
			if (selectedRoad.getOdrRoad().getLink().getPredecessor()!=null) {
				setLinkFields("Predecessor");
				break;
			}
		case 1:
			if (selectedRoad.getOdrRoad().getLink().getSuccessor()!=null) {
				setLinkFields("Successor");
				break;
			}
		default:
			if (selectedRoad.getOdrRoad().getLink().getPredecessor()!=null)
				setLinkFields("Predecessor");
			else
				setLinkFields("Successor");
		}
	}
	public void setSelectedRoad(RoadSegment rs){
		this.selectedRoad=rs;
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
}
