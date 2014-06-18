package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection.LaneLink;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Class used to Listen for changes made to related {@link LaneLink}
 * @author Shmeel
 * @see LaneLink
 */
public class LaneLinkListener implements ActionListener,Blockable {
	boolean blocked=true;
	RoadContext rdCxt;
	JComboBox<String>from;
	JComboBox<String>to;
	LaneLink lnLnk;
	JButton removeLn;
	Connection cn;
	/**
	 * 
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 * @param from id of predecessor {@link Lane}
	 * @param to id of successor {@link Lane}
	 * @param lnLnk {@link LaneLink} to which this listener is attached
	 * @param removeLn {@link JButton} used to remove this {@link LaneLink}
	 * @param cn {@link Connection} in which related {@link LaneLink} is contained
	 */
	public LaneLinkListener(RoadContext rdCxt, JComboBox<String> from,
			JComboBox<String> to, LaneLink lnLnk,JButton removeLn,Connection cn) {
		super();
		this.rdCxt = rdCxt;
		this.from = from;
		this.to = to;
		this.lnLnk = lnLnk;
		this.removeLn=removeLn;
		this.cn=cn;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JComboBox<String>src=null;
		JButton srcBtn=null;
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		if(e.getSource() instanceof JComboBox<?>)src=(JComboBox<String>)e.getSource();
		if(src==from){
			lnLnk.setFrom(Integer.parseInt((String)from.getSelectedItem()));
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updateGraphics();
		}else if(src==to){
			lnLnk.setTo(Integer.parseInt((String)to.getSelectedItem()));
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updateGraphics();
		}else if(srcBtn==removeLn){
			cn.getLaneLink().remove(lnLnk);
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.getAppFrame().getJl().actionPerformed(new ActionEvent(rdCxt.getAppFrame().getJp().getCbSelectJunc(), 234, ""));
		}
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
