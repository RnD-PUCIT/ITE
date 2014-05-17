package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

class GeometryPanel extends JPanel implements ActionListener{
	RoadSegment selectedRoad;
	RoadNetwork rn;
	JComboBox<String>cbGeom,cbGmType;
	JTextField s,x,y,hdg,l,curvature;
	JButton add,remove;
	int gmInd=0;
	JPanel gmTypePnl;
	public GeometryPanel(RoadNetwork rn) {
		this.rn=rn;
		add=new JButton("Add new");
		remove=new JButton("Remove");
		
		gmTypePnl=new JPanel(new GridBagLayout());
		setMinimumSize(new Dimension(50, 50));
		setLayout(new GridBagLayout());
		Insets ins=new Insets(5,5,5,5);
		GridBagConstraints gbc_lbl = new GridBagConstraints();
		gbc_lbl.insets = ins;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		gbc_lbl.weightx=1;
		add(add,gbc_lbl);
		gbc_lbl.weightx=2;
		
		GridBagConstraints gbc_tf = new GridBagConstraints();
		gbc_tf.insets = ins;
		gbc_tf.fill = GridBagConstraints.BOTH;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		gbc_tf.weightx=1;
		add(remove,gbc_tf);
		gbc_tf.weightx=3;
		
		cbGeom=new JComboBox<>();
		cbGmType=new JComboBox<>(new String[]{"line","arc"});
		cbGeom.addActionListener(this);
		cbGmType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		JLabel lbl=new JLabel("Select (s offset)");
		lbl.setLabelFor(cbGeom);
		JLabel lblGmType=new JLabel("Geometry Type");
		lblGmType.setLabelFor(cbGmType);
		add(lbl,gbc_lbl);
		add(cbGeom,gbc_tf);

		s=new JTextField(10);
		lbl=new JLabel("S offset");
		lbl.setLabelFor(s);
		add(lbl,gbc_lbl);
		add(s,gbc_tf);
		x=new JTextField(10);
		lbl=new JLabel("X coordinate");
		lbl.setLabelFor(x);
		add(lbl,gbc_lbl);
		add(x,gbc_tf);
		y=new JTextField(10);
		lbl=new JLabel("Y coordinate");
		lbl.setLabelFor(y);
		add(lbl,gbc_lbl);
		add(y,gbc_tf);
		hdg=new JTextField(10);
		lbl=new JLabel("Direction");
		lbl.setLabelFor(hdg);
		add(lbl,gbc_lbl);
		add(hdg,gbc_tf);
		l=new JTextField(10);
		lbl=new JLabel("Length");
		lbl.setLabelFor(l);
		add(lbl,gbc_lbl);
		add(l,gbc_tf);
		
		curvature=new JTextField(10);
		lbl=new JLabel("Curvature");
		lbl.setLabelFor(curvature);
		gmTypePnl.add(lbl,gbc_lbl);
		gmTypePnl.add(curvature,gbc_tf);
		
		add(lblGmType,gbc_lbl);
		add(cbGmType,gbc_tf);
	}
	public void updateGeomPanel(){
		if(selectedRoad==null)return;
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),"Geometr"+(selectedRoad.roadMapping() instanceof RoadMappingPoly?"ies ("+selectedRoad.getOdrRoad().getPlanView().getGeometry().size()+")":"y") , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		if(rn.isModified()){
			cbGeom.removeAllItems();
			for(Geometry gm:selectedRoad.getOdrRoad().getPlanView().getGeometry()){
				cbGeom.addItem(gm.getS()+"");
			}
		}
	}
	public void updateGeomPanel(RoadSegment selectedRoad2) {
		selectedRoad=selectedRoad2;
		updateGeomPanel();
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(cbGeom.getSelectedItem()==null)return;
		gmInd=cbGeom.getSelectedIndex();
		geometryChanged();
	}
	private void geometryChanged(){
		s.setText(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getS()+"");
		x.setText(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getX()+"");
		y.setText(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getY()+"");
		l.setText(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getLength()+"");
		hdg.setText(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getHdg()+"");
		if(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getLine()!=null){
			if(isAdded(gmTypePnl))remove(gmTypePnl);
			cbGmType.setSelectedItem("line");
		}
		else if(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getArc()!=null){
			if(!isAdded(gmTypePnl)){
				GridBagConstraints gbc_tf = new GridBagConstraints();
				gbc_tf.fill = GridBagConstraints.BOTH;
				gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
				add(gmTypePnl,gbc_tf);
			}
			curvature.setText(selectedRoad.getOdrRoad().getPlanView().getGeometry().get(gmInd).getArc().getCurvature()+"");
			cbGmType.setSelectedItem("arc");
		}
		((RoadPropertiesPanel)getParent()).updateGraphics();
	}
	private boolean isAdded(Component c){
		return ((GeometryPanel)c.getParent()==this);
	}
	public int getSelectedIndex() {
		return gmInd;
	}
	public void setSelectedGeometry(int ind) {
		if(selectedRoad==null){
			throw new NullPointerException("selectedroad is null : setSelectedGeometry");
		}
		gmInd=ind;
		cbGeom.setSelectedIndex(gmInd);
	}
}