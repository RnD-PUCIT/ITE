package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

public class LanesPanel extends JPanel implements ActionListener{
	RoadNetwork rn;
	RoadSegment selectedRoad;
	JTextField width,maxSpeed;
	JLabel level;
	JComboBox<String>cbLanes,cbtype;
	JButton add,remove;
	LaneLinkPanel lnLinkPnl;
	private int lnInd=0;
	public LanesPanel(RoadNetwork rn) {
		this.rn=rn;
		lnLinkPnl=new LaneLinkPanel(rn);
		lnLinkPnl.setLanePanel(this);
		setLayout(new GridBagLayout());
		Insets ins=new Insets(5,5,5,5);
		GridBagConstraints gbc_lbl = new GridBagConstraints();
		gbc_lbl.insets = ins;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		gbc_lbl.weightx=1;
		add=new JButton("Add new");
		remove=new JButton("Remove");
		add(add,gbc_lbl);
		gbc_lbl.weightx=2;
		
		GridBagConstraints gbc_tf = new GridBagConstraints();
		gbc_tf.insets = ins;
		gbc_tf.fill = GridBagConstraints.BOTH;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		gbc_tf.weightx=1;
		add(remove,gbc_tf);
		gbc_tf.weightx=3;
		
		
		
		
		cbLanes=new JComboBox<String>();
		cbLanes.addActionListener(this);
		JLabel lbl=new JLabel("Select lane (Id)");
		lbl.setLabelFor(cbLanes);
		add(lbl,gbc_lbl);
		add(cbLanes,gbc_tf);
		
		cbtype=new JComboBox<>(new String[]{"driving","mwyExit","mwyEntry"});
		lbl=new JLabel("Type");
		lbl.setLabelFor(cbtype);
		add(lbl,gbc_lbl);
		add(cbtype,gbc_tf);
		
		level=new JLabel();
		lbl=new JLabel("Level");
		lbl.setLabelFor(level);
		add(lbl,gbc_lbl);
		add(level,gbc_tf);
		
		width=new JTextField(10);
		lbl=new JLabel("Width");
		lbl.setLabelFor(width);
		add(lbl,gbc_lbl);
		add(width,gbc_tf);
		
		maxSpeed=new JTextField(10);
		lbl=new JLabel("Max speed");
		lbl.setLabelFor(maxSpeed);
		add(lbl,gbc_lbl);
		add(maxSpeed,gbc_tf);
	}
	public void updateLanesPanel(RoadSegment selectedRoad) {
		this.selectedRoad = selectedRoad;
		updatelanesPanel();
	}
	public void updatelanesPanel() {
		if(selectedRoad==null)return;
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),"Lane"+(selectedRoad.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size()>1?"s ("+selectedRoad.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size()+")":"") , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		if(rn.isModified()){
			cbLanes.removeAllItems();
			for(Lane ln:selectedRoad.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane()){
				cbLanes.addItem(ln.getId()+"");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(cbLanes.getSelectedItem()==null)return;
		lnInd=cbLanes.getSelectedIndex();
		laneChanged();
	}
	private void laneChanged() {
		Lane ln=selectedRoad.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().get(lnInd);
		cbtype.setSelectedItem(ln.getType()+"");
		level.setText(ln.getLevel()+"");
		width.setText(ln.getWidth().get(0).getA()+"");
		maxSpeed.setText(ln.getSpeed()!=null && ln.getSpeed().size()>0?ln.getSpeed().get(0).getMax()+"":"");
		if(ln.getLink()!=null){
			if(!isAdded(lnLinkPnl)){
				GridBagConstraints gbc_tf = new GridBagConstraints();
				gbc_tf.fill = GridBagConstraints.BOTH;
				gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
				add(lnLinkPnl,gbc_tf);
			}
			lnLinkPnl.updateLinkPanel();
		}else{
			if(isAdded(lnLinkPnl))remove(lnLinkPnl);
		}
		((RoadPropertiesPanel)getParent()).updateGraphics();
		
	}
	public RoadSegment getSelectedRoad() {
		return selectedRoad;
	}
	public int getLnInd() {
		return lnInd;
	}
	public boolean isAdded(Component c){
		return (LanesPanel)c.getParent()==this;
	}
	
}
