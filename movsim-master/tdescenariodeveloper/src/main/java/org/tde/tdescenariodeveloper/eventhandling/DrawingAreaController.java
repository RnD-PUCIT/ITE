package org.tde.tdescenariodeveloper.eventhandling;

import org.tde.tdescenariodeveloper.ui.DrawingArea;

/**
 * Class used to listen for wheel movements
 * @author Shmeel
 * @see DrawingArea
 */
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
