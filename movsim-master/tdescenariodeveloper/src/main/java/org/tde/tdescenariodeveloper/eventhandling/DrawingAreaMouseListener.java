package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.LaneSegment;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.ui.DrawingArea;
import org.tde.tdescenariodeveloper.ui.StatusPanel;
import org.tde.tdescenariodeveloper.ui.TDEResources;
import org.tde.tdescenariodeveloper.updation.LinkUpdater;
import org.tde.tdescenariodeveloper.updation.RoadLaneSegmentPair;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Used to listen for drawing area mouse gestures/clicks/movements
 * @author Shmeel
 * @see DrawingArea
 *
 */
public class DrawingAreaMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {
    private final DrawingArea trafficCanvas;
    public final DrawingAreaController controller;
    public boolean inDrag,selected,roadInDrag=false;
    private int startDragX;
    private int startDragY;
    private int xOffsetSave;
    private int yOffsetSave;
    private RoadMapping rm;
    private StatusPanel statusPnl;
    Point2D.Double startTransformed=new Point2D.Double(),endTransformed=new Point2D.Double();
    private ArrayList<Double> X0=new ArrayList<>(),Y0=new ArrayList<>();
	private Point2D mousePoint;
    final private ArrayList<RoadLaneSegmentPair>linkPoints=new ArrayList<>();
    /**
     * @param trafficCanvas
     */
    public DrawingAreaMouseListener(DrawingArea trafficCanvas, DrawingAreaController controller) {
        this.trafficCanvas = trafficCanvas;
        this.controller = controller;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    	Point point=e.getPoint();
        Point2D.Double transformedPoint=new Point2D.Double();
        try {
			trafficCanvas.transform.inverseTransform(new Point2D.Float(point.x, point.y), transformedPoint);
		} catch (NoninvertibleTransformException e1) {
			GraphicsHelper.showMessage("Error: "+e1.getMessage());
		}
    	if(trafficCanvas.getCursor()==TDEResources.getResources().HAND_CURSOR){
    		RoadSegment sp=RoadNetworkUtils.getUnderLyingRoadSegment(transformedPoint,trafficCanvas.getRoadPrPnl().getMvCxt());
    		if(sp!=null){
    			if(e.getButton()==MouseEvent.BUTTON1){
    				for(RoadSegment rs:trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().getSelectedRoads()){
    					if(rs.userId().equals(sp.userId())){
    						trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().removeRoadSelection(sp);
    						trafficCanvas.paint(trafficCanvas.getGraphics());
    						return;
    					}
    				}
    				trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().addRoadSelection(sp);
    				trafficCanvas.paint(trafficCanvas.getGraphics());
    			}else if(e.getButton()==MouseEvent.BUTTON3){
    				trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().addRoadSelection(sp);
    				trafficCanvas.paint(trafficCanvas.getGraphics());
    			}
    		}
    		else{
        		trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().clearRoadSelections();
        		trafficCanvas.paint(trafficCanvas.getGraphics());
        		return;
    		}
    		if(e.getButton()==MouseEvent.BUTTON3){
    			trafficCanvas.getPopup2().show(trafficCanvas, e.getX(), e.getY());
    		}
    	}
    	else if(trafficCanvas.getCursor()==TDEResources.getResources().LINK_CURSOR){
    		if(e.getButton()==MouseEvent.BUTTON1){
    			RoadSegment sp=RoadNetworkUtils.getUnderLyingRoadSegment(transformedPoint,trafficCanvas.getRoadPrPnl().getMvCxt());
        		if(sp!=null){
        			LaneSegment ls=RoadNetworkUtils.getUnderLyingLaneSegment(transformedPoint, sp);
        			switch(linkPoints.size()){
        			case 0:
            			linkPoints.add(new RoadLaneSegmentPair(sp, ls, sp.userId(), ls.lane()));
        				break;
        			case 1:
        				if(linkPoints.get(0).getRoadId().equals(sp.userId())){
        					linkPoints.clear();
        					return;
        				}
        				linkPoints.add(new RoadLaneSegmentPair(sp, ls, sp.userId(), ls.lane()));
        				LinkUpdater.linkSelectedLanes(linkPoints, trafficCanvas.getRoadPrPnl());
        				trafficCanvas.getRoadPrPnl().getAppFrame().getJp().updateJunction();
        				trafficCanvas.getRoadPrPnl().updateGraphics();
        				break;
        			default:
        				linkPoints.clear();
        			}
        		}
        		else{
        			if(linkPoints.size()>0)linkPoints.clear();
        		}
    		}
    	}
    	else if(trafficCanvas.getCursor()==TDEResources.getResources().LINK_REMOVER_CURSOR){
    		if(e.getButton()==MouseEvent.BUTTON1){
    			RoadSegment sp=RoadNetworkUtils.getUnderLyingRoadSegment(transformedPoint,trafficCanvas.getRoadPrPnl().getMvCxt());
    			if(sp!=null){
        			LaneSegment ls=RoadNetworkUtils.getUnderLyingLaneSegment(transformedPoint, sp);
        			switch(linkPoints.size()){
        			case 0:
            			linkPoints.add(new RoadLaneSegmentPair(sp, ls, sp.userId(), ls.lane()));
        				break;
        			case 1:
        				if(linkPoints.get(0).getRoadId().equals(sp.userId())){
        					linkPoints.clear();
        					return;
        				}
        				linkPoints.add(new RoadLaneSegmentPair(sp, ls, sp.userId(), ls.lane()));
        				LinkUpdater.removeLink(linkPoints);
        				trafficCanvas.getRoadPrPnl().getAppFrame().getJp().updateJunction();
        				trafficCanvas.getRoadPrPnl().updateGraphics();
        				break;
        			default:
        				linkPoints.clear();
        			}
        		}
        		else{
        			if(linkPoints.size()>0)linkPoints.clear();
        		}
    		}
    	}
    	else if(trafficCanvas.getCursor()==TDEResources.getResources().TRAFFIC_SOURCE_CURSOR){
    		if(e.getButton()==MouseEvent.BUTTON1)trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().trafficSourceClicked(e);
    	}else if(trafficCanvas.getCursor()==TDEResources.getResources().STRAIGHT_ROAD_CURSOR){
    		if(e.getButton()==MouseEvent.BUTTON1)trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().straightRoadClicked(e);
    	}else if(trafficCanvas.getCursor()==TDEResources.getResources().ARC_ROAD_CURSOR){
    		if(e.getButton()==MouseEvent.BUTTON1)trafficCanvas.getRoadPrPnl().getAppFrame().getTpnl().arcRoadClicked(e);
	    }else if(trafficCanvas.getCursor().getType()==Cursor.DEFAULT_CURSOR){
	        selected=false;
	        for(int i=trafficCanvas.getRoadPrPnl().getRn().size()-1;i>=0 && !selected;i--){
	        	RoadSegment rs=trafficCanvas.getRoadPrPnl().getRn().getRoadSegments().get(i);
	        	if(!(rs.roadMapping() instanceof RoadMappingPoly)){
		    		if(rs.roadMapping().contains(transformedPoint.getX(), transformedPoint.getY())){
		    			trafficCanvas.getRoadPrPnl().getGmPnl().setSelectedGeometry(0,false);
		    			for(int lnInd=0;lnInd<rs.getLaneSegments().length;lnInd++){
		    				if(rs.getLaneSegments()[lnInd].getBounds().contains(transformedPoint)){
		    					trafficCanvas.getRoadPrPnl().getLanesPnl().setSelectedLane(lnInd, false);
		    					break;
		    				}
		    			}
		    			trafficCanvas.getRoadPrPnl().setSelectedRoad(rs);
		    			selected=true;
		    		}
	        	}else{
	        		RoadMappingPoly rmp=((RoadMappingPoly)rs.roadMapping());
	        		for(int ind=rmp.getRoadMappings().size()-1;ind>=0 && !selected;ind--){
	        			rm=rmp.getRoadMappings().get(ind);
	        			if(rm.contains(transformedPoint.getX(), transformedPoint.getY())){
	        				trafficCanvas.getRoadPrPnl().getGmPnl().setSelectedGeometry(ind,false);
	        				for(int lnInd=0;lnInd<rs.getLaneSegments().length;lnInd++){
	    	    				if(rs.getLaneSegments()[lnInd].getBounds().contains(transformedPoint)){
	    	    					trafficCanvas.getRoadPrPnl().getLanesPnl().setSelectedLane(lnInd, false);
	    	    					break;
	    	    				}
	    	    			}
	    	    			trafficCanvas.getRoadPrPnl().setSelectedRoad(rs);
	    	    			selected=true;
	    	    		}
	        		}
	        	}
	    	}
	        if(!selected){
	        	trafficCanvas.getRoadPrPnl().setSelectedRoadNull();
	        	trafficCanvas.getRoadPrPnl().updateGraphics();
	        }
	        else {
	        	trafficCanvas.getRoadPrPnl().updateGraphics();
	        	if(e.getButton()==MouseEvent.BUTTON3){
	        		trafficCanvas.getPopup().show(trafficCanvas, e.getX(), e.getY());
	        		statusPnl.setStatus(" ");
	        	}
	        }
    	}
    }
   


	/*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        final Point point = e.getPoint();
        startDragX = point.x;
        startDragY = point.y;
        xOffsetSave =  trafficCanvas.xOffset;
        yOffsetSave =  trafficCanvas.yOffset;
        try {
        	if(trafficCanvas.getRoadPrPnl().getSelectedRoad()!=null){
        		trafficCanvas.transform.inverseTransform(new Point2D.Float(startDragX, startDragY), startTransformed);
        		if(!(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly)){
        			if(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
        				roadInDrag=true;
        				X0.add(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().getX0());
        				Y0.add(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().getY0());
        			}
        		}else{
        			RoadMappingPoly rmp=(RoadMappingPoly) trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping();
        			if(rmp.contains(startTransformed)){
    					for(int ii=0;ii<rmp.getRoadMappings().size();ii++){
    						roadInDrag=true;
    						X0.add(rmp.getRoadMappings().get(ii).getX0());
    						Y0.add(rmp.getRoadMappings().get(ii).getY0());
    					}
        			}
        		}
        	}
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
        inDrag = true;
    }

	/*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    	Point p=e.getPoint();
    	if(inDrag){
       	 	 if(trafficCanvas.getRoadPrPnl().getSelectedRoad()!=null){
    		 boolean isPoly=trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly;
    		 try {
				trafficCanvas.transform.inverseTransform(new Point2D.Float(p.x, p.y), endTransformed);
				if(!isPoly){
					if(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
						roadInDrag=false;
						trafficCanvas.getRoadPrPnl().getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setX(X0.get(0)+endTransformed.getX()-startTransformed.getX());
						trafficCanvas.getRoadPrPnl().getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setY(Y0.get(0)+endTransformed.getY()-startTransformed.getY());
						RoadNetworkUtils.refresh(trafficCanvas.getRoadPrPnl());
					}
	    		 }
	    		 else{
	    			 
	    			 if(((RoadMappingPoly)trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping()).contains(startTransformed)){
	    				 roadInDrag=false;
						for (int ii=0;ii<X0.size();ii++) {
							Geometry rm=trafficCanvas.getRoadPrPnl().getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(ii);
							rm.setX(X0.get(ii) + endTransformed.getX() - startTransformed.getX());
							rm.setY(Y0.get(ii) + endTransformed.getY() - startTransformed.getY());
						}
						RoadNetworkUtils.refresh(trafficCanvas.getRoadPrPnl());
	    			 }
	    		}
			} catch (NoninvertibleTransformException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
    		 finally{
    			 X0.clear();
    			 Y0.clear();
    		 }
            }else{
	            final int xOffsetNew = xOffsetSave + (int) ((p.x - startDragX) / trafficCanvas.scale);
	            final int yOffsetNew = yOffsetSave + (int) ((p.y - startDragY) / trafficCanvas.scale);
	            if (xOffsetNew != trafficCanvas.xOffset || yOffsetNew != trafficCanvas.yOffset) {
	                trafficCanvas.xOffset = xOffsetNew;
	                trafficCanvas.yOffset = yOffsetNew;
	                trafficCanvas.setTransform();
	            }
   			 X0.clear();
   			 Y0.clear();
            }
    	}
    	trafficCanvas.paint(trafficCanvas.getGraphics());
        inDrag = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        final Point p = e.getPoint();
        boolean inBounds=false;
        if (inDrag) {//start transform should be updated every time
        	if(trafficCanvas.getRoadPrPnl().getSelectedRoad()!=null){
        		if(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
        			inBounds=((RoadMappingPoly)trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping()).contains(startTransformed);
        		}
        		else{
        			inBounds=trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().contains(startTransformed);
        		}
        	}
        	 if(trafficCanvas.getRoadPrPnl().getSelectedRoad()!=null && inBounds){
        		 boolean isPoly=trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly;
	    		 try {
					trafficCanvas.transform.inverseTransform(new Point2D.Float(p.x, p.y), endTransformed);
					if(!isPoly){
						if(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
							trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().setX0(X0.get(0)+endTransformed.getX()-startTransformed.getX());
							trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().setY0(Y0.get(0)+endTransformed.getY()-startTransformed.getY());
						}
		    		 }
		    		 else{
						RoadMappingPoly rmp = (RoadMappingPoly) trafficCanvas
								.getRoadPrPnl().getSelectedRoad().roadMapping();
						for (int ii=0;ii<X0.size();ii++) {
							RoadMapping rm=rmp.getRoadMappings().get(ii);
							rm.setX0(X0.get(ii) + endTransformed.getX() - startTransformed.getX());
							rm.setY0(Y0.get(ii) + endTransformed.getY() - startTransformed.getY());
						}
		    		}
				} catch (NoninvertibleTransformException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
             }else{
	            final int xOffsetNew = xOffsetSave + (int) ((p.x - startDragX) / trafficCanvas.scale);
	            final int yOffsetNew = yOffsetSave + (int) ((p.y - startDragY) / trafficCanvas.scale);
	            if (xOffsetNew != trafficCanvas.xOffset || yOffsetNew != trafficCanvas.yOffset) {
	                trafficCanvas.xOffset = xOffsetNew;
	                trafficCanvas.yOffset = yOffsetNew;
	                trafficCanvas.setTransform();
	            }
             }
        	 trafficCanvas.paint(trafficCanvas.getGraphics());
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
    	Point2D p=new Point2D.Double();
    	try {
			trafficCanvas.transform.inverseTransform(e.getPoint(), p);
		} catch (NoninvertibleTransformException e1) {
		}
    	if(linkPoints.size()==1){
    		mousePoint=p;
    		trafficCanvas.paint(trafficCanvas.getGraphics());
    	}
    	statusPnl.setStatus((int)p.getX()+", "+(int)p.getY());
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        final int notches = e.getWheelRotation();
        if (notches < 0) {
            controller.commandZoomIn();
        } else {
            controller.commandZoomOut();
        }
        trafficCanvas.paint(trafficCanvas.getGraphics());
    }
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}


	public void setStatusPnl(StatusPanel statusPnl) {
		this.statusPnl = statusPnl;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public ArrayList<RoadLaneSegmentPair> getLinkPoints() {
		return linkPoints;
	}


	public Point2D getMousePoint() {
		return mousePoint;
	}
}
