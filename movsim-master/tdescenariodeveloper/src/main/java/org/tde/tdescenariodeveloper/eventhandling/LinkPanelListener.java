package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Predecessor;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Successor;
import org.tde.tdescenariodeveloper.ui.LanesPanel;
import org.tde.tdescenariodeveloper.ui.LinkPanel;
import org.tde.tdescenariodeveloper.ui.RoadContext;
/**
 * Class used to listen changes made to {@link Road} {link Link}
 * @author Shmeel
 * @see Road
 * @see Link
 * @see Predecessor
 * @see Successor
 */
public class LinkPanelListener implements ActionListener,Blockable {
	boolean blocked=true;
	RoadContext rdCxt;
	LinkPanel lp;
	/**
	 * 
	 * @param rdCxt contains reference to loaded .xodr and other added panels in it
	 * @param lp {@link LanesPanel} panel to which this listener is attached
	 */
	public LinkPanelListener(RoadContext rdCxt,LinkPanel lp) {
		super();
		this.rdCxt = rdCxt;
		this.lp=lp;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(blocked)return;
		if(lp.getCbSelectLink().getSelectedIndex()==0){
			rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor().setElementType((String)lp.getCbElementType().getSelectedItem());
		}
		else{
			rdCxt.getSelectedRoad().getOdrRoad().getLink().getSuccessor().setElementType((String)lp.getCbElementType().getSelectedItem());
		}
	}
	
}
