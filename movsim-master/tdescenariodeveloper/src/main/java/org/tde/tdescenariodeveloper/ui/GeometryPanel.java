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
	JPanel arcTypePnl;
	RoadContext rdCxt;
	GeometryPanelListener gpl;
	public GeometryPanel(RoadContext roadPropertiesPanel, GeometryPanelListener gpl) {
		rdCxt=roadPropertiesPanel;
		this.gpl=gpl;
		add=new JButton("Add new");
		add.addActionListener(gpl);
		remove=new JButton("Remove");
		remove.addActionListener(gpl);
		
		arcTypePnl=new JPanel(new GridBagLayout());
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
		cbGmType=new JComboBox<>();
		cbGeom.addActionListener(gpl);
		cbGmType.addActionListener(gpl);
		JLabel lbl=new JLabel("Select (s offset)");
		lbl.setLabelFor(cbGeom);
		JLabel lblGmType=new JLabel("Geometry Type");
		lblGmType.setLabelFor(cbGmType);
		add(lbl,gbc_lbl);
		add(cbGeom,gbc_tf);

		s=new JTextField(10);
		s.setHighlighter(null);
		s.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("S offset");
		lbl.setLabelFor(s);
		add(lbl,gbc_lbl);
		add(s,gbc_tf);
		tfx=new JTextField(10);
		tfx.setHighlighter(null);
		tfx.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("X coordinate");
		lbl.setLabelFor(tfx);
		add(lbl,gbc_lbl);
		add(tfx,gbc_tf);
		tfy=new JTextField(10);
		tfy.setHighlighter(null);
		tfy.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Y coordinate");
		lbl.setLabelFor(tfy);
		add(lbl,gbc_lbl);
		add(tfy,gbc_tf);
		hdg=new JTextField(10);
		hdg.setHighlighter(null);
		hdg.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Direction");
		lbl.setLabelFor(hdg);
		add(lbl,gbc_lbl);
		add(hdg,gbc_tf);
		l=new JTextField(10);
		l.setHighlighter(null);
		l.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Length");
		lbl.setLabelFor(l);
		add(lbl,gbc_lbl);
		add(l,gbc_tf);
		
		curvature=new JTextField(10);
		curvature.setHighlighter(null);
		curvature.getDocument().addDocumentListener(gpl);
		lbl=new JLabel("Curvature");
		lbl.setLabelFor(curvature);
		arcTypePnl.add(lbl,gbc_lbl);
		arcTypePnl.add(curvature,gbc_tf);
		
		add(lblGmType,gbc_lbl);
		add(cbGmType,gbc_tf);
	}
	public void updateGeomPanel(){
		gpl.setDocListLocked(true);
		if(rdCxt.getSelectedRoad()==null)return;
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),"Geometr"+(rdCxt.getSelectedRoad().roadMapping() instanceof RoadMappingPoly?"ies ("+rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size()+")":"y") , TitledBorder.LEADING, TitledBorder.TOP, null, null));
		cbGeom.removeAllItems();
		for(Geometry gm:rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry()){
			cbGeom.addItem(gm.getS()+"");
		}
		cbGeom.setSelectedIndex(gmInd);
		geometryChanged();
		gpl.setDocListLocked(false);
	}
	public void geometryChanged(){
		cbGmType.removeAllItems();
		cbGmType.addItem("Line");
		cbGmType.addItem("Arc");
		gpl.setDocListLocked(true);
		makeBlackFont();
		DecimalFormat df=new DecimalFormat("##.####");
		if(gmInd+1>rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size())
			gmInd=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size()-1;
		s.setText(df.format(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getS()));
		tfx.setText(df.format(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getX()));
		tfy.setText(df.format(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getY()));
		l.setText(df.format(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getLength()));
		hdg.setText(df.format(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getHdg()));
		if(gmInd!=0){
			tfx.setEditable(false);
			tfy.setEditable(false);
			hdg.setEditable(false);
		}else{
			tfx.setEditable(true);
			tfy.setEditable(true);
			hdg.setEditable(true);
		}
		if(rdCxt.getSelectedRoad().roadMapping() instanceof RoadMappingPoly && gmInd>0){
			remove.setEnabled(true);
		}else{
			remove.setEnabled(false);
		}
		if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).isSetLine()){
			if(isAdded(arcTypePnl))remove(arcTypePnl);
			cbGmType.setSelectedItem("line");
		}
		else if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getArc()!=null){
			if(!isAdded(arcTypePnl)){
				GridBagConstraints gbc_tf = new GridBagConstraints();
				gbc_tf.fill = GridBagConstraints.BOTH;
				gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
				add(arcTypePnl,gbc_tf);
			}
			
			curvature.setText(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(gmInd).getArc().getCurvature()+"");
			cbGmType.setSelectedItem("arc");
		}
		gpl.setDocListLocked(false);
		((RoadContext)getParent()).updateGraphics();
	}
	private boolean isAdded(Component c){
		return ((GeometryPanel)c.getParent()==this);
	}
	public int getSelectedIndex() {
		return gmInd;
	}
	public void setSelectedGeometry(int ind,boolean update) {
		gmInd=ind;
//		if(cbGeom.getItemCount()!=rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().size())throw new IllegalStateException("Geometry Combobox count not equals road geometries count.");
		if(update)cbGeom.setSelectedIndex(gmInd);
	}
	public void setSelectedGeometry(int ind) {
		setSelectedGeometry(ind,true);
	}
	public void resetSelectedIndex(){
		gmInd=0;
	}
	public JComboBox<String> getCbGeom() {
		return cbGeom;
	}
	public int getGmInd() {
		return gmInd;
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
	public JPanel getarcTypePnl() {
		return arcTypePnl;
	}
	public void makeBlackFont(){
		GraphicsHelper.makeBlack(s,tfx,hdg,l,curvature);
	}
	public JButton getAdd() {
		return add;
	}
	public void reset() {
		cbGeom.removeAllItems();
		cbGmType.removeAllItems();
		s.setText("");
		tfx.setText("");
		tfy.setText("");
		hdg.setText("");
		l.setText("");
	}
}