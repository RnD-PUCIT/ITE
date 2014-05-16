package org.tde.tdescenariodeveloper.ui;

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

import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;

public class DrawingAreaMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final DrawingArea trafficCanvas;
    public final DrawingAreaController controller;
    private final RoadNetwork roadNetwork;
    private boolean inDrag;
    private int startDragX;
    private int startDragY;
    private int xOffsetSave;
    private int yOffsetSave;
    RoadMapping rm;
    private StatusPanel statusPnl;
    Point2D.Double startTransformed=new Point2D.Double(),endTransformed=new Point2D.Double();
    private ArrayList<Double> X0=new ArrayList<>(),Y0=new ArrayList<>(); 

    /**
     * @param trafficCanvas
     */
    public DrawingAreaMouseListener(DrawingArea trafficCanvas, DrawingAreaController controller, RoadNetwork roadNetwork) {
        this.trafficCanvas = trafficCanvas;
        this.controller = controller;
        this.roadNetwork = roadNetwork;
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
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
        boolean isSelectedSet=false;
        for(int i=roadNetwork.size()-1;i>=0 && !isSelectedSet;i--){
        	RoadSegment rs=roadNetwork.getRoadSegments().get(i);
        	if(!(rs.roadMapping() instanceof RoadMappingPoly)){
	    		if(rs.roadMapping().contains(transformedPoint.getX(), transformedPoint.getY())){
	    			trafficCanvas.getRoadPnl().setSelectedRoad(rs);
	    			isSelectedSet=true;
	    		}
        	}else{
        		int ind=0;
        		for(RoadMapping rm:((RoadMappingPoly)rs.roadMapping())){
        			if(rm.contains(transformedPoint.getX(), transformedPoint.getY())){
    	    			trafficCanvas.getRoadPnl().setSelectedRoad(rs);
    	    			trafficCanvas.getRoadPnl().getGmPnl().setSelectedGeometry(ind);
    	    			isSelectedSet=true;
    	    		}
        			ind++;
        		}
        	}
    	}
        if(!isSelectedSet){
        	trafficCanvas.getRoadPnl().setSelectedRoadNull();
        	trafficCanvas.getRoadPnl().setVisible(false);
        }
        else trafficCanvas.getRoadPnl().setVisible(true);
        trafficCanvas.paint(trafficCanvas.getGraphics());
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
        X0.clear();
        Y0.clear();
        try {
        	if(trafficCanvas.getRoadPnl().getSelectedRoad()!=null){
        		trafficCanvas.transform.inverseTransform(new Point2D.Float(startDragX, startDragY), startTransformed);
        		if(!(trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly)){
        			if(trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
        				X0.add(trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().getX0());
        				Y0.add(trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().getY0());
        			}
        		}else{
        			RoadMappingPoly rmp=(RoadMappingPoly) trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping();
        			if(rmp.contains(startTransformed)){
        				if(trafficCanvas.isWholeRoadSelectable()){
        					for(int ii=0;ii<rmp.getRoadMappings().size();ii++){
        						X0.add(rmp.getRoadMappings().get(ii).getX0());
        						Y0.add(rmp.getRoadMappings().get(ii).getY0());
        					}
        				}
        				else {
        					X0.add(rmp.getRoadMappings().get(trafficCanvas.getRoadPnl().getGmPnl().getSelectedIndex()).getX0());
        					Y0.add(rmp.getRoadMappings().get(trafficCanvas.getRoadPnl().getGmPnl().getSelectedIndex()).getY0());						
        				}
        			}
        		}
        	}
		} catch (NoninvertibleTransformException e1) {
			// TODO Auto-generated catch block
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
       	 	 if(trafficCanvas.getRoadPnl().getSelectedRoad()!=null){
    		 boolean isPoly=trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly;
    		 try {
				trafficCanvas.transform.inverseTransform(new Point2D.Float(p.x, p.y), endTransformed);
				if(!isPoly){
					if(trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
						trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().setX0(X0.get(0)+endTransformed.getX()-startTransformed.getX());
						trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().setY0(Y0.get(0)+endTransformed.getY()-startTransformed.getY());
						trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().adjustBounds(endTransformed.getX()-startTransformed.getX(),endTransformed.getY()-startTransformed.getY());
					}
	    		 }
	    		 else{
					RoadMappingPoly rmp = (RoadMappingPoly) trafficCanvas
							.getRoadPnl().getSelectedRoad().roadMapping();
					if (trafficCanvas.isWholeRoadSelectable()) {
						for (int ii=0;ii<X0.size();ii++) {
							RoadMapping rm=rmp.getRoadMappings().get(ii);
							rm.setX0(X0.get(ii) + endTransformed.getX() - startTransformed.getX());
							rm.setY0(Y0.get(ii) + endTransformed.getY() - startTransformed.getY());
							rm.adjustBounds(endTransformed.getX()-startTransformed.getX(),endTransformed.getY()-startTransformed.getY());
						}
					}
					else{
						int gmSelInd=trafficCanvas.getRoadPnl().getGmPnl().getSelectedIndex();
						rmp.getRoadMappings().get(gmSelInd).setX0(X0.get(0)+endTransformed.getX()-startTransformed.getX());
						rmp.getRoadMappings().get(gmSelInd).setY0(Y0.get(0)+endTransformed.getY()-startTransformed.getY());
						rmp.getRoadMappings().get(gmSelInd).adjustBounds(endTransformed.getX()-startTransformed.getX(),endTransformed.getY()-startTransformed.getY());
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
        	if(trafficCanvas.getRoadPnl().getSelectedRoad()!=null){
        		if(trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
        			inBounds=((RoadMappingPoly)trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping()).contains(startTransformed);
        		}
        		else{
        			inBounds=trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().contains(startTransformed);
        		}
        	}
        	 if(trafficCanvas.getRoadPnl().getSelectedRoad()!=null && inBounds){
        		 boolean isPoly=trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly;
	    		 try {
					trafficCanvas.transform.inverseTransform(new Point2D.Float(p.x, p.y), endTransformed);
					if(!isPoly){
						if(trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
							trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().setX0(X0.get(0)+endTransformed.getX()-startTransformed.getX());
							trafficCanvas.getRoadPnl().getSelectedRoad().roadMapping().setY0(Y0.get(0)+endTransformed.getY()-startTransformed.getY());
						}
		    		 }
		    		 else{
						RoadMappingPoly rmp = (RoadMappingPoly) trafficCanvas
								.getRoadPnl().getSelectedRoad().roadMapping();
						if (trafficCanvas.isWholeRoadSelectable()) {
							for (int ii=0;ii<X0.size();ii++) {
								RoadMapping rm=rmp.getRoadMappings().get(ii);
								rm.setX0(X0.get(ii) + endTransformed.getX() - startTransformed.getX());
								rm.setY0(Y0.get(ii) + endTransformed.getY() - startTransformed.getY());
							}
						}
						else{
							int gmSelInd=trafficCanvas.getRoadPnl().getGmPnl().getSelectedIndex();
							rmp.getRoadMappings().get(gmSelInd).setX0(X0.get(0)+endTransformed.getX()-startTransformed.getX());
							rmp.getRoadMappings().get(gmSelInd).setY0(Y0.get(0)+endTransformed.getY()-startTransformed.getY());
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
			e1.printStackTrace();
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
}
