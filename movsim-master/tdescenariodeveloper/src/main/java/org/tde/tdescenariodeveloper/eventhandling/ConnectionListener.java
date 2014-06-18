package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection.LaneLink;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.updation.JunctionsUpdater;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * The class to listen for changes made to any field of {@link Connection}
 * Each connection in the junction holds its own instance of this class.
 * @author Shmeel
 * @see Connection
 */
public class ConnectionListener implements ActionListener,Blockable {
	RoadContext rdCxt;
	JComboBox<String>connecting;
	JComboBox<String>incoming;
	JButton remove,addLn;
	/**
	 * 
	 * @param rdCxt contains reference to loaded .xodr and other added panels
	 * @param connecting connecting road selection
	 * @param incoming incoming road selection
	 * @param cntPnt  contact point selection
	 * @param cn connection to which this listener is attached
	 * @param remove button used to remove related connection
	 * @param addlnlnk button used to add new {@link LaneLink} in related connection
	 */
	public ConnectionListener(RoadContext rdCxt, JComboBox<String> connecting,
			JComboBox<String> incoming, JComboBox<String> cntPnt, Connection cn, JButton remove, JButton addlnlnk) {
		super();
		this.rdCxt = rdCxt;
		this.connecting = connecting;
		this.incoming = incoming;
		this.cntPnt = cntPnt;
		this.cn = cn;
		this.remove=remove;
		this.addLn=addlnlnk;
	}
	JComboBox<String>cntPnt;
	boolean blocked=true;
	Connection cn;
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JComboBox<String>src=null;
		JButton srcBtn=null;
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		if(e.getSource() instanceof JComboBox<?>)src=(JComboBox<String>)e.getSource();
		if(src==connecting){
			cn.setConnectingRoad((String)connecting.getSelectedItem());
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updateGraphics();
		}else if(src==incoming){
			cn.setIncomingRoad((String)incoming.getSelectedItem());
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updateGraphics();
		}else if(src==cntPnt){
			cn.setContactPoint((String)cntPnt.getSelectedItem());
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.updateGraphics();
		}else if(srcBtn==remove){
			rdCxt.getAppFrame().getJp();
			rdCxt.getAppFrame().getJp();
			JunctionsUpdater.getJunction(rdCxt.getAppFrame().getJp().getSelectedJn(), rdCxt).getConnection().remove(cn);
			RoadNetworkUtils.refresh(rdCxt);
			rdCxt.getAppFrame().getJl().actionPerformed(new ActionEvent(rdCxt.getAppFrame().getJp().getCbSelectJunc(), 234, ""));
		}else if(srcBtn==addLn){
			LaneLink ll=new LaneLink();
			String []vls=GraphicsHelper.valuesFromUser("Enter lane link information", "From ","To ");
			int from=Integer.MIN_VALUE,to=Integer.MIN_VALUE;
			if(vls[0].equals("") && vls[1].equals(""))return;
			try{
				from=Integer.parseInt(vls[0]);
				to=Integer.parseInt(vls[1]);
				ll.setFrom(from);
				ll.setTo(to);
				cn.getLaneLink().add(ll);
				RoadNetworkUtils.refresh(rdCxt);
				rdCxt.getAppFrame().getJl().actionPerformed(new ActionEvent(rdCxt.getAppFrame().getJp().getCbSelectJunc(), 234, ""));
			}catch(NumberFormatException ee){
				GraphicsHelper.showToast("Ivalid data entered", rdCxt.getToastDurationMilis());
			}
		}
	}
	@Override
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

}
