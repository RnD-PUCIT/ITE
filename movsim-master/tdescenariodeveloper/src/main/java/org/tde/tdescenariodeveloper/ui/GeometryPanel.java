package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMappingPoly;
import org.tde.tdescenariodeveloper.eventhandling.GeometryPanelListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class GeometryPanel extends JPanel{
	JComboBox<String>cbGeom,cbGmType;
	JTextField s,tfx,tfy,hdg,l,curvature;
	JButton add,remove;
	int gmInd=0;
	JPanel gmTypePnl;
	RoadPropertiesPanel rdPrPnl;
	GeometryPanelListener gpl;
	public GeometryPanel(RoadPropertiesPanel roadPropertiesPanel, GeometryPanelListener gpl) {
		rdPrPnl=roadPropertiesPanel;
		this.gpl=gpl;
		add=new JButton("Add new");
		add.addActionListener(gpl);
		remove=new JButton("Remove");
		remove.addActionListener(gpl);
		
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
		cbGeom.addActionListener(gpl);
		cbGmType.addActionListener(gpl);
		JLabel lbl=new JLabel("Select (s offset)");
		lbl.setLabelFor(cbGeom);
		JLabel lblGmType=new JLabel("Geometry Type");
		lblGmType.setLabelFor(cbGmType);
		add(lbl,gbc_lbl);
		add(cbGeom,gbc_tf);

		s=new JTextField(10);
		s.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("S offset");
		lbl.setLabelFor(s);
		add(lbl,gbc_lbl);
		add(s,gbc_tf);
		tfx=new JTextField(10);
		tfx.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("X coordinate");
		lbl.setLabelFor(tfx);
		add(lbl,gbc_lbl);
		add(tfx,gbc_tf);
		tfy=new JTextField(10);
		tfy.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Y coordinate");
		lbl.setLabelFor(tfy);
		add(lbl,gbc_lbl);
		add(tfy,gbc_tf);
		hdg=new JTextField(10);
		hdg.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Direction");
		lbl.setLabelFor(hdg);
		add(lbl,gbc_lbl);
		add(hdg,gbc_tf);
		l=new JTextField(10);
		l.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Length");
		lbl.setLabelFor(l);
		add(lbl,gbc_lbl);
		add(l,gbc_tf);
		
		curvature=new JTextField(10);
		curvature.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Curvature");
		lbl.setLabelFor(curvature);
		gmTypePnl.add(lbl,gbc_lbl);
		gmTypePnl.add(curvature,gbc_tf);
		
		add(lblGmType,gbc_lbl);
		add(cbGmType,gbc_tf);
	}
	public void updateGeomPanel(){
		gpl.setDocListLocked(true);
		if(rdPrPnl.getSelectedRoad()==null)return;
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),"Geometr"+(rdPrPnl.getSelectedRoad().roadMapping() instanceof RoadMappingPoly?"ies ("+rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size()+")":"y") , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		cbGeom.removeAllItems();
		for(Geometry gm:rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry()){
			cbGeom.addItem(gm.getS()+"");
		}
		geometryChanged();
		gpl.setDocListLocked(false);
	}
	public void geometryChanged(){
		gpl.setDocListLocked(true);
		makeBlackFont();
		DecimalFormat df=new DecimalFormat("##.####");
		s.setText(df.format(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getS()));
		tfx.setText(df.format(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getX()));
		tfy.setText(df.format(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getY()));
		l.setText(df.format(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getLength()));
		hdg.setText(df.format(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getHdg()));
		if(gmInd!=0){
			tfx.setEnabled(false);
			tfy.setEnabled(false);
			hdg.setEnabled(false);
		}else{
			tfx.setEnabled(true);
			tfy.setEnabled(true);
			hdg.setEnabled(true);
		}
		if(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getLine()!=null){
			if(isAdded(gmTypePnl))remove(gmTypePnl);
			cbGmType.setSelectedItem("line");
		}
		else if(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getArc()!=null){
			if(!isAdded(gmTypePnl)){
				GridBagConstraints gbc_tf = new GridBagConstraints();
				gbc_tf.fill = GridBagConstraints.BOTH;
				gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
				add(gmTypePnl,gbc_tf);
			}
			curvature.setText(rdPrPnl.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getArc().getCurvature()+"");
			cbGmType.setSelectedItem("arc");
		}
		gpl.setDocListLocked(false);
		((RoadPropertiesPanel)getParent()).updateGraphics();
	}
	private boolean isAdded(Component c){
		return ((GeometryPanel)c.getParent()==this);
	}
	public int getSelectedIndex() {
		return gmInd;
	}
	public void setSelectedGeometry(int ind) {
		if(rdPrPnl.getSelectedRoad()==null){
			throw new NullPointerException("selectedroad is null : setSelectedGeometry");
		}
		gmInd=ind;
		cbGeom.setSelectedIndex(gmInd);
	}
	public JComboBox<String> getCbGeom() {
		return cbGeom;
	}
	public int getGmInd() {
		return gmInd;
	}
	public void setGmInd(int gmInd) {
		this.gmInd = gmInd;
	}
	public JComboBox<String> getCbGmType() {
		return cbGmType;
	}
	public JTextField getS() {
		return s;
	}
	public JTextField getTfX() {
		return tfx;
	}
	public JTextField getTfY() {
		return tfy;
	}
	public JTextField getHdg() {
		return hdg;
	}
	public JTextField getL() {
		return l;
	}
	public JTextField getCurvature() {
		return curvature;
	}
	public JButton getRemove() {
		return remove;
	}
	public JPanel getGmTypePnl() {
		return gmTypePnl;
	}
	public void makeBlackFont(){
		GraphicsHelper.makeBlack(s,tfx,hdg,l,curvature);
	}
	public JButton getAdd() {
		return add;
	}
}