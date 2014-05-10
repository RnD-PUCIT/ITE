package org.tde.tdescenariodeveloper.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;

import org.movsim.roadmappings.RoadMapping;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.movsim.simulator.trafficlights.TrafficLightLocation;
import org.movsim.viewer.graphics.TrafficCanvas;

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
        for (final RoadSegment roadSegment : roadNetwork) {
            if (roadSegment.trafficLightLocations() != null) {
                final RoadMapping roadMapping = roadSegment.roadMapping();
                for (final TrafficLightLocation trafficLightLocation : roadSegment.trafficLightLocations()) {
                    final Rectangle2D trafficLightRect = TrafficCanvas.trafficLightRect(roadMapping, trafficLightLocation);
                    final Point point = e.getPoint();
                    final Point2D transformedPoint = new Point2D.Float();
                    try {
                        // convert from mouse coordinates to canvas coordinates
                        trafficCanvas.transform.inverseTransform(new Point2D.Float(point.x, point.y), transformedPoint);
                    } catch (final NoninvertibleTransformException e1) {
                        e1.printStackTrace();
                        return;
                    }
                    if (trafficLightRect.contains(transformedPoint)) {
                        trafficLightLocation.getTrafficLight().triggerNextPhase();
                        trafficCanvas.paint(trafficCanvas.getGraphics());
                    }
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
        Point2D.Double transformedPoint=new Point2D.Double();
        try {
			trafficCanvas.transform.inverseTransform(new Point2D.Float(point.x, point.y), transformedPoint);
		} catch (NoninvertibleTransformException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        for(RoadSegment rs:roadNetwork){
    		if(controller.contains(transformedPoint.getX(), transformedPoint.getY(), rs.roadMapping())){
    			//JOptionPane.showMessageDialog(null,rs.toString());
    		}
    	}
        
        JOptionPane.showMessageDialog(null,transformedPoint.getX()+", "+transformedPoint.getY());
        inDrag = true;
    }


	/*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
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
        if (inDrag) {
            final int xOffsetNew = xOffsetSave + (int) ((p.x - startDragX) / trafficCanvas.scale);
            final int yOffsetNew = yOffsetSave + (int) ((p.y - startDragY) / trafficCanvas.scale);
            if (xOffsetNew != trafficCanvas.xOffset || yOffsetNew != trafficCanvas.yOffset) {
                trafficCanvas.xOffset = xOffsetNew;
                trafficCanvas.yOffset = yOffsetNew;
                trafficCanvas.setTransform();
                trafficCanvas.paint(trafficCanvas.getGraphics());
            }
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
    }


	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
}
