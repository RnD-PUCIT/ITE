package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.prefs.Preferences;

import javax.swing.Icon;
import javax.swing.ImageIcon;
/**
 * This class uses singleton pattern and holds all information/resources to be used in different locations
 * @author Shmeel
 */
public class TDEResources {
	Preferences pref=Preferences.userRoot().node(this.getClass().getName());
	private static TDEResources ir=null;
	private static boolean useTheme;
	public static void setUseTheme(boolean useTheme) {
		TDEResources.useTheme = useTheme;
	}
	public void setColorDensity(int colorDensity) {
		if(!useTheme){
			COLOR1=COLOR2=new Color(240,240,240);
			return;
		}
		pref.putInt("colorDensity", colorDensity);
		COLOR1=new Color(themeColor.getRGB());
		if(colorDensity>0){
			for(int i=0;i<colorDensity;i++)
				COLOR1=COLOR1.darker();
		}
		if(colorDensity<0){
			for(int i=colorDensity;i<0;i++)
				COLOR1=COLOR1.brighter();
		}
		COLOR2=COLOR1.darker();
	}
	private Cursor linkCursor;
	final public Cursor DEFAULT_CURSOR=new Cursor(Cursor.DEFAULT_CURSOR);
	final public Cursor DEFAULT_RED_CURSOR;
	final public Cursor STRAIGHT_ROAD_CURSOR;
	final public Cursor ARC_ROAD_CURSOR;
	final public Cursor TRAFFIC_SOURCE_CURSOR;
	final public Cursor LINK_CURSOR;
	final public Image JUNCTION_DEMO;
	final public Image APP_ICON;
	final public Cursor HAND_CURSOR=new Cursor(Cursor.HAND_CURSOR);
	public Color COLOR1=new Color(245,245,245),COLOR2=new Color(220,220,220);
	private TDEResources(){
		useTheme=pref.getBoolean("useTheme", false);
		Dimension d=Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
		Point p=new Point();
		p.setLocation(d.width/2.0, d.height/2.0);
		Point p2=new Point();
		p2.setLocation(d.width/2.0, d.height-1);
		LINK_CURSOR=Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("linker_icon2.png")), p2, "linkCursor");
		DEFAULT_RED_CURSOR=Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("defaultred_cursor.png")), p, "defaultRedCusor");
		STRAIGHT_ROAD_CURSOR=Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("straight_road2.png")), p, "straightRoadCursor");
		ARC_ROAD_CURSOR=Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("arc_road2.png")), p, "arcRoadCursor");
		TRAFFIC_SOURCE_CURSOR=Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("traffic_source2.png")), p, "trafficSourceCursor");
		
		
		sim=new ImageIcon(getClass().getClassLoader().getResource("simulation_icon.png"));
		prt=new ImageIcon(getClass().getClassLoader().getResource("car_icon.png"));
		trf=new ImageIcon(getClass().getClassLoader().getResource("trafficlight_icon.png"));
		rts=new ImageIcon(getClass().getClassLoader().getResource("routes_icon.png"));
		
		rem=new ImageIcon(getClass().getClassLoader().getResource("del.png"));
		addIcon=new ImageIcon(getClass().getClassLoader().getResource("add.png"));
		run=new ImageIcon(getClass().getClassLoader().getResource("run.png"));
		open=new ImageIcon(getClass().getClassLoader().getResource("open.png"));
		save=new ImageIcon(getClass().getClassLoader().getResource("save.png"));
		output=new ImageIcon(getClass().getClassLoader().getResource("output_icon.png"));
		reset=new ImageIcon(getClass().getClassLoader().getResource("reset.png"));
		straightRoad=new ImageIcon(getClass().getClassLoader().getResource("straight_road.png"));
		arcRoad=new ImageIcon(getClass().getClassLoader().getResource("arc_road.png"));
		trafficSource=new ImageIcon(getClass().getClassLoader().getResource("traffic_source.png"));
		junctions=new ImageIcon(getClass().getClassLoader().getResource("Junc_icon.png"));
		linker=new ImageIcon(getClass().getClassLoader().getResource("linker.png"));
		controller=new ImageIcon(getClass().getClassLoader().getResource("controller.png"));
		colorChooser=new ImageIcon(getClass().getClassLoader().getResource("color_chooser.png"));
		email=new ImageIcon(getClass().getClassLoader().getResource("email.png"));
		
		
		JUNCTION_DEMO=Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("junction_demo.png"));
		APP_ICON=Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("app_icon.png"));
		
		run.setImage(run.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		output.setImage(output.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		open.setImage(open.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		save.setImage(save.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		rem.setImage(rem.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		addIcon.setImage(addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		reset.setImage(reset.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		straightRoad.setImage(straightRoad.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		arcRoad.setImage(arcRoad.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		trafficSource.setImage(trafficSource.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		junctions.setImage(junctions.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		linker.setImage(linker.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		controller.setImage(controller.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		colorChooser.setImage(colorChooser.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		sim.setImage(sim.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		prt.setImage(prt.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		trf.setImage(trf.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		rts.setImage(rts.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		email.setImage(email.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		themeColor=new Color(pref.getInt("themeColor", new Color(233,233,233).getRGB()));
		setColorDensity(pref.getInt("colorDensity", 0));
	}
	public static ImageIcon getEmail() {
		return email;
	}
	public static TDEResources getResources(){
		if(ir==null)ir=new TDEResources();
		return ir;
	}
	public static final Color TRAFFIC_COMP_BORDER_COLOR=new Color(50,50,50);
	public static final Color TRAFFIC_COMP_BORDER_FONT_COLOR=new Color(50,50,50);
	public static final Color TRAFFIC_SRC_BORDER_COLOR=new Color(60,60,60);
	public static final Color TRAFFIC_SRC_BORDER_FONT_COLOR=new Color(60,60,60);
	public static final Color INFLOW_BORDER_COLOR=new Color(120,120,120);
	public static final Color INFLOW_BORDER_FONT_COLOR=new Color(120,120,120);
	public static final Color VEHICLE_TYPE_BORDER_COLOR=new Color(100,100,100);
	public static final Color VEHICLE_TYPE_BORDER_FONT_COLOR=new Color(100,100,100);
	public static final Color ROAD_BORDER_COLOR=new Color(0,0,0);
	public static final Color ROAD_BORDER_FONT_COLOR=new Color(0,0,0);
	public static final Color ROUTE_BORDER_COLOR=new Color(60,60,60);
	public static final Color ROUTE_BORDER_FONT_COLOR=new Color(60,60,60);
	public static final Color SIGNALS_BORDER_COLOR=new Color(150,150,150);
	public static final Color CONTROLLERS_BORDER_COLOR=new Color(150,150,150);
	private static ImageIcon rem;
	private static ImageIcon straightRoad;
	private static ImageIcon addIcon;
	private static ImageIcon open;
	private static ImageIcon save;
	private static ImageIcon run;
	private static ImageIcon output;
	private static ImageIcon reset;
	private static ImageIcon arcRoad;
	private static ImageIcon trafficSource;
	private static ImageIcon junctions;
	private static ImageIcon linker;
	private static ImageIcon controller;
	private static ImageIcon sim;
	private static ImageIcon prt;
	private static ImageIcon trf;
	private static ImageIcon rts;
	private static ImageIcon email;
	private static ImageIcon colorChooser;
	private Color themeColor;
	public Color getThemeColor() {
		return themeColor;
	}
	public void setThemeColor(Color themeColor) {
		this.themeColor = themeColor;
		pref.putInt("themeColor", themeColor.getRGB());
	}
	public void resetThemeColor() {
		this.themeColor =Color.WHITE;
	}
	public static ImageIcon getColorChooser() {
		return colorChooser;
	}
	public ImageIcon getRem() {
		return rem;
	}
	public ImageIcon getArcRoad() {
		return arcRoad;
	}
	public ImageIcon getReset() {
		return reset;
	}
	public ImageIcon getStraightRoad() {
		return straightRoad;
	}
	public ImageIcon getAddIcon() {
		return addIcon;
	}
	public ImageIcon getOpen() {
		return open;
	}
	public ImageIcon getSave() {
		return save;
	}
	public ImageIcon getRun() {
		return run;
	}
	public ImageIcon getOutput() {
		return output;
	}
	public ImageIcon getTrafficSource() {
		return trafficSource;
	}
	public ImageIcon getJunctions() {
		return junctions;
	}
	public Icon getLinker() {
		return linker;
	}
	public Icon getController() {
		return controller;
	}
	public Cursor getLinkCursor() {
		return linkCursor;
	}
	public ImageIcon getSimulation() {
		return sim;
	}
	public ImageIcon getPrototypes() {
		return prt;
	}
	public ImageIcon getTraffic() {
		return trf;
	}
	public ImageIcon getRoutes() {
		return rts;
	}
}
