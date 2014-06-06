package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class TDEResources {
	private static TDEResources ir=null;
	private TDEResources(){
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
		junctions=new ImageIcon(getClass().getClassLoader().getResource("junctions_tab_icon.png"));
		
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
	}
	public static TDEResources getResources(){
		if(ir==null)ir=new TDEResources();
		return ir;
	}
//	public static final Color TRAFFIC_COMP_BORDER_COLOR=new Color(0,204,214);
//	public static final Color TRAFFIC_COMP_BORDER_FONT_COLOR=new Color(0,204,214);
//	public static final Color TRAFFIC_SRC_BORDER_COLOR=new Color(242,103,31);
//	public static final Color TRAFFIC_SRC_BORDER_FONT_COLOR=new Color(242,103,31);
//	public static final Color INFLOW_BORDER_COLOR=new Color(96,4,122);
//	public static final Color INFLOW_BORDER_FONT_COLOR=new Color(96,4,122);
//	public static final Color VEHICLE_TYPE_BORDER_COLOR=new Color(177,135,0);
//	public static final Color VEHICLE_TYPE_BORDER_FONT_COLOR=new Color(177,135,0);
//	public static final Color ROAD_BORDER_COLOR=new Color(177,63,107);
//	public static final Color ROAD_BORDER_FONT_COLOR=new Color(177,63,107);
//	public static final Color ROUTE_BORDER_COLOR=new Color(96,4,122);
//	public static final Color ROUTE_BORDER_FONT_COLOR=new Color(96,4,122);
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
	public static ImageIcon getRem() {
		return rem;
	}
	public static ImageIcon getArcRoad() {
		return arcRoad;
	}
	public static ImageIcon getReset() {
		return reset;
	}
	public static ImageIcon getStraightRoad() {
		return straightRoad;
	}
	public static ImageIcon getAddIcon() {
		return addIcon;
	}
	public static ImageIcon getOpen() {
		return open;
	}
	public static ImageIcon getSave() {
		return save;
	}
	public static ImageIcon getRun() {
		return run;
	}
	public static ImageIcon getOutput() {
		return output;
	}
	public static ImageIcon getTrafficSource() {
		return trafficSource;
	}
	public static ImageIcon getJunctions() {
		return junctions;
	}
}
