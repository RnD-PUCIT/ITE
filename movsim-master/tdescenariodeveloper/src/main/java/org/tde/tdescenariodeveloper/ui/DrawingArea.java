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
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import org.movsim.autogen.Road;
import org.movsim.autogen.TrafficLightStatus;
import org.movsim.autogen.TrafficSource;
import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.input.network.OpenDriveHandlerUtils;
import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Lanes.LaneSection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.PlanView.Geometry;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMappingPoly;
import org.movsim.simulator.roadnetwork.LaneSegment;
import org.movsim.simulator.roadnetwork.Lanes;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.movsim.simulator.roadnetwork.Slope;
import org.movsim.simulator.roadnetwork.SpeedLimit;
import org.movsim.simulator.roadnetwork.TrafficSink;
import org.movsim.simulator.trafficlights.TrafficLight;
import org.movsim.simulator.trafficlights.TrafficLightLocation;
import org.movsim.viewer.roadmapping.PaintRoadMapping;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaController;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaMouseListener;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaPopupListener;
import org.tde.tdescenariodeveloper.eventhandling.Shortcuts;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Class used as canvas and used to show current scenario graphically to the user.
 * @author Shmeel
 * @see DrawingAreaController
 * @see DrawingAreaMouseListener
 * @see DrawingAreaPopupMenu
 * @see DrawingAreaPopupListener
 */
public class DrawingArea extends Canvas {
	private static final long serialVersionUID = 1653L;
    private int bufferHeight=700;
    private int bufferWidth=1000;
    private Image backgroundBuffer=new BufferedImage(bufferWidth, bufferHeight, BufferedImage.TYPE_INT_ARGB);
    protected Color backgroundColor=new Color(74,172,23);;
    public double scale=1.0;
    public int xOffset=0;
	public int yOffset=0;
    public AffineTransform transform = new AffineTransform();
    RoadContext roadPrPnl;
    float lineWidth=0.5f;
    float lineLength=4.5f;
    float gapLength=3.0f;
    float gapLengthExit=1.0f;
    // colors
    protected Color roadColor=new Color(150,150,150);
    protected Color roadEdgeColor=new Color(63,63,63);
    protected Color roadLineColor=new Color(200,200,200);
    protected Color sourceColor=Color.WHITE;
    protected Color sinkColor=Color.BLACK;
    
    protected boolean drawRoadId=true;
    protected boolean drawSources=true;
    protected boolean drawSinks=true;
    protected boolean drawSpeedLimits=true;
    protected boolean drawSlopes=true;
    
    final DrawingAreaMouseListener mouseListener;
    final Shortcuts keyListner;
	private boolean drawAxis=true;
	private boolean drawRoadBounds=false;
	private boolean drawRoadNames=false;
	private boolean drawLaneBounds=false;
	private boolean drawSelectedLane=true;
	private boolean drawSelectedGeometry=true;
	private boolean drawSignals=true;
	public void setDrawSignals(boolean drawSignals) {
		this.drawSignals = drawSignals;
	}
	public void setDrawSpeedLimits(boolean drawSpeedLimits) {
		this.drawSpeedLimits = drawSpeedLimits;
	}
	public void setDrawSelectedLane(boolean drawSelectedLane) {
		this.drawSelectedLane = drawSelectedLane;
	}
	public void setDrawSelectedGeometry(boolean drawSelectedGeometry) {
		this.drawSelectedGeometry = drawSelectedGeometry;
	}
	public void setDrawLaneLinks(boolean drawLaneLinks) {
		this.drawLaneLinks = drawLaneLinks;
	}
	private boolean drawLaneLinks=true;
    private DrawingAreaPopupMenu popup;
	private DrawingAreaPopupMenu2 popup2;
	
	GeneralPath arrow=new GeneralPath();
	private boolean drawSelectedRoad=true;
	/**
	 * recieves a line and draws line along with arrow head 
	 * @param g {@link Graphics2D}
	 * @param line {@link Line2D}
	 * @param c {@link Color}
	 */
	public void drawArrowHead(Graphics2D g,Line2D line,Color c) {
		g.setColor(c);
		double angle=Math.atan2(line.getY2()-line.getY1(), line.getX2()-line.getX1());
		arrow.reset();
		arrow.moveTo(line.getX2()+4*Math.cos(angle), line.getY2()+4*Math.sin(angle));
		arrow.lineTo(line.getX2()+2*Math.sin(angle), line.getY2()-2*Math.cos(angle));
		arrow.lineTo(line.getX2()-2*Math.sin(angle), line.getY2()+2*Math.cos(angle));
		arrow.closePath();
	    g.draw(line);
	    g.fill(arrow);
	    
	    
//	    g.fillOval((int)line.getX2()-3, (int)line.getY2()-3, 6, 6);
	}
	/**
	 * 
	 * @param rdPrPnl rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public DrawingArea(RoadContext rdPrPnl) {
		this.roadPrPnl=rdPrPnl;
		popup=new DrawingAreaPopupMenu(roadPrPnl);
		popup2=new DrawingAreaPopupMenu2(roadPrPnl);
		setSize(new Dimension(bufferWidth,bufferHeight));
		DrawingAreaController controller=new DrawingAreaController(this);
		mouseListener=new DrawingAreaMouseListener(this, controller);
		keyListner = new Shortcuts();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);
		addKeyListener(keyListner);
		setBackground(new Color(74,172,23));
	}
	/**
	 * Draws selected road, geometry and lane
	 * @param g {@link Graphics2D}
	 */
    public void drawSelected(Graphics2D g){
    	if(roadPrPnl.getSelectedRoad()==null)return;
		Stroke dashed = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2+(float)scale}, 0);
		g.setStroke(dashed);
		g.setColor(Color.WHITE.darker());
		if(drawSelectedRoad && !mouseListener.roadInDrag)drawSelectedRoad(g);
    	if(drawSelectedLane && !mouseListener.roadInDrag){
    		Stroke lnStroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1.5f}, 0);
	    	g.setStroke(lnStroke);
	    	g.setColor(Color.BLUE);
	    	drawSelectedLane(g);
    	}
    	if(drawSelectedGeometry && !mouseListener.roadInDrag){
        	Stroke gmStroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1f}, 0);
        	g.setStroke(gmStroke);
        	g.setColor(Color.YELLOW);
        	drawSelectedGeometry(g);
    	}
    }
    /**
     * Draws selected {@link Geometry}
     * @param g {@link Graphics2D}
     */
	public void drawSelectedGeometry(Graphics2D g) {
		int ind=roadPrPnl.getGmPnl().getSelectedIndex();
		if(roadPrPnl.getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
			RoadMappingPoly rmp=(RoadMappingPoly)roadPrPnl.getSelectedRoad().roadMapping();
			g.draw(rmp.getRoadMappings().get(ind).getBounds());
		}else{
			g.draw(roadPrPnl.getSelectedRoad().roadMapping().getBounds());
		}
	}
	/**
	 * resets transform
	 */
	public void setTransform() {
        transform.setToIdentity();
        transform.scale(scale, scale);
        transform.translate(xOffset, yOffset);
    }
	/**
	 * Draws selected road boundary with grey {@link Color}
	 * @param g {@link Graphics2D}
	 */
	public void drawSelectedRoad(Graphics2D g){
		if(roadPrPnl.getSelectedRoad().roadMapping() instanceof RoadMappingPoly){
			RoadMappingPoly rmp=(RoadMappingPoly)roadPrPnl.getSelectedRoad().roadMapping();
			g.draw(rmp.getBounds());
		}else{
			g.draw(roadPrPnl.getSelectedRoad().roadMapping().getBounds());
		}
	}
	/**
	 * Draws Selected roads with junctions editor
	 * @param g {@link Graphics2D}
	 */
	public void drawSelectedJunctionBounds(Graphics2D g){
		Set<RoadSegment>rdShapes =getRoadPrPnl().getAppFrame().getTpnl().getSelectedRoads();
		if(rdShapes.size()>0){
			g.setColor(Color.RED);
			for(RoadSegment s:rdShapes){
				RoadMapping rm=s.roadMapping();
				if(rm instanceof RoadMappingPoly){
					g.draw(((RoadMappingPoly)rm).getBounds());
				}else{
					g.draw(rm.getBounds());
				}
			}
		}
	}
	/**
	 * Draws {@link org.movsim.network.autogen.opendrive.OpenDRIVE.Road} links
	 * @param g {@link Graphics2D}
	 */
	public void drawLinks(Graphics2D g){
		 for (org.movsim.network.autogen.opendrive.OpenDRIVE.Road road : getRoadPrPnl().getRn().getOdrNetwork().getRoad()) {
	            RoadSegment roadSegment = getRoadPrPnl().getRn().findByUserId(road.getId());
	            if (roadSegment == null) {
	            	continue;
//	                throw new IllegalArgumentException("cannot find roadSegment in network for road: " + road.getId());
	            }

	            if (!road.isSetLink()) {
	                continue;
	            }
	            boolean hasPr=false;
	            try{
	            	hasPr=OpenDriveHandlerJaxb.hasRoadPredecessor(road);
	            }catch(NullPointerException e){
	            	continue;
	            }
	            if (hasPr) {
	            	RoadSegment sourceRoadSegment;
	            	try{
	            		sourceRoadSegment = OpenDriveHandlerJaxb.getSourceRoadSegment(getRoadPrPnl().getRn(), road);
	            	}catch(NullPointerException e){
	            		continue;
	            	}
	                for (LaneSection laneSection : road.getLanes().getLaneSection()) {
	                    if (laneSection.isSetCenter()) {
	                        // LOG.warn("cannot handle center lane");
	                        continue;
	                    }
	                    List<org.movsim.network.autogen.opendrive.Lane> lanes = laneSection.isSetLeft() ? laneSection
	                            .getLeft().getLane() : laneSection.getRight().getLane();
	                    for (Lane lane : lanes) {
	                        if (lane.isSetLink() && lane.getLink().isSetPredecessor()) {
	                            int fromLane = OpenDriveHandlerUtils.laneIdToLaneIndex(sourceRoadSegment, lane.getLink()
	                                    .getPredecessor().getId());
	                            int toLane = OpenDriveHandlerUtils.laneIdToLaneIndex(roadSegment, lane.getId());
	                            Point2D p1=RoadNetworkUtils.getEnd(sourceRoadSegment, fromLane);
	                            Point2D p2=RoadNetworkUtils.getStart(roadSegment, toLane);
	                            drawArrow(g, p1, p2,Color.BLACK);
	                            
	                        }
	                    }
	                }
	            }
	            boolean hasSc=false;
	            try{
	            	hasSc=OpenDriveHandlerJaxb.hasRoadSuccessor(road);
	            }catch(NullPointerException e){
	            	continue;
	            }
	            if (hasSc) {
	            	RoadSegment sinkRoadSegment;
	                try{
	                	sinkRoadSegment = OpenDriveHandlerJaxb.getRoadSuccessor(getRoadPrPnl().getRn(), road);
	                }catch(NullPointerException e){
	                	continue;
	                }
	                for (LaneSection laneSection : road.getLanes().getLaneSection()) {
	                    if (laneSection.isSetCenter()) {
	                        continue;
	                    }
	                    List<Lane> lanes = laneSection.isSetLeft() ? laneSection.getLeft().getLane() : laneSection
	                            .getRight().getLane();
	                    for (Lane lane : lanes) {
	                        if (lane.isSetLink() && lane.getLink().isSetSuccessor()) {
	                            int fromLane = OpenDriveHandlerUtils.laneIdToLaneIndex(roadSegment, lane.getId());
	                            int toLane = OpenDriveHandlerUtils.laneIdToLaneIndex(sinkRoadSegment, lane.getLink().getSuccessor()
	                                    .getId());
	                            Point2D p1=RoadNetworkUtils.getEnd(roadSegment, fromLane);
	                            Point2D p2=RoadNetworkUtils.getStart(sinkRoadSegment, toLane);
	                            drawArrow(g, p1, p2,Color.WHITE);
	                        }
	                    }
	                }
	            }
	        }
	}
	/**
	 * draws line with arrow head starting from p1 to p2 with color c
	 * @param g {@link Graphics2D}
	 * @param p1 {@link Point2D}
	 * @param p2 {@link Point2D}
	 * @param c {@link Color}
	 */
    public void drawArrow(Graphics2D g, Point2D p1, Point2D p2,Color c) {
		Line2D line=new Line2D.Double(p1,p2);
		drawArrowHead(g, line,c);
	}
    /**
     * Draws selected lane boundary with blue {@link Color}
     * @param g
     */
	public void drawSelectedLane(Graphics2D g) {
    	g.setColor(Color.BLUE);
    	g.draw(roadPrPnl.getSelectedRoad().getLaneSegments()[roadPrPnl.getLanesPnl().getSelectedIndex()].getBounds());
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
    /**
     * sets scale
     * @param scale Scale
     */
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
        drawSelected(backgroundGraphics);
        if(getRoadPrPnl().getAppFrame().getTpnl()!=null)drawSelectedJunctionBounds(backgroundGraphics);
        g.drawImage(backgroundBuffer, 0, 0, null);
    }
    protected void clearBackground(Graphics2D g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    /**
     * sets background color of the canvas
     * @param backgroundColor
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /**
     * Draws all {@link org.movsim.network.autogen.opendrive.OpenDRIVE.Road}s
     * @param g {@link Graphics2D}
     */
    public void drawRoadSegments(Graphics2D g) {
        for (final RoadSegment roadSegment : roadPrPnl.getRn()) {
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
    /**
     * Draws road lines
     * @param g {@link Graphics2D}
     * @param roadMapping {@link RoadMapping}
     */
    public void drawRoadSegmentLines(Graphics2D g, RoadMapping roadMapping) {
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
    	if(drawSignals)drawTrafficLights(g);
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

        if (drawRoadId || drawRoadNames) {
            drawRoadSectionIds(g);
        }
        if (drawAxis) {
        	drawAxis(g);
        }
        if (drawRoadBounds) {
        	drawRoadBounds(g);
        }
        if (drawLaneBounds) {
        	g.setColor(Color.BLUE);
        	drawLaneBounds(g);
        }
        if(drawLaneLinks)drawLinks(g);
        if(mouseListener.getLinkPoints().size()==1)drawLinkerLine(g);
    }
    /**
     * used to draw indicator line when linker has clicked one road and is waiting for other click 
     * @param g {@link Graphics2D}
     */
	public void drawLinkerLine(Graphics2D g) {
		g.setColor(Color.RED.darker());
		RoadSegment roadSegment=mouseListener.getLinkPoints().get(0).getRs();
		int ls=mouseListener.getLinkPoints().get(0).getLaneId();
		Point2D p=RoadNetworkUtils.getEnd(roadSegment, ls);
		g.drawLine((int)p.getX(), (int)p.getY(), (int)mouseListener.getMousePoint().getX(),(int) mouseListener.getMousePoint().getY());
	}
	/**
	 * used to draw all lanes' bounds
	 * @param g {@link Graphics2D}
	 */
	public void drawLaneBounds(Graphics2D g) {
    	for(RoadSegment rs:roadPrPnl.getRn()){
    		for(LaneSegment ls:rs.getLaneSegments()){
    			g.draw(ls.getBounds());
    		}
    	}
	}
	/**
	 * Used to draw all roads' bounds
	 * @param g {@link Graphics2D}
	 */
	public void drawRoadBounds(Graphics2D g) {
    	for(RoadSegment rs:roadPrPnl.getRn()){
    		g.draw(rs.roadMapping().getBounds());
    	}
	}
	/**
	 * Used to draw axis
	 * @param g {@link Graphics2D}
	 */
	public void drawAxis(Graphics2D g) {
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
	/**
	 * draws traffic lights
	 * @param g {@link Graphics2D}
	 */
	public void drawTrafficLights(Graphics2D g) {
        for (final RoadSegment roadSegment : roadPrPnl.getRn()) {
            drawTrafficLightsOnRoad(g, roadSegment,roadPrPnl);
        }
    }
	/**
	 * 
	 * @param roadMapping {@link RoadMapping}
	 * @param trafficLightLocation {@link TrafficLightLocation}
	 * @return {@link Rectangle2D} bounding signal
	 */
    public static Rectangle2D trafficLightRect(RoadMapping roadMapping, TrafficLightLocation trafficLightLocation) {
        final double offset = (roadMapping.laneCount() / 2.0 + 1.5) * roadMapping.laneWidth();
        final double size = 2 * roadMapping.laneWidth();
        final RoadMapping.PosTheta posTheta = roadMapping.map(trafficLightLocation.position(), offset);
        final Rectangle2D rect = new Rectangle2D.Double(posTheta.x - size / 2, posTheta.y - size / 2, size, size
                * trafficLightLocation.getTrafficLight().lightCount());
        return rect;
    }
    private static void drawTrafficLightsOnRoad(Graphics2D g, RoadSegment roadSegment,RoadContext rdCxt) {
        if (roadSegment.trafficLightLocations() == null) 
            return;
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
    	try{
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
    	}catch(NullPointerException e){
    	}

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
    /**
     * draws speed limits
     * @param g
     */
    public void drawSpeedLimits(Graphics2D g) {
        for (final RoadSegment roadSegment : roadPrPnl.getRn()) {
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
        for (final RoadSegment roadSegment : roadPrPnl.getRn()) {
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
    /**
     * draws ids/names of the {@link org.movsim.network.autogen.opendrive.OpenDRIVE.Road}s
     * @param g {@link Graphics2D}
     */
    public void drawRoadSectionIds(Graphics2D g) {
        for (final RoadSegment roadSegment : roadPrPnl.getRn()) {
            final RoadMapping roadMapping = roadSegment.roadMapping();
            final RoadMapping.PosTheta posTheta = roadMapping.map(0.0);
            final int fontHeight = 12;
            final Font font = new Font("SansSerif", Font.PLAIN, fontHeight); //$NON-NLS-1$
            g.setFont(font);
            g.setColor(Color.DARK_GRAY);
            if(drawRoadNames)g.drawString(roadSegment.getRoadName(), (int) (posTheta.x+20*posTheta.cosTheta), (int) (posTheta.y-20*posTheta.sinTheta)); //$NON-NLS-1$
            else g.drawString(roadSegment.userId(), (int) (posTheta.x+20*posTheta.cosTheta), (int) (posTheta.y-20*posTheta.sinTheta)); //$NON-NLS-1$//$NON-NLS-1$
        }
    }
    /**
     * Draws {@link TrafficSource}s
     * @param g {@link Graphics2D}
     */
    public void drawSources(Graphics2D g) {
        for (final RoadSegment roadSegment : roadPrPnl.getRn()) {
            final RoadMapping roadMapping = roadSegment.roadMapping();
            assert roadMapping != null;
            final int radius = (int) ((roadMapping.laneCount() + 2) * roadMapping.laneWidth());
            RoadMapping.PosTheta posTheta;
            if(roadPrPnl.getMvCxt()!=null){
            	for(Road r:roadPrPnl.getMvCxt().getMovsim().getScenario().getSimulation().getRoad()){
            		if(r.getId().equals(roadSegment.userId()) && r.isSetTrafficSource()){
            			g.setColor(sourceColor);
            			posTheta = roadMapping.startPos();
            			g.fillOval((int) posTheta.x - radius / 2, (int) posTheta.y - radius / 2, radius, radius);
            		}
            	}
            }
        }
    }
    /**
     * draws {@link org.movsim.autogen.TrafficSink}s
     * @param g {@link Graphics2D}
     */
    public void drawSinks(Graphics2D g) {
        for (final RoadSegment roadSegment : roadPrPnl.getRn()) {
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
	public void setDrawRoadId(boolean drawRoadId) {
		this.drawRoadId = drawRoadId;
	}
	public void setDrawRoadNames(boolean drawRoadNames) {
		this.drawRoadNames = drawRoadNames;
	}
	public void setDrawAxis(boolean drawAxis) {
		this.drawAxis = drawAxis;
	}
	public RoadContext getRoadPrPnl() {
		return roadPrPnl;
	}
	public DrawingAreaPopupMenu getPopup() {
		return popup;
	}
	public DrawingAreaPopupMenu2 getPopup2() {
		return popup2;
	}
}
