package org.tde.tdescenariodeveloper.updation;

import java.util.ArrayList;
import java.util.List;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection.LaneLink;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Predecessor;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Successor;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.ui.AppFrame;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.tde.tdescenariodeveloper.ui.ToolsPanel;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * This class is used to udpate data of link of lanes and helps linker in {@link ToolsPanel} 
 * @author Shmeel
 * @see ToolsPanel
 * @see org.movsim.network.autogen.opendrive.Lane.Link
 * @see Lane
 * @see LaneLink
 */
public class LinkUpdater {
	/**
	 * 
	 * @param linkPoints {@link List} of {@link RoadLaneSegmentPair}
	 * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	 public static void linkSelectedLanes(ArrayList<RoadLaneSegmentPair> linkPoints,RoadContext rdCxt) {
		 //TODO auto road if junction conflict
	    	RoadSegment rdPr=linkPoints.get(0).getRs();
	    	RoadSegment rdSc=linkPoints.get(1).getRs();
	    	if(rdPr.getOdrRoad().getJunction().equals("-1") && rdSc.getOdrRoad().getJunction().equals("-1")){
	    		addLinks(linkPoints);
	    	}else if(!rdPr.getOdrRoad().getJunction().equals("-1") && rdSc.getOdrRoad().getJunction().equals("-1")){
	    		addLinks2(linkPoints,rdCxt);
	    	}else if(rdPr.getOdrRoad().getJunction().equals("-1") && !rdSc.getOdrRoad().getJunction().equals("-1")){
	    		addLinks3(linkPoints,rdCxt);
	    	}else if(!rdPr.getOdrRoad().getJunction().equals("-1") && !rdSc.getOdrRoad().getJunction().equals("-1")){
	    		addLinks4(linkPoints,rdCxt);
	    	}
	    	linkPoints.clear();
	}
	 /**
	  * 
	  * Called when two jucntions tries to be linked only used to show user a warning
	  * @param linkPoints list of {@link RoadLaneSegmentPair}
	  * @param rdCxt RoadLaneSegmentPair
	  */
	 public static void addLinks4(ArrayList<RoadLaneSegmentPair> linkPoints,
			RoadContext rdCxt) {
		 GraphicsHelper.showToast("Two junctions cannot be linked, use non-junction intermediate road",rdCxt.getToastDurationMilis());
	 }
	 /**
	  * Called when predecessor road is {@link Junction}
	  * @param linkPoints {@link List} of {@link RoadLaneSegmentPair}
	  * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	  */
	public static void addLinks3(ArrayList<RoadLaneSegmentPair> linkPoints,
			RoadContext rdCxt) {
			RoadSegment rdPr=linkPoints.get(0).getRs();
		    RoadSegment rdSc=linkPoints.get(1).getRs();
		    if(rdSc.getOdrRoad().isSetLink() && rdSc.getOdrRoad().getLink().isSetSuccessor() && rdSc.getOdrRoad().getLink().getSuccessor().getElementType().equals("junction")){
		    	if(rdSc.getOdrRoad().getLink().isSetPredecessor() && !rdSc.getOdrRoad().getLink().getPredecessor().getElementId().equals(rdPr.userId())){
		    		RoadSegment pr=rdCxt.getRn().findByUserId(rdSc.getOdrRoad().getLink().getPredecessor().getElementId());
		    		clearLink(pr,rdSc,rdCxt);
		    		GraphicsHelper.showMessage("Main road can't have both predecessor and successor junctions: link broken with old road");
		    	}
		    	addLinks(linkPoints);
		    	return;
		    }
	 		Junction scJn=JunctionsUpdater.getJunction(rdSc.getOdrRoad().getJunction(), rdCxt);
	 		if(scJn==null){
	 			GraphicsHelper.showToast("Junction not found in road "+rdSc.userId(), rdCxt.getToastDurationMilis());
	 			return;
	 		}
	 		if(!rdPr.getOdrRoad().isSetLink())rdPr.getOdrRoad().setLink(new Link());
	 		if(!rdSc.getOdrRoad().isSetLink())rdSc.getOdrRoad().setLink(new Link());
	 		if(!rdSc.getOdrRoad().getLink().isSetPredecessor())rdSc.getOdrRoad().getLink().setPredecessor(new Predecessor());
	 		if(!rdPr.getOdrRoad().getLink().isSetSuccessor())rdPr.getOdrRoad().getLink().setSuccessor(new Successor());
	 		
	 		rdPr.getOdrRoad().getLink().getSuccessor().setElementId(rdSc.userId());
	 		rdSc.getOdrRoad().getLink().getPredecessor().setElementId(scJn.getId());

	 		rdPr.getOdrRoad().getLink().getSuccessor().setContactPoint("start");
	 		rdSc.getOdrRoad().getLink().getPredecessor().setContactPoint("end");
	 		
	 		rdPr.getOdrRoad().getLink().getSuccessor().setElementType("road");
	 		rdSc.getOdrRoad().getLink().getPredecessor().setElementType("junction");
	 		
	 		Lane lanePr=linkPoints.get(0).getLs().getOdrLane();
	 		Lane laneSc=linkPoints.get(1).getLs().getOdrLane();
	 		
	 		if(laneSc.isSetLink()){
	 			if(laneSc.getLink().isSetPredecessor())laneSc.getLink().setPredecessor(null);
	 		}
	 		
	 		if(!lanePr.isSetLink())lanePr.setLink(new org.movsim.network.autogen.opendrive.Lane.Link());
	 		if(!lanePr.getLink().isSetSuccessor())lanePr.getLink().setSuccessor(new org.movsim.network.autogen.opendrive.Lane.Link.Successor());
	 		
	 		lanePr.getLink().getSuccessor().setId(laneSc.getId());
	 		
	 		Connection cn=JunctionsUpdater.getConnection(rdSc.userId(),rdPr.userId(),scJn);
	 		if(cn!=null){
	 			LaneLink l=getLaneLink(lanePr.getId(), laneSc.getId(), cn);
	 			if(l==null){
	 				l=new LaneLink();
	 				l.setFrom(lanePr.getId());
	 				l.setTo(laneSc.getId());
	 				cn.getLaneLink().add(l);
	 			}
	 		}else{
	 			Connection cc=new Connection();
	 			cc.setConnectingRoad(rdSc.userId());
	 			cc.setIncomingRoad(rdPr.userId());
	 			LaneLink l;
				l=new LaneLink();
				l.setFrom(lanePr.getId());
				l.setTo(laneSc.getId());
				cc.getLaneLink().add(l);
				scJn.getConnection().add(cc);
	 		}
	}
	 /**
	  * Called when successor road is {@link Junction}
	  * @param linkPoints {@link List} of {@link RoadLaneSegmentPair}
	  * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	  */
	public static void addLinks2(ArrayList<RoadLaneSegmentPair> linkPoints,RoadContext rdCxt) {
	    RoadSegment rdPr=linkPoints.get(0).getRs();
	    RoadSegment rdSc=linkPoints.get(1).getRs();
	    if(rdPr.getOdrRoad().isSetLink() && rdPr.getOdrRoad().getLink().isSetPredecessor() && rdPr.getOdrRoad().getLink().getPredecessor().getElementType().equals("junction")){
	    	if(rdPr.getOdrRoad().getLink().isSetSuccessor() && !rdPr.getOdrRoad().getLink().getSuccessor().getElementId().equals(rdSc.userId()) ){
	    		RoadSegment nxt=rdCxt.getRn().findByUserId(rdPr.getOdrRoad().getLink().getSuccessor().getElementId());
	    		clearLink(rdPr,nxt,rdCxt);
	    		GraphicsHelper.showMessage("Main road can't have both predecessor and successor junctions: link broken with old road\nYou may like to add an intermediate road");
	    	}
	    	addLinks(linkPoints);
	    	return;
	    }
		Junction prJn=JunctionsUpdater.getJunction(rdPr.getOdrRoad().getJunction(), rdCxt);
		if(prJn==null){
			GraphicsHelper.showToast("Junction not found in road "+rdPr.userId(), rdCxt.getToastDurationMilis());
			return;
		}
		if(!rdPr.getOdrRoad().isSetLink())rdPr.getOdrRoad().setLink(new Link());
		if(!rdSc.getOdrRoad().isSetLink())rdSc.getOdrRoad().setLink(new Link());
		if(!rdSc.getOdrRoad().getLink().isSetPredecessor())rdSc.getOdrRoad().getLink().setPredecessor(new Predecessor());
		if(!rdPr.getOdrRoad().getLink().isSetSuccessor())rdPr.getOdrRoad().getLink().setSuccessor(new Successor());
		
		rdPr.getOdrRoad().getLink().getSuccessor().setElementId(prJn.getId());
		rdSc.getOdrRoad().getLink().getPredecessor().setElementId(rdPr.userId());

		rdPr.getOdrRoad().getLink().getSuccessor().setContactPoint("start");
		rdSc.getOdrRoad().getLink().getPredecessor().setContactPoint("end");
		
		rdPr.getOdrRoad().getLink().getSuccessor().setElementType("junction");
		rdSc.getOdrRoad().getLink().getPredecessor().setElementType("road");
		
		Lane lanePr=linkPoints.get(0).getLs().getOdrLane();
		Lane laneSc=linkPoints.get(1).getLs().getOdrLane();
		
		if(lanePr.isSetLink()){
			if(lanePr.getLink().isSetSuccessor())lanePr.getLink().setSuccessor(null);
		}
		
		if(!laneSc.isSetLink())laneSc.setLink(new org.movsim.network.autogen.opendrive.Lane.Link());
		if(!laneSc.getLink().isSetPredecessor())laneSc.getLink().setPredecessor(new org.movsim.network.autogen.opendrive.Lane.Link.Predecessor());
		
		laneSc.getLink().getPredecessor().setId(lanePr.getId());
		
		Connection cn=JunctionsUpdater.getConnection(rdPr.userId(),rdSc.userId(),prJn);
		if(cn!=null){
			LaneLink l=getLaneLink(lanePr.getId(), laneSc.getId(), cn);
			if(l==null){
				l=new LaneLink();
				l.setFrom(lanePr.getId());
				l.setTo(laneSc.getId());
				cn.getLaneLink().add(l);
			}
		}else{
			Connection cc=new Connection();
			cc.setId(JunctionsUpdater.getNextId(prJn)+"");
			cc.setConnectingRoad(rdPr.userId());
			cc.setIncomingRoad(rdSc.userId());
			LaneLink l;
				l=new LaneLink();
				l.setFrom(lanePr.getId());
				l.setTo(laneSc.getId());
				cc.getLaneLink().add(l);
				prJn.getConnection().add(cc);
		}
	
	}
	/**
	 * clears link between given roads
	 * @param rdPr {@link Road} predecessor
	 * @param rdSc {@link Road} Successor
	 * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public static void clearLink(RoadSegment rdPr, RoadSegment rdSc,RoadContext rdCxt) {
		if(!rdPr.getOdrRoad().getJunction().equals("-1"))
			JunctionsUpdater.clearJunction(rdPr, rdCxt);
		if(!rdSc.getOdrRoad().getJunction().equals("-1"))
			JunctionsUpdater.clearJunction(rdSc, rdCxt);
		clearSuccessor(rdPr);
		clearPredecessor(rdSc);
	}
	/**
	 * clears predecessor of given road
	 * @param rdSc {@link Road} under consideration
	 */
	public static void clearPredecessor(RoadSegment rdSc) {
		if(rdSc.getOdrRoad().isSetLink()){
			if(rdSc.getOdrRoad().getLink().isSetPredecessor()){
				if(rdSc.getOdrRoad().getLink().isSetSuccessor())rdSc.getOdrRoad().getLink().setPredecessor(null);
				else{
					rdSc.getOdrRoad().setLink(null);
				}
			}else{
				if(!rdSc.getOdrRoad().getLink().isSetSuccessor()){
					rdSc.getOdrRoad().setLink(null);
				}
			}
		}
		for(int i=0;i<rdSc.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size();i++){
			Lane l=rdSc.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().get(i);
			if(l.isSetLink() && l.getLink().isSetPredecessor()){
				if(l.getLink().isSetSuccessor())l.getLink().setPredecessor(null);
				else l.setLink(null);
			}else if(l.isSetLink() && !l.getLink().isSetSuccessor()){
				l.setLink(null);
			}
		}
	}
	/**
	 * used to remove {@link Successor} of given {@link Road}
	 * @param rdPr {@link Road} of which successor to be cleared
	 */
	public static void clearSuccessor(RoadSegment rdPr) {
		if(rdPr.getOdrRoad().isSetLink()){
			if(rdPr.getOdrRoad().getLink().isSetSuccessor()){
				if(rdPr.getOdrRoad().getLink().isSetPredecessor())rdPr.getOdrRoad().getLink().setSuccessor(null);
				else{
					rdPr.getOdrRoad().setLink(null);
				}
			}else{
				if(!rdPr.getOdrRoad().getLink().isSetPredecessor()){
					rdPr.getOdrRoad().setLink(null);
				}
			}
		}
		for(int i=0;i<rdPr.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().size();i++){
			Lane l=rdPr.getOdrRoad().getLanes().getLaneSection().get(0).getRight().getLane().get(i);
			if(l.isSetLink() && l.getLink().isSetSuccessor()){
				if(l.getLink().isSetPredecessor())l.getLink().setSuccessor(null);
				else l.setLink(null);
			}else if(l.isSetLink() && !l.getLink().isSetPredecessor()){
				l.setLink(null);
			}
		}
	}
	/**
	 * gets a link if existed having from and to ids same as given
	 * @param from id of from lane
	 * @param to id of to lane
	 * @param cn connection under consideration
	 * @return {@link LaneLink}
	 */
	public static LaneLink getLaneLink(int from,int to,Connection cn){
		 LaneLink ln=null;
		 for(LaneLink l:cn.getLaneLink()){
			 if(l.getFrom()==from && l.getTo()==to)return l;
		 }
		 return ln;
	 }
	/**
	 * used when link is requested with help of linker
	 * @param linkPoints {@link List} of {@link RoadLaneSegmentPair}
	 */
	public static void addLinks(ArrayList<RoadLaneSegmentPair>linkPoints){
    	RoadSegment rdPr=linkPoints.get(0).getRs();
    	RoadSegment rdSc=linkPoints.get(1).getRs();
    	
    	Lane lanePr=linkPoints.get(0).getLs().getOdrLane();
    	Lane laneSc=linkPoints.get(1).getLs().getOdrLane();
    	
    	if(lanePr.isSetLink() && lanePr.getLink().isSetSuccessor()){
    		GraphicsHelper.showToast("A lane can't be successor of two lanes at a time", AppFrame.getAppFrame().getrdCxt().getToastDurationMilis());
    		return;
    	}
    	
		if(!rdPr.getOdrRoad().isSetLink())rdPr.getOdrRoad().setLink(new Link());
		if(!rdSc.getOdrRoad().isSetLink())rdSc.getOdrRoad().setLink(new Link());
//		if(!rdPr.getOdrRoad().getLink().isSetPredecessor())rdPr.getOdrRoad().getLink().setPredecessor(new Predecessor());
		if(!rdSc.getOdrRoad().getLink().isSetPredecessor())rdSc.getOdrRoad().getLink().setPredecessor(new Predecessor());
		if(!rdPr.getOdrRoad().getLink().isSetSuccessor())rdPr.getOdrRoad().getLink().setSuccessor(new Successor());
//		if(!rdSc.getOdrRoad().getLink().isSetSuccessor())rdSc.getOdrRoad().getLink().setSuccessor(new Successor());
		
		rdPr.getOdrRoad().getLink().getSuccessor().setElementId(rdSc.userId());
		rdSc.getOdrRoad().getLink().getPredecessor().setElementId(rdPr.userId());

		rdPr.getOdrRoad().getLink().getSuccessor().setContactPoint("start");
		rdSc.getOdrRoad().getLink().getPredecessor().setContactPoint("end");
		
		rdPr.getOdrRoad().getLink().getSuccessor().setElementType("road");
		rdSc.getOdrRoad().getLink().getPredecessor().setElementType("road");
		
		
		if(!lanePr.isSetLink())lanePr.setLink(new org.movsim.network.autogen.opendrive.Lane.Link());
		if(!lanePr.getLink().isSetSuccessor())lanePr.getLink().setSuccessor(new org.movsim.network.autogen.opendrive.Lane.Link.Successor());

		if(!laneSc.isSetLink())laneSc.setLink(new org.movsim.network.autogen.opendrive.Lane.Link());
		if(!laneSc.getLink().isSetPredecessor())laneSc.getLink().setPredecessor(new org.movsim.network.autogen.opendrive.Lane.Link.Predecessor());
		
		lanePr.getLink().getSuccessor().setId(laneSc.getId());
		laneSc.getLink().getPredecessor().setId(lanePr.getId());
	}
	/**
	 * clears whole road's link
	 * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public static void removeRoad(RoadContext rdCxt) {
		//TODO modify for concurrentModificationExcetion
		if(rdCxt.getSelectedRoad()!=null){
			if(!rdCxt.getSelectedRoad().getOdrRoad().getJunction().equals("-1"))
				JunctionsUpdater.clearJunction(rdCxt.getSelectedRoad(), rdCxt);
			if(rdCxt.getSelectedRoad().getOdrRoad().getLink()!=null && rdCxt.getSelectedRoad().getOdrRoad().getLink().isSetPredecessor()){
				RoadSegment rs=rdCxt.getRn().findByUserId(rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor().getElementId());
				if(rs!=null && !rs.getOdrRoad().getJunction().equals("-1")){
					Junction j=JunctionsUpdater.getJunction(rs.getOdrRoad().getLink().getSuccessor().getElementId(), rdCxt);
					if(j!=null){						
						for(Connection cn:j.getConnection()){
							if(cn.getIncomingRoad().equals(rdCxt.getSelectedRoad().userId())){
								j.getConnection().remove(cn);
								break;
							}
						}
					}
				}
			}
			if(rdCxt.getSelectedRoad().getOdrRoad().getLink()!=null && rdCxt.getSelectedRoad().getOdrRoad().getLink().isSetSuccessor()){
				RoadSegment rs=rdCxt.getRn().findByUserId(rdCxt.getSelectedRoad().getOdrRoad().getLink().getSuccessor().getElementId());
				if(rs!=null && !rs.getOdrRoad().getJunction().equals("-1")){
					Junction j=JunctionsUpdater.getJunction(rs.getOdrRoad().getLink().getPredecessor().getElementId(), rdCxt);
					if(j!=null){						
						for(Connection cn:j.getConnection()){
							if(cn.getIncomingRoad().equals(rdCxt.getSelectedRoad().userId())){
								j.getConnection().remove(cn);
								break;
							}
						}
					}
				}
			}
			if(rdCxt.getSelectedRoad().getOdrRoad().getLink()!=null && rdCxt.getSelectedRoad().getOdrRoad().getLink().isSetPredecessor()){
				RoadSegment rs=rdCxt.getRn().findByUserId(rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor().getElementId());
				if(rs!=null && rs.getOdrRoad().getJunction().equals("-1"))
					clearSuccessor(rs);
			}
			if(rdCxt.getSelectedRoad().getOdrRoad().getLink()!=null && rdCxt.getSelectedRoad().getOdrRoad().getLink().isSetSuccessor()){
				RoadSegment rs=rdCxt.getRn().findByUserId(rdCxt.getSelectedRoad().getOdrRoad().getLink().getSuccessor().getElementId());
				if(rs!=null && rs.getOdrRoad().getJunction().equals("-1"))
					clearPredecessor(rs);
			}
			if(rdCxt.getRn().getOdrNetwork().getRoad().remove(rdCxt.getSelectedRoad().getOdrRoad())){
				RoadNetworkUtils.refresh(rdCxt);
				String id = rdCxt.getSelectedRoad().getOdrRoad().getId();
				List<org.movsim.autogen.Road> roadList = rdCxt.getMvCxt().getMovsim().getScenario().getSimulation().getRoad();
				org.movsim.autogen.Road deletedRoad = null;
				for(org.movsim.autogen.Road road : roadList){
					if(road.getId().equals(id)){
						deletedRoad = road;
						break;
					}
				}
				rdCxt.getMvCxt().getMovsim().getScenario().getSimulation().getRoad().remove(deletedRoad);
				rdCxt.getMvCxt().updatePanels();
			}else GraphicsHelper.showToast("Road "+rdCxt.getSelectedRoad().userId()+" couldn't be remvoed", rdCxt.getToastDurationMilis());
		}else GraphicsHelper.showToast("Select road to delete", rdCxt.getToastDurationMilis());
	
	}
}
