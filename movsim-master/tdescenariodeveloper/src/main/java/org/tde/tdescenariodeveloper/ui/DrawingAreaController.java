package org.tde.tdescenariodeveloper.ui;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMappingArc;
import org.movsim.roadmappings.RoadMappingBezier;
import org.movsim.roadmappings.RoadMappingCircle;
import org.movsim.roadmappings.RoadMappingLine;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.roadmappings.RoadMappingPolyBezier;
import org.movsim.roadmappings.RoadMappingPolyLine;
import org.movsim.roadmappings.RoadMappingS;
import org.movsim.roadmappings.RoadMappingU;
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
    public boolean contains(double x,double y,RoadMapping roadMapping){
    	assert roadMapping != null;

        final Line2D.Double line = new Line2D.Double();
        final Point2D from = new Point2D.Double();
        final Point2D to = new Point2D.Double();
        RoadMapping.PosTheta posTheta;
        double lateralOffset=0.0;
        final double roadLength = roadMapping.roadLength();
        GeneralPath path=new GeneralPath();
        final Class<? extends RoadMapping> roadMappingClass = roadMapping.getClass();
        if (roadMappingClass == RoadMappingU.class) {
            final RoadMappingU mappingU = (RoadMappingU) roadMapping;
            final double straightLength = mappingU.straightLength();

            // draw the first straight
            posTheta = roadMapping.startPos(lateralOffset);
            from.setLocation(posTheta.x, posTheta.y);
            posTheta = roadMapping.map(straightLength, lateralOffset);
            to.setLocation(posTheta.x, posTheta.y);
            path.moveTo(from.getX(), from.getY());
            path.lineTo(to.getX(), to.getY());

            // draw the U
            posTheta = mappingU.startPos();
            final double radius = mappingU.radius();
            final Arc2D.Double arc2D = new Arc2D.Double();
            arc2D.setArcByCenter(posTheta.x - straightLength, posTheta.y + radius, radius + lateralOffset, 90.0, 180.0,
                    Arc2D.OPEN);
            path.append(arc2D, true);
            
            // draw the second straight
            posTheta = roadMapping.map(roadLength - straightLength, lateralOffset);
            from.setLocation(posTheta.x, posTheta.y);
            posTheta = roadMapping.map(roadLength, lateralOffset);
            to.setLocation(posTheta.x, posTheta.y);
            line.setLine(from, to);
            path.append(line, true);
            path.closePath();
            Shape path3=trafficCanvas.transform.createTransformedShape(path);
            return path3.getBounds2D().contains(x,y);


        } else if (roadMappingClass == RoadMappingS.class) {
            // } else if (roadMappingClass == RoadMappingArcExtended.class) {
            // final double straightLength =
            // ((RoadMappingArcExtended)roadMapping).straightLength();
            //
            // // draw the first straight
            // posTheta = roadMapping.map(0.0, lateralOffset);
            // from.setLocation(posTheta.x, posTheta.y);
            // posTheta = roadMapping.map(straightLength, lateralOffset);
            // to.setLocation(posTheta.x, posTheta.y);
            // line.setLine(from, to);
            // g.draw(line);
            //
            // // draw the second straight
            // posTheta = roadMapping.map(roadLength - straightLength,
            // lateralOffset);
            // from.setLocation(posTheta.x, posTheta.y);
            // posTheta = roadMapping.map(roadLength, lateralOffset);
            // to.setLocation(posTheta.x, posTheta.y);
            // line.setLine(from, to);
            // g.draw(line);

            // // draw the arc
            // final RoadMappingArcStraight arc =
            // (RoadMappingArcStraight)roadMapping;
            // posTheta = roadMapping.map(straightLength);
            // final double angSt = Math.toDegrees(arc.startAngle());
            // final double angExt = Math.toDegrees(arc.arcAngle());
            // final double radius = arc.radius();
            // final double dx = radius * Math.cos(arc.startAngle());
            // final double dy = radius * Math.sin(arc.startAngle());
            // Arc2D.Double arc2D = new Arc2D.Double();
            // arc2D.setArcByCenter(posTheta.x - dx, posTheta.y + dy, radius +
            // offset, angSt, angExt, Arc2D.OPEN);
            // g.draw(arc2D);
            // return;
        } else if (roadMappingClass == RoadMappingArc.class) {
            final RoadMappingArc arc = (RoadMappingArc) roadMapping;
            posTheta = roadMapping.startPos();
            final double angSt = arc.startAngle() + (arc.clockwise() ? 0.5 * Math.PI : -0.5 * Math.PI);
            final double radius = arc.radius();
            final double dx = radius * Math.cos(angSt);
            final double dy = radius * Math.sin(angSt);
            final Arc2D.Double arc2D = new Arc2D.Double();
            arc2D.setArcByCenter(posTheta.x - dx, posTheta.y + dy, radius + lateralOffset, Math.toDegrees(angSt),
                    Math.toDegrees(arc.arcAngle()), Arc2D.OPEN);
            path.append(arc2D, true);
            path.closePath();
            Shape path3=trafficCanvas.transform.createTransformedShape(path);
            return path3.getBounds2D().contains(x,y);
        } else if (roadMappingClass == RoadMappingCircle.class) {
            final RoadMappingCircle arc = (RoadMappingCircle) roadMapping;
            posTheta = roadMapping.startPos();
            final double radius = arc.radius();
            final Arc2D.Double arc2D = new Arc2D.Double();
            arc2D.setArcByCenter(posTheta.x - radius, posTheta.y, radius + lateralOffset, 0.0, 360.0, Arc2D.OPEN);
            path.append(arc2D, true);
            path.closePath();
            Shape path3=trafficCanvas.transform.createTransformedShape(path);
            return path3.getBounds2D().contains(x,y);
        } else if (roadMappingClass == RoadMappingLine.class) {
            posTheta = roadMapping.startPos(lateralOffset);
            from.setLocation(posTheta.x, posTheta.y);
            posTheta = roadMapping.endPos(lateralOffset);
            to.setLocation(posTheta.x, posTheta.y);
            line.setLine(from, to);
            path.append(line, true);
            path.closePath();
            Shape path3=trafficCanvas.transform.createTransformedShape(path);
            return path3.getBounds2D().contains(x,y);
        } else if (roadMappingClass == RoadMappingPoly.class) {
            final RoadMappingPoly poly = (RoadMappingPoly) roadMapping;
            boolean bl = false;
            for (final RoadMapping map : poly) {
                bl=contains(x, y, roadMapping);
                if(bl)break;
            }
            return bl;
        } else if (roadMappingClass == RoadMappingPolyLine.class) {
            if (lateralOffset == 0.0) {
                final RoadMappingPolyLine polyLine = (RoadMappingPolyLine) roadMapping;
                final Iterator<RoadMappingLine> iterator = polyLine.iterator();
                if (!iterator.hasNext())
                    return false;
                final GeneralPath path2 = new GeneralPath();
                RoadMappingLine line1 = iterator.next();
                posTheta = line1.startPos(lateralOffset);
                path2.moveTo(posTheta.x, posTheta.y);
                posTheta = line1.endPos(lateralOffset);
                path2.lineTo(posTheta.x, posTheta.y);
                while (iterator.hasNext()) {
                    line1 = iterator.next();
                    posTheta = line1.endPos(lateralOffset);
                    path2.lineTo(posTheta.x, posTheta.y);
                }
                path.append(path2, true);
                path.closePath();
                Shape path3=trafficCanvas.transform.createTransformedShape(path);
                return path3.getBounds2D().contains(x,y);
            }
        } else if (roadMappingClass.equals(RoadMappingBezier.class)) {
            if (lateralOffset == 0.0) {
                // TODO remove this zero condition when Bezier lateral offset
                // for control points has been fixed
                // Bezier mapping does not quite give correct control point
                // offsets
                // so only use this if lateral offset is zero (ie not for road
                // edge lines)
                final RoadMappingBezier bezier = (RoadMappingBezier) roadMapping;
                final GeneralPath path2 = new GeneralPath();
                posTheta = bezier.startPos(lateralOffset);
                path2.moveTo(posTheta.x, posTheta.y);
                posTheta = bezier.endPos(lateralOffset);
                final double cx = bezier.controlX(lateralOffset);
                final double cy = bezier.controlY(lateralOffset);
                path2.quadTo(cx, cy, posTheta.x, posTheta.y);
                path.append(path2, true);
                path.closePath();
                Shape path3=trafficCanvas.transform.createTransformedShape(path);
                return path3.getBounds2D().contains(x,y);
            }
        } else if (roadMappingClass.equals(RoadMappingPolyBezier.class)) {
            if (lateralOffset == 0.0) {
                final RoadMappingPolyBezier polyBezier = (RoadMappingPolyBezier) roadMapping;
                final Iterator<RoadMappingBezier> iterator = polyBezier.iterator();
                if (!iterator.hasNext())
                    return false;
                final GeneralPath path2 = new GeneralPath();
                RoadMappingBezier bezier = iterator.next();
                posTheta = bezier.startPos(lateralOffset);
                final int radius = 10;
                final int radiusC = 6;
//                if (drawBezierPoints) {
//                    g.fillOval((int) posTheta.x - radius / 2, (int) posTheta.y - radius / 2, radius, radius);
//                }
                path2.moveTo(posTheta.x, posTheta.y);
                posTheta = bezier.endPos(lateralOffset);
                path2.quadTo(bezier.controlX(lateralOffset), bezier.controlY(lateralOffset), posTheta.x, posTheta.y);
//                if (drawBezierPoints) {
//                    g.fillOval((int) posTheta.x - radius / 2, (int) posTheta.y - radius / 2, radius, radius);
//                    g.fillOval((int) bezier.controlX(lateralOffset) - radiusC / 2, (int) bezier.controlY(lateralOffset)
//                            - radiusC / 2, radiusC, radiusC);
//                }
                while (iterator.hasNext()) {
                    bezier = iterator.next();
                    posTheta = bezier.endPos(lateralOffset);
                    path2.quadTo(bezier.controlX(lateralOffset), bezier.controlY(lateralOffset), posTheta.x, posTheta.y);
//                    if (drawBezierPoints) {
//                        g.fillOval((int) posTheta.x - radius / 2, (int) posTheta.y - radius / 2, radius, radius);
//                        g.fillOval((int) bezier.controlX(lateralOffset) - radiusC / 2,
//                                (int) bezier.controlY(lateralOffset) - radiusC / 2, radiusC, radiusC);
//                    }
                }
                path.append(path2, true);
                path.closePath();
                Shape path3=trafficCanvas.transform.createTransformedShape(path);
                return path3.getBounds2D().contains(x,y);
            }
        }
        // default drawing
        // draw the road in sections 5 meters long
//        final double sectionLength = 5.0;
//        double roadPos = 0.0;
//        posTheta = roadMapping.startPos(lateralOffset);
//        from.setLocation(posTheta.x, posTheta.y);
//        while (roadPos < roadLength) {
//            roadPos += sectionLength;
//            posTheta = roadMapping.map(Math.min(roadPos, roadLength), lateralOffset);
//            to.setLocation(posTheta.x, posTheta.y);
//            line.setLine(from, to);
//            g.draw(line);
//            from.setLocation(to.getX(), to.getY());
//        }
//    	return true;
		return false;
    }
}
