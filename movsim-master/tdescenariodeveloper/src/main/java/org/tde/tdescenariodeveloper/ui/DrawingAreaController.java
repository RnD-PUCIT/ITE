package org.tde.tdescenariodeveloper.ui;

import org.movsim.roadmappings.RoadMapping;
import org.movsim.simulator.roadnetwork.RoadNetwork;


public class DrawingAreaController {
    final DrawingArea trafficCanvas;
    protected final RoadNetwork roadNetwork;

    public DrawingAreaController(DrawingArea trafficCanvas, RoadNetwork roadNetwork) {
        this.trafficCanvas = trafficCanvas;
        this.roadNetwork = roadNetwork;
    }

    public void commandZoomIn() {
        final double zoomFactor = Math.sqrt(2.0);
        trafficCanvas.setScale(trafficCanvas.getScale() * zoomFactor);
        trafficCanvas.paint(trafficCanvas.getGraphics());
    }

    public void commandZoomOut() {
        final double zoomFactor = Math.sqrt(2.0);
        trafficCanvas.setScale(trafficCanvas.getScale() / zoomFactor);
        trafficCanvas.paint(trafficCanvas.getGraphics());
    }
}
