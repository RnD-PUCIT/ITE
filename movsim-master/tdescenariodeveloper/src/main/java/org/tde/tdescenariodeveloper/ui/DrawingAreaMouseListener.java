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

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class DrawingAreaMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final DrawingArea trafficCanvas;
    public final DrawingAreaController controller;
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
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
        boolean isSelectedSet=false;
        for(int i=trafficCanvas.getRoadPrPnl().getRn().size()-1;i>=0 && !isSelectedSet;i--){
        	RoadSegment rs=trafficCanvas.getRoadPrPnl().getRn().getRoadSegments().get(i);
        	if(!(rs.roadMapping() instanceof RoadMappingPoly)){
	    		if(rs.roadMapping().contains(transformedPoint.getX(), transformedPoint.getY())){
	    			trafficCanvas.getRoadPrPnl().setSelectedRoad(rs);
	    			isSelectedSet=true;
	    		}
        	}else{
        		int ind=0;
        		for(RoadMapping rm:((RoadMappingPoly)rs.roadMapping())){
        			if(rm.contains(transformedPoint.getX(), transformedPoint.getY())){
    	    			trafficCanvas.getRoadPrPnl().setSelectedRoad(rs);
    	    			trafficCanvas.getRoadPrPnl().getGmPnl().setSelectedGeometry(ind);
    	    			isSelectedSet=true;
    	    		}
        			ind++;
        		}
        	}
    	}
        if(!isSelectedSet){
        	trafficCanvas.getRoadPrPnl().setSelectedRoadNull();
        	trafficCanvas.getRoadPrPnl().setVisible(false);
        }
        else trafficCanvas.getRoadPrPnl().setVisible(true);
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
        	if(trafficCanvas.getRoadPrPnl().getSelectedRoad()!=null){
        		trafficCanvas.transform.inverseTransform(new Point2D.Float(startDragX, startDragY), startTransformed);
        		if(!(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly)){
        			if(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
        				X0.add(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().getX0());
        				Y0.add(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().getY0());
        			}
        		}else{
        			RoadMappingPoly rmp=(RoadMappingPoly) trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping();
        			if(rmp.contains(startTransformed)){
    					for(int ii=0;ii<rmp.getRoadMappings().size();ii++){
    						X0.add(rmp.getRoadMappings().get(ii).getX0());
    						Y0.add(rmp.getRoadMappings().get(ii).getY0());
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
       	 	 if(trafficCanvas.getRoadPrPnl().getSelectedRoad()!=null){
    		 boolean isPoly=trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping() instanceof RoadMappingPoly;
    		 try {
				trafficCanvas.transform.inverseTransform(new Point2D.Float(p.x, p.y), endTransformed);
				if(!isPoly){
					if(trafficCanvas.getRoadPrPnl().getSelectedRoad().roadMapping().contains(startTransformed)){
						int selRd=trafficCanvas.getRoadPrPnl().getRn().getRoadSegments().indexOf(trafficCanvas.getRoadPrPnl().getSelectedRoad());
						selRd=selRd<0?0:selRd;
						trafficCanvas.getRoadPrPnl().getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setX(X0.get(0)+endTransformed.getX()-startTransformed.getX());
						trafficCanvas.getRoadPrPnl().getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(0).setY(Y0.get(0)+endTransformed.getY()-startTransformed.getY());
						RoadNetworkUtils.refresh(trafficCanvas.getRoadPrPnl());
						trafficCanvas.getRoadPrPnl().getGmPnl().setSelectedGeometry(0);
						trafficCanvas.getRoadPrPnl().setSelectedRoad(trafficCanvas.getRoadPrPnl().getRn().getRoadSegments().get(selRd));
					}
	    		 }
	    		 else{
	    			 int selRd=trafficCanvas.getRoadPrPnl().getRn().getRoadSegments().indexOf(trafficCanvas.getRoadPrPnl().getSelectedRoad());
	    			 selRd=selRd<0?0:selRd;
					for (int ii=0;ii<X0.size();ii++) {
						Geometry rm=trafficCanvas.getRoadPrPnl().getSelectedRoad().getOdrRoad().getPlanView().getGeometry().get(ii);
						rm.setX(X0.get(ii) + endTransformed.getX() - startTransformed.getX());
						rm.setY(Y0.get(ii) + endTransformed.getY() - startTransformed.getY());
						RoadNetworkUtils.refresh(trafficCanvas.getRoadPrPnl());
					}
					trafficCanvas.getRoadPrPnl().getGmPnl().setSelectedGeometry(0);
					trafficCanvas.getRoadPrPnl().setSelectedRoad(trafficCanvas.getRoadPrPnl().getRn().getRoadSegments().get(selRd));
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
