package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
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
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

class GeometryPanel extends JPanel implements ActionListener{
	RoadSegment selectedRoad;
	RoadNetwork rn;
	JComboBox<String>cbGeom;
	JTextField s,x,y,hdg,l;
	JButton add;
	int gmInd=0;
	public GeometryPanel(RoadNetwork rn) {
		this.rn=rn;
		setMinimumSize(new Dimension(50, 50));
		setLayout(new GridBagLayout());
		Insets ins=new Insets(5,5,5,5);
		GridBagConstraints gbc_lbl = new GridBagConstraints();
		gbc_lbl.insets = ins;
		gbc_lbl.weightx=2;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		
		GridBagConstraints gbc_tf = new GridBagConstraints();
		gbc_tf.insets = ins;
		gbc_tf.fill = GridBagConstraints.BOTH;
		gbc_tf.weightx=3;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		cbGeom=new JComboBox<>();
		cbGeom.addActionListener(this);
		JLabel lbl=new JLabel("Select (s offset)");
		lbl.setLabelFor(cbGeom);
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
		add=new JButton("Add new");
		add(add,gbc_tf);
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
		((RoadPropertiesPanel)getParent()).updateGraphics();
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