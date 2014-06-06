package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.updation.JunctionsUpdater;

public class DrawingAreaPopupMenu2 extends JPopupMenu implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7710136231966863266L;
	RoadContext rdCxt;
	JMenuItem markAsJunction,unmarkAsJunction;//,removegeo,newgeo,toggleRotation;
	public DrawingAreaPopupMenu2(RoadContext roadPrPnl) {
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
		add(markAsJunction,fullRow);
		add(unmarkAsJunction,fullRow);
		pack();
	}

	private void addListeners() {
		markAsJunction.addActionListener(this);
		unmarkAsJunction.addActionListener(this);
	}

	private void initialize() {
		markAsJunction=new JMenuItem("Make part of junction");
		markAsJunction.setToolTipText("Makes all selected roads part of junction further configuration is done in Junctions editor");
		unmarkAsJunction=new JMenuItem("Unmark roads as junctions");
	}
	@Override
	public void show(Component c,int a,int b){
		super.show(c, a, b);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem src=null;
		if(e.getSource() instanceof JMenuItem)src=(JMenuItem)e.getSource();
		if(src==rdCxt.getDrawingArea().getPopup2().getMarkAsJunction()){
			markJunc(rdCxt,rdCxt.getAppFrame().getTpnl().getSelectedRoads());
		}else if(src==rdCxt.getDrawingArea().getPopup2().getUnmarkAsJunction()){
			for(RoadSegment r:rdCxt.getAppFrame().getTpnl().getSelectedRoads())
				r.getOdrRoad().setJunction("-1");
			rdCxt.getAppFrame().getJp().updateJunction();
		}
	}
	private static void markJunc(RoadContext rdCxt,Set<RoadSegment> set) {
		int id=JunctionsUpdater.getNextId(rdCxt);
		Junction jn=new Junction();
		jn.setId(id+"");
		jn.setName("");
		for(RoadSegment r:set)
			r.getOdrRoad().setJunction(id+"");
		rdCxt.getRn().getOdrNetwork().getJunction().add(jn);
		rdCxt.getAppFrame().getJp().selectedJn=jn.getId();
		rdCxt.getAppFrame().getJp().updateJunction();
	}
	public JMenuItem getMarkAsJunction() {
		return markAsJunction;
	}
	public JMenuItem getUnmarkAsJunction() {
		return unmarkAsJunction;
	}
}
