package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.tde.tdescenariodeveloper.ui.LinkPanel;
import org.tde.tdescenariodeveloper.ui.RoadContext;

public class LinkPanelListener implements ActionListener,Blockable {
	boolean blocked=true;
	RoadContext rdCxt;
	LinkPanel lp;
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
