package org.tde.tdescenariodeveloper.ui;

import org.movsim.simulator.roadnetwork.RoadNetwork;


public class DrawingAreaController {
    final DrawingArea trafficCanvas;
    public DrawingAreaController(DrawingArea trafficCanvas) {
        this.trafficCanvas = trafficCanvas;
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
