package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;

import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaPopupListener;

public class DrawingAreaPopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7710136231966863266L;
	RoadContext rdCxt;
	JMenuItem newlane,removeLane,removegeo,newgeo,toggleRotation,delete;
	JSlider lnWidth,sOffset,hdg,curv;
	DrawingAreaPopupListener popupListener;
	JLabel lnWidthLbl,gmsOffsetLbl,hdgLbl,curvLbl;
	
	public DrawingAreaPopupMenu(RoadContext roadPrPnl) {
		rdCxt=roadPrPnl;
		initialize();
		addListeners();
		GridBagConstraints fullRow,row1,row3;
		fullRow=new GridBagConstraints();
		row3=new GridBagConstraints();
		row1=new GridBagConstraints();
		fullRow.fill=GridBagConstraints.BOTH;
		fullRow.gridwidth=GridBagConstraints.REMAINDER;
		fullRow.insets=new Insets(5, 5, 5, 5);
		
		row1.fill=GridBagConstraints.BOTH;
		row1.weightx=1;
		row1.insets=new Insets(5, 5, 5, 5);

		row3.fill=GridBagConstraints.BOTH;
		row1.weightx=2;
		row3.gridwidth=GridBagConstraints.REMAINDER;
		row3.insets=new Insets(5, 5, 5, 5);
		
		
		setLayout(new GridBagLayout());
		add(newgeo,fullRow);
		add(toggleRotation,fullRow);
		add(delete,fullRow);
		
		add(removegeo,fullRow);
		add(new Separator(),fullRow);
		add(newlane,fullRow);
		add(removeLane,fullRow);
		
		gmsOffsetLbl=new JLabel("Start point ");
		gmsOffsetLbl.setToolTipText("Geometry's start point relative to road");
		add(gmsOffsetLbl,row1);
		add(sOffset,row3);

		hdgLbl=new JLabel("Direction");
		hdgLbl.setToolTipText("Road direction");
		add(hdgLbl,row1);
		add(hdg,row3);

		curvLbl=new JLabel("Curvature");
		curvLbl.setToolTipText("Changes radius of circle to which this arc belongs");
		add(curvLbl,row1);
		add(curv,row3);

		lnWidthLbl=new JLabel("Width ");
		lnWidthLbl.setToolTipText("Change road width");
		add(lnWidthLbl,row1);
		add(lnWidth,row3);
		
		pack();
	}

	private void addListeners() {
		DrawingAreaPopupListener listener=new DrawingAreaPopupListener(rdCxt);
		this.popupListener=listener;
		lnWidth.addChangeListener(listener);
		hdg.addChangeListener(listener);
		curv.addChangeListener(listener);
		sOffset.addChangeListener(listener);
		newgeo.addActionListener(listener);
		toggleRotation.addActionListener(listener);
		delete.addActionListener(listener);
		newlane.addActionListener(listener);
		removegeo.addActionListener(listener);
		removeLane.addActionListener(listener);
	}

	private void initialize() {
		lnWidth=new JSlider();
		hdg=new JSlider();
		curv=new JSlider();
		sOffset=new JSlider();
		
		removegeo=new JMenuItem("Remove road segment");
		newgeo=new JMenuItem("Append new road segment");
		toggleRotation=new JMenuItem("Clockwise/Counter-Clockwise");
		delete=new JMenuItem("Delete road");
		
		newlane=new JMenuItem("Add new lane");
		removeLane=new JMenuItem("Remove lane");
	}

	public JMenuItem getNewlane() {
		return newlane;
	}

	public JMenuItem getNewgeo() {
		return newgeo;
	}

	public JSlider getLaneWidthSlider() {
		return lnWidth;
	}

	public JSlider getsOffsetSlider() {
		return sOffset;
	}
	@Override
	public void show(Component c,int a,int b){
		rdCxt.blockListeners(true);
		lnWidth.setValue((int)rdCxt.getSelectedRoad().getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().get(0).getWidth().get(0).getA());
		removeLane.setEnabled(rdCxt.getLanesPnl().getOdrLanes().size()>1);
		removegeo.setEnabled(rdCxt.getGmPnl().getSelectedIndex()>0);
		sOffset.setMinimum((int)popupListener.getGmVl().getStartLimit());
		sOffset.setMaximum((int)popupListener.getGmVl().getEndLimit());
		sOffset.setValue((int)rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).getS());
		sOffset.setEnabled(rdCxt.getGmPnl().getSelectedIndex()>0);
		hdg.setMinimum(-628);
		hdg.setMaximum(628);
		hdg.setValue((int)(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).getHdg()*100.0));
		hdg.setEnabled(rdCxt.getGmPnl().getSelectedIndex()==0);
		hdgLbl.setEnabled(rdCxt.getGmPnl().getSelectedIndex()==0);
		gmsOffsetLbl.setEnabled(rdCxt.getGmPnl().getSelectedIndex()>0);
		if(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).isSetArc()){
			toggleRotation.setEnabled(true);
			curv.setMinimum(-20);
			curv.setMaximum(20);
			curv.setValue((int)(rdCxt.getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(rdCxt.getGmPnl().getSelectedIndex()).getArc().getCurvature()*2000.0));
			curv.setEnabled(true);
			curvLbl.setEnabled(true);
		}else{
			toggleRotation.setEnabled(false);
			curv.setEnabled(false);
			curvLbl.setEnabled(false);
		}
		rdCxt.blockListeners(false);
		super.show(c, a, b);
	}
	public void setBlockListener(boolean b){
		popupListener.setBlocked(b);
	}
	public JMenuItem getRemoveLane() {
		return removeLane;
	}
	public JMenuItem getRemovegeo() {
		return removegeo;
	}

	public JMenuItem getToggleRotation() {
		return toggleRotation;
	}
	public JMenuItem getDelete() {
		return delete;
	}

	public JSlider getHdg() {
		return hdg;
	}

	public JSlider getCurv() {
		return curv;
	}

}
