package org.tde.tdescenariodeveloper.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

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
    RoadSegment selected=null;
    AffineTransform at=null;

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
        at=trafficCanvas.transform;
        try {
			at.inverseTransform(new Point2D.Float(point.x, point.y), transformedPoint);
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
        
        for(int i=roadNetwork.size()-1;i>=0;i--){
        	RoadSegment rs=roadNetwork.getRoadSegments().get(i);
    		if(controller.contains(transformedPoint.getX(), transformedPoint.getY(), rs.roadMapping())){
    			selected=rs;
    			updateSelected();
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
        inDrag = true;
    }

    public void updateSelected(){
    	trafficCanvas.paint(trafficCanvas.getGraphics());
    	Stroke dashed = new BasicStroke(1.5f*(float)trafficCanvas.scale, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4*(float)trafficCanvas.scale}, 0);
    	Graphics2D g2d=((Graphics2D)trafficCanvas.getGraphics());
    	g2d.setStroke(dashed);
    	g2d.setColor(new Color(Integer.MAX_VALUE-selected.roadMapping().roadColor()));
    	g2d.draw(at.createTransformedShape(selected.roadMapping().getBounds()));
    	
    }
	/*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    	if(selected!=null)updateSelected();
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
        updateSelected();
    }


	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
}
