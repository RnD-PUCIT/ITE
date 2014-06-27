package org.tde.tdescenariodeveloper.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.updation.JunctionsUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Right click menu for {@link Junction}s
 * @author Shmeel
 * @see JunctionsPanel
 * @see Junction
 */
public class DrawingAreaPopupMenu2 extends JPopupMenu implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7710136231966863266L;
	RoadContext rdCxt;
	JMenuItem markAsJunction,unmarkAsJunction;
	/**
	 * 
	 * @param roadPrPnl rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public DrawingAreaPopupMenu2(RoadContext roadPrPnl) {
		rdCxt=roadPrPnl;
		initialize();
		addListeners();
		setDefaultLightWeightPopupEnabled(false);
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
		markAsJunction=new JMenuItem("Make junction");
		markAsJunction.setToolTipText("Makes all selected roads part of junction further configuration is done in Junctions editor");
		unmarkAsJunction=new JMenuItem("Unmark roads as junctions");
		markAsJunction.setOpaque(false);
		unmarkAsJunction.setOpaque(false);
	}
	@Override
	public void show(Component c,int a,int b){
		super.show(c, a, b);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
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
	/**
	 * used to mark selected {@link Road}s part of {@link Junction}
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @param set selected {@link Road}s to be marked
	 */
	public static void markJunc(RoadContext rdCxt,Set<RoadSegment> set) {
		int id=JunctionsUpdater.getNextId(rdCxt);
		Junction jn=new Junction();
		jn.setId(id+"");
		jn.setName("");
		String []ids=new String[set.size()];
		int i=0;
		for(RoadSegment r:set)
			ids[i++]=r.userId();
		String mainRd=GraphicsHelper.selectionFromUser("Select main road", new JunctionDemoPanel(), ids);
		if(mainRd!=null && !mainRd.equals("")){
			RoadSegment main=null;
			ArrayList<RoadSegment>othr=new ArrayList<>();
			for(RoadSegment r:set){
				if(r.userId().equals(mainRd)){
					r.getOdrRoad().setJunction(jn.getId());
					main=r;
				}else othr.add(r);
			}
			if(main==null){
				GraphicsHelper.showToast("Selected road not found", rdCxt.getToastDurationMilis());
				return;
			}else if(othr.size()+1!=set.size()){
				GraphicsHelper.showToast("Something went wrong", rdCxt.getToastDurationMilis());
				return;
			}
			for(RoadSegment r:rdCxt.getAppFrame().getTpnl().getSelectedRoads())
				r.getOdrRoad().setJunction("-1");
			main.getOdrRoad().setJunction(jn.getId());
			i=0;
			for(RoadSegment r:othr){
				Connection cc=new Connection();
				cc.setConnectingRoad(mainRd);
				cc.setIncomingRoad(r.userId());
				cc.setId((i++)+"");
				cc.setContactPoint("start");
				jn.getConnection().add(cc);
			}
			rdCxt.getRn().getOdrNetwork().getJunction().add(jn);
			rdCxt.getAppFrame().getJp().selectedJn=jn.getId();
			rdCxt.getAppFrame().getJp().updateJunction();
		}
	}

	public JMenuItem getMarkAsJunction() {
		return markAsJunction;
	}
	public JMenuItem getUnmarkAsJunction() {
		return unmarkAsJunction;
	}
}
/**
 * Panel used as background while selecting connecting road at time of marking roads as {@link Junction}
 * @author Shmeel
 *
 */
class JunctionDemoPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -408415062954815657L;
	public JunctionDemoPanel() {
		setPreferredSize(new Dimension(400,250));
	}
}