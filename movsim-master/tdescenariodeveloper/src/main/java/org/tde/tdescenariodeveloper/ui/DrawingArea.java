package org.tde.tdescenariodeveloper.ui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.movsim.autogen.TrafficLightStatus;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.simulator.roadnetwork.AbstractTrafficSource;
import org.movsim.simulator.roadnetwork.Lanes;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.movsim.simulator.roadnetwork.Slope;
import org.movsim.simulator.roadnetwork.SpeedLimit;
import org.movsim.simulator.roadnetwork.TrafficSink;
import org.movsim.simulator.trafficlights.TrafficLight;
import org.movsim.simulator.trafficlights.TrafficLightLocation;
import org.movsim.viewer.roadmapping.PaintRoadMapping;

public class DrawingArea extends Canvas {
	private static final long serialVersionUID = 1653L;
	private RoadNetwork rn;
    private int bufferHeight=700;
    private int bufferWidth=1000;
    private Image backgroundBuffer=new BufferedImage(bufferWidth, bufferHeight, BufferedImage.TYPE_INT_ARGB);
    protected Color backgroundColor=new Color(7,147,7);;
    double scale=1.0;
    int xOffset=0,yOffset=0;
    protected AffineTransform transform = new AffineTransform();
    RoadPropertiesPanel roadPnl;
    float lineWidth=0.5f;
    float lineLength=4.5f;
    float gapLength=3.0f;
    float gapLengthExit=1.0f;
    // colors
    protected Color roadColor=new Color(150,150,150);
    protected Color roadEdgeColor=new Color(63,63,63);
    protected Color roadLineColor=new Color(200,200,200);
    protected Color sourceColor=Color.RED;
    protected Color sinkColor=Color.YELLOW;
    
    protected boolean drawRoadId=true;
    protected boolean drawSources=true;
    protected boolean drawSinks=true;
    protected boolean drawSpeedLimits=true;
    protected boolean drawSlopes=true;
    
    final DrawingAreaMouseListener mouseListener;
    final DrawingAreaKeyListener keyListener;
	private boolean drawAxis=true;
	private boolean drawBounds=false;
	private RoadSegment selectedRoad=null;
    
    
	public DrawingArea(RoadNetwork rn, RoadPropertiesPanel rdPrPnl) {
		this.roadPnl=rdPrPnl;
		setSize(new Dimension(bufferWidth,bufferHeight));
		this.rn=rn;
		keyListener=new DrawingAreaKeyListener(this, rn);
		addKeyListener(keyListener);
		mouseListener=new DrawingAreaMouseListener(this, keyListener, rn);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);
		setBackground(Color.GREEN.darker());
		
	}
    public void updateSelected(Graphics2D g){
    	if(selectedRoad==null)return;
    	roadPnl.updateFields(selectedRoad);
    	Stroke dashed = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2+(float)scale}, 0);
    	g.setStroke(dashed);
    	//g.setColor(new Color(Integer.MAX_VALUE-selectedRoad.roadMapping().roadColor()));
    	g.setColor(Color.WHITE.darker());
    	g.draw(selectedRoad.roadMapping().getBounds());
    	
    }
	protected void setTransform() {
        transform.setToIdentity();
        transform.scale(scale, scale);
        transform.translate(xOffset, yOffset);
    }
	
	public double getScale(){
		return scale;
	}
	@Override
	public void setSize(int newWidth, int newHeight) {
        super.setSize(Math.max(newWidth, 10), Math.max(newHeight, 10));
        final int width = getWidth();
        final int height = getHeight();
        setTransform();
        if (backgroundBuffer == null || width > bufferWidth || height > bufferHeight) {
            backgroundBuffer = createImage(width, height);
            bufferWidth = width;
            bufferHeight = height;
        }
    }
    @Deprecated
    protected void setScales() {
        final int width = Math.max(getSize().width, 10);
        final int height = Math.max(getSize().height, 10);

        if (backgroundBuffer == null || width != bufferWidth || height != bufferHeight) {
            backgroundBuffer = createImage(width, height);
            bufferWidth = width;
            bufferHeight = height;
        }
    }
    public void setScale(double scale) {
        final int width = getWidth();
        final int height = getHeight();
        xOffset -= 0.5 * width * (1.0 / this.scale - 1.0 / scale);
        yOffset -= 0.5 * height * (1.0 / this.scale - 1.0 / scale);
        this.scale = scale;
        setTransform();
    }
    @Override
    public void update(Graphics g) {
        final Graphics2D bufferGraphics = (Graphics2D) backgroundBuffer.getGraphics();
        clearBackground(bufferGraphics);
        bufferGraphics.setTransform(transform);
        drawBackground(bufferGraphics);
    }
    
    @Override
    public void paint(Graphics g) {
        if (backgroundBuffer == null)
            return;
        final Graphics2D backgroundGraphics = (Graphics2D) backgroundBuffer.getGraphics();
        clearBackground(backgroundGraphics);
        backgroundGraphics.setTransform(transform);
        drawBackground(backgroundGraphics);
        updateSelected(backgroundGraphics);
        g.drawImage(backgroundBuffer, 0, 0, null);
    }
    protected void clearBackground(Graphics2D g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    private void drawRoadSegments(Graphics2D g) {
        for (final RoadSegment roadSegment : rn) {
            final RoadMapping roadMapping = roadSegment.roadMapping();
            drawRoadSegment(g, roadMapping);
            drawRoadSegmentLines(g, roadMapping); // in one step (parallel or sequential update)?!
        }
    }
    private static void drawRoadSegment(Graphics2D g, RoadMapping roadMapping) {
        final BasicStroke roadStroke = new BasicStroke((float) roadMapping.roadWidth(), BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER);
        g.setStroke(roadStroke);
        g.setColor(new Color(roadMapping.roadColor()));
        PaintRoadMapping.paintRoadMapping(g, roadMapping);
    }
    
    private void drawRoadSegmentLines(Graphics2D g, RoadMapping roadMapping) {
        final float dashPhase = (float) (roadMapping.roadLength() % (lineLength + gapLength));

        final Stroke lineStroke = new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
                new float[] { lineLength, gapLength }, dashPhase);
        g.setStroke(lineStroke);
        g.setColor(roadLineColor);
        final int laneCount = roadMapping.laneCount();
        for (int lane = 1; lane < laneCount; ++lane) {
            final double offset = roadMapping.laneInsideEdgeOffset(lane);
            if (lane == roadMapping.trafficLaneMin() || lane == roadMapping.trafficLaneMax()) {
                // use exit stroke pattern for on-ramps, off-ramps etc
                final Stroke exitStroke = new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                        10.0f, new float[] { 5.0f, gapLengthExit }, 5.0f);
                g.setStroke(exitStroke);
            } else {
                g.setStroke(lineStroke);
            }
            PaintRoadMapping.paintRoadMapping(g, roadMapping, offset);
        }
        g.setStroke(new BasicStroke());
        g.setColor(roadEdgeColor);
        double offset = roadMapping.laneInsideEdgeOffset(Lanes.MOST_INNER_LANE - 1);
        PaintRoadMapping.paintRoadMapping(g, roadMapping, offset);
        offset = roadMapping.laneInsideEdgeOffset(roadMapping.laneCount());
        PaintRoadMapping.paintRoadMapping(g, roadMapping, offset);

    }
    protected void drawBackground(Graphics2D g) {
    	drawTrafficLights(g);
        if (drawSources) {
            drawSources(g);
        }
        if (drawSinks) {
            drawSinks(g);
        }
        drawRoadSegments(g);
        if (drawSpeedLimits) {
            drawSpeedLimits(g);
        }

        if (drawSlopes) {
            drawSlopes(g);
        }

        if (drawRoadId) {
            drawRoadSectionIds(g);
        }
        if (drawAxis) {
        	drawAxis(g);
        }
        if (drawBounds) {
        	drawBounds(g);
        }
    }
    private void drawBounds(Graphics2D g) {
    	for(RoadSegment rs:rn){
    		g.draw(rs.roadMapping().getBounds());
    	}
	}

	private void drawAxis(Graphics2D g) {
    	int i;
    	for(i=-15;i<15;i++){
    		g.drawLine(i*150, 0, ((i+1)*150), 0);
    		g.drawString(i*150+"", i*150, 0);
    		g.drawLine(0,i*150, 0, (i+1)*150);
    		g.drawString(i*150+"", 0, i*150);
    	}
    	g.drawString(i*150+"", i*150, 0);
    	g.drawString(i*150+"", 0, i*150);
	}

	private void drawTrafficLights(Graphics2D g) {
        for (final RoadSegment roadSegment : rn) {
            drawTrafficLightsOnRoad(g, roadSegment);
        }
    }
    public static Rectangle2D trafficLightRect(RoadMapping roadMapping, TrafficLightLocation trafficLightLocation) {
        final double offset = (roadMapping.laneCount() / 2.0 + 1.5) * roadMapping.laneWidth();
        final double size = 2 * roadMapping.laneWidth();
        final RoadMapping.PosTheta posTheta = roadMapping.map(trafficLightLocation.position(), offset);
        final Rectangle2D rect = new Rectangle2D.Double(posTheta.x - size / 2, posTheta.y - size / 2, size, size
                * trafficLightLocation.getTrafficLight().lightCount());
        return rect;
    }
    private static void drawTrafficLightsOnRoad(Graphics2D g, RoadSegment roadSegment) {
        if (roadSegment.trafficLightLocations() == null) {
            return;
        }
        final RoadMapping roadMapping = roadSegment.roadMapping();
        assert roadMapping != null;
        final double radius = 0.8 * roadMapping.laneWidth();
        for (TrafficLightLocation trafficLightLocation : roadSegment.trafficLightLocations()) {
            Rectangle2D trafficLightRect = trafficLightRect(roadMapping, trafficLightLocation);
            switch (trafficLightLocation.getTrafficLight().lightCount()) {
            case 1:
                drawTrafficLight1(g, trafficLightLocation.getTrafficLight(), trafficLightRect, radius);
                break;
            case 2:
                drawTrafficLight2(g, trafficLightLocation.getTrafficLight(), trafficLightRect, radius);
                break;
            default:
                drawTrafficLight3(g, trafficLightLocation.getTrafficLight(), trafficLightRect, radius);
                break;
            }
        }
    }
    
    private static void drawTrafficLight1(Graphics2D g, TrafficLight trafficLight, Rectangle2D trafficLightRect,
            double radius) {
        g.setColor(Color.DARK_GRAY);
        g.fill(trafficLightRect);
        switch (trafficLight.status()) {
        case GREEN:
            g.setColor(Color.GREEN);
            break;
        case GREEN_RED:
            g.setColor(Color.YELLOW);
            break;
        case RED:
            g.setColor(Color.RED);
            break;
        case RED_GREEN:
            g.setColor(Color.ORANGE);
            break;
        }
        final double x = trafficLightRect.getCenterX();
        final double y = trafficLightRect.getCenterY();
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }
    private static void drawTrafficLight2(Graphics2D g, TrafficLight trafficLight, Rectangle2D trafficLightRect,
            double radius) {
        g.setColor(Color.DARK_GRAY);
        g.fill(trafficLightRect);
        final Double width = trafficLightRect.getWidth();
        final Double height = trafficLightRect.getHeight();

        // draw the top light
        g.setColor(trafficLight.status() == TrafficLightStatus.RED ? Color.RED : Color.LIGHT_GRAY);
        Rectangle2D rect = new Rectangle2D.Double(trafficLightRect.getX(), trafficLightRect.getY(), width, height / 2.0);
        double x = rect.getCenterX();
        double y = rect.getCenterY();
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));

        // draw the bottom light
        g.setColor(trafficLight.status() == TrafficLightStatus.GREEN ? Color.GREEN : Color.LIGHT_GRAY);
        rect = new Rectangle2D.Double(trafficLightRect.getX(), trafficLightRect.getY() + height / 2.0, width,
                height / 2.0);
        x = rect.getCenterX();
        y = rect.getCenterY();
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }
    private static void drawTrafficLight3(Graphics2D g, TrafficLight trafficLight, Rectangle2D trafficLightRect,
            double radius) {
        g.setColor(Color.DARK_GRAY);
        g.fill(trafficLightRect);
        final Double width = trafficLightRect.getWidth();
        final Double height = trafficLightRect.getHeight();

        // draw the top light
        g.setColor(trafficLight.status() == TrafficLightStatus.RED ? Color.RED : Color.LIGHT_GRAY);
        Rectangle2D rect = new Rectangle2D.Double(trafficLightRect.getX(), trafficLightRect.getY(), width, height / 3.0);
        double x = rect.getCenterX();
        double y = rect.getCenterY();
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));

        // draw the middle light
        if (trafficLight.status() == TrafficLightStatus.GREEN_RED) {
            g.setColor(Color.YELLOW);
        } else if (trafficLight.status() == TrafficLightStatus.RED_GREEN) {
            g.setColor(Color.ORANGE);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        rect = new Rectangle2D.Double(trafficLightRect.getX(), trafficLightRect.getY() + height / 3.0, width,
                height / 3.0);
        x = rect.getCenterX();
        y = rect.getCenterY();
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));

        // draw the bottom light
        g.setColor(trafficLight.status() == TrafficLightStatus.GREEN ? Color.GREEN : Color.LIGHT_GRAY);
        rect = new Rectangle2D.Double(trafficLightRect.getX(), trafficLightRect.getY() + 2.0 * height / 3.0, width,
                height / 3.0);
        x = rect.getCenterX();
        y = rect.getCenterY();
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }
    
    private void drawSpeedLimits(Graphics2D g) {
        for (final RoadSegment roadSegment : rn) {
            drawSpeedLimitsOnRoad(g, roadSegment);
        }
    }

    private void drawSpeedLimitsOnRoad(Graphics2D g, RoadSegment roadSegment) {
        if (roadSegment.speedLimits() == null) {
            return;
        }

        final RoadMapping roadMapping = roadSegment.roadMapping();
        assert roadMapping != null;
        final double offset = -(roadMapping.laneCount() / 2.0 + 1.5) * roadMapping.laneWidth();
        final int redRadius2 = (int) (2.5 * roadMapping.laneWidth()) / 2;
        final int whiteRadius2 = (int) (2.0 * roadMapping.laneWidth()) / 2;
        final int fontHeight = whiteRadius2;
        final int offsetY = (int) (0.4 * fontHeight);
        final Font font = new Font("SansSerif", Font.BOLD, fontHeight); 
        final FontMetrics fontMetrics = getFontMetrics(font);

        for (final SpeedLimit speedLimit : roadSegment.speedLimits()) {

            g.setFont(font);
            final RoadMapping.PosTheta posTheta = roadMapping.map(speedLimit.getPosition(), offset);

            final double speedLimitValueKmh = speedLimit.getSpeedLimitKmh();
            if (speedLimitValueKmh < 150) {
                g.setColor(Color.RED);
                g.fillOval((int) posTheta.x - redRadius2, (int) posTheta.y - redRadius2, 2 * redRadius2, 2 * redRadius2);
                g.setColor(Color.WHITE);
                g.fillOval((int) posTheta.x - whiteRadius2, (int) posTheta.y - whiteRadius2, 2 * whiteRadius2,
                        2 * whiteRadius2);
                g.setColor(Color.BLACK);
                final String text = String.valueOf((int) (speedLimit.getSpeedLimitKmh()));
                final int textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, (int) (posTheta.x - textWidth / 2.0), (int) (posTheta.y + offsetY));
            } else {
                g.setColor(Color.BLACK);
                g.fillOval((int) posTheta.x - redRadius2, (int) posTheta.y - redRadius2, 2 * redRadius2, 2 * redRadius2);
                g.setColor(Color.WHITE);
                g.fillOval((int) posTheta.x - whiteRadius2, (int) posTheta.y - whiteRadius2, 2 * whiteRadius2,
                        2 * whiteRadius2);
                g.setColor(Color.BLACK);
                final int xOnCircle = (int) (whiteRadius2 * Math.cos(Math.toRadians(45.)));
                final int yOnCircle = (int) (whiteRadius2 * Math.sin(Math.toRadians(45.)));
                final Graphics2D g2 = g;
                final Line2D line = new Line2D.Double((int) posTheta.x - xOnCircle, (int) posTheta.y + yOnCircle,
                        (int) posTheta.x + xOnCircle, (int) posTheta.y - yOnCircle);
                g2.setStroke(new BasicStroke(2));
                g2.draw(line);
            }
        }
    }

    private void drawSlopes(Graphics2D g) {
        for (final RoadSegment roadSegment : rn) {
            drawSlopesOnRoad(g, roadSegment);
        }
    }

    private void drawSlopesOnRoad(Graphics2D g, RoadSegment roadSegment) {
        if (roadSegment.slopes() == null) {
            return;
        }

        final RoadMapping roadMapping = roadSegment.roadMapping();
        assert roadMapping != null;
        final double laneWidth = 10;
        final double offset = -(roadMapping.laneCount() / 2.0 + 1.5) * (roadMapping.laneWidth() + 1);
        final int whiteRadius2 = (int) (2.0 * laneWidth) / 2;
        final int fontHeight = whiteRadius2;
        final int offsetY = (int) (0.4 * fontHeight);
        final Font font = new Font("SansSerif", Font.BOLD, fontHeight);
        final FontMetrics fontMetrics = getFontMetrics(font);

        for (final Slope slope : roadSegment.slopes()) {
            g.setFont(font);
            final RoadMapping.PosTheta posTheta = roadMapping.map(slope.getPosition(), offset);

            final double gradient = slope.getGradient() * 100;
            g.setColor(Color.BLACK);
            final String text = String.valueOf((int) (gradient)) + " %";
            final int textWidth = fontMetrics.stringWidth(text);
            g.drawString(text, (int) (posTheta.x - textWidth / 2.0), (int) (posTheta.y + offsetY));
        }
    }
    private void drawRoadSectionIds(Graphics2D g) {
        for (final RoadSegment roadSegment : rn) {
            final RoadMapping roadMapping = roadSegment.roadMapping();
            final RoadMapping.PosTheta posTheta = roadMapping.map(0.0);
            final int fontHeight = 12;
            final Font font = new Font("SansSerif", Font.PLAIN, fontHeight); //$NON-NLS-1$
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawString("R" + roadSegment.userId(), (int) (posTheta.x), (int) (posTheta.y)); //$NON-NLS-1$
        }
    }
    private void drawSources(Graphics2D g) {
        for (final RoadSegment roadSegment : rn) {
            final RoadMapping roadMapping = roadSegment.roadMapping();
            assert roadMapping != null;
            final int radius = (int) ((roadMapping.laneCount() + 2) * roadMapping.laneWidth());
            final RoadMapping.PosTheta posTheta;
            final AbstractTrafficSource trafficSource = roadSegment.trafficSource();
            if (trafficSource != null) {
                g.setColor(sourceColor);
                posTheta = roadMapping.startPos();
                g.fillOval((int) posTheta.x - radius / 2, (int) posTheta.y - radius / 2, radius, radius);
            }
        }
    }
    private void drawSinks(Graphics2D g) {
        for (final RoadSegment roadSegment : rn) {
            final RoadMapping roadMapping = roadSegment.roadMapping();
            assert roadMapping != null;
            final int radius = (int) ((roadMapping.laneCount() + 2) * roadMapping.laneWidth());
            final RoadMapping.PosTheta posTheta;
            // draw the road segment sink, if there is one
            final TrafficSink sink = roadSegment.sink();
            if (sink != null) {
                g.setColor(sinkColor);
                posTheta = roadMapping.endPos();
                g.fillOval((int) posTheta.x - radius / 2, (int) posTheta.y - radius / 2, radius, radius);
            }
        }
    }
	public RoadSegment getSelectedRoad() {
		return selectedRoad;
	}
	public void setSelectedRoad(RoadSegment selectedRoad) {
		this.selectedRoad = selectedRoad;
	}
    
}
