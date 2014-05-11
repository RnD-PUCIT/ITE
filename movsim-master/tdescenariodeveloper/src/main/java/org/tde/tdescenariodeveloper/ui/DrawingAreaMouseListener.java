package org.tde.tdescenariodeveloper.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import org.movsim.roadmappings.RoadMapping;
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
    Point2D.Double startTransformed=new Point2D.Double(),endTransformed=new Point2D.Double();
    private double X0,Y0; 

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
        
        for(int i=roadNetwork.size()-1;i>=0;i--){
        	RoadSegment rs=roadNetwork.getRoadSegments().get(i);
    		if(rs.roadMapping().contains(transformedPoint.getX(), transformedPoint.getY())){
    			trafficCanvas.setSelectedRoad(rs);
    			trafficCanvas.paint(trafficCanvas.getGraphics());
    			break;
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
        	if(trafficCanvas.getSelectedRoad()!=null){
        		X0=trafficCanvas.getSelectedRoad().roadMapping().getX0();
        		Y0=trafficCanvas.getSelectedRoad().roadMapping().getY0();
        		trafficCanvas.transform.inverseTransform(new Point2D.Float(startDragX, startDragY), startTransformed);
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
    	if(inDrag && trafficCanvas.getSelectedRoad()!=null && trafficCanvas.getSelectedRoad().roadMapping().contains(startTransformed)){
        	try {
        		trafficCanvas.transform.inverseTransform(new Point2D.Float(e.getPoint().x, e.getPoint().y), endTransformed);
        		trafficCanvas.getSelectedRoad().roadMapping().setX0(X0+endTransformed.getX()-startTransformed.getX());
        		trafficCanvas.getSelectedRoad().roadMapping().setY0(Y0+endTransformed.getY()-startTransformed.getY());
        		trafficCanvas.getSelectedRoad().roadMapping().adjustBounds(endTransformed.getX()-startTransformed.getX(),endTransformed.getY()-startTransformed.getY());
        	} catch (NoninvertibleTransformException e1) {
        		JOptionPane.showMessageDialog(null, e1.getMessage());
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
        if (inDrag) {//start transform should be updated every time
        	 if(trafficCanvas.getSelectedRoad()!=null && trafficCanvas.getSelectedRoad().roadMapping().contains(startTransformed)){
             	try {
             		trafficCanvas.transform.inverseTransform(new Point2D.Float(p.x, p.y), endTransformed);
             		trafficCanvas.getSelectedRoad().roadMapping().setX0(X0+endTransformed.getX()-startTransformed.getX());
            		trafficCanvas.getSelectedRoad().roadMapping().setY0(Y0+endTransformed.getY()-startTransformed.getY());
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
}
