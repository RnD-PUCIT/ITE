package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class TDEResources {
	private static TDEResources ir=null;
	private TDEResources(){
		rem=new ImageIcon(getClass().getClassLoader().getResource("del.png"));
		addIcon=new ImageIcon(getClass().getClassLoader().getResource("add.png"));
		run=new ImageIcon(getClass().getClassLoader().getResource("run.png"));
		open=new ImageIcon(getClass().getClassLoader().getResource("open.png"));
		save=new ImageIcon(getClass().getClassLoader().getResource("save.png"));
		
		run.setImage(run.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		open.setImage(open.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		save.setImage(save.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		rem.setImage(rem.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		addIcon.setImage(addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
	}
	public static TDEResources getResources(){
		if(ir!=null)
			return ir;
		else return new TDEResources();
	}
	public static final Color TRAFFIC_COMP_BORDER_COLOR=new Color(0,204,214);
	public static final Color TRAFFIC_COMP_BORDER_FONT_COLOR=new Color(0,204,214);
	public static final Color TRAFFIC_SRC_BORDER_COLOR=new Color(242,103,31);
	public static final Color TRAFFIC_SRC_BORDER_FONT_COLOR=new Color(242,103,31);
	public static final Color INFLOW_BORDER_COLOR=new Color(96,4,122);
	public static final Color INFLOW_BORDER_FONT_COLOR=new Color(96,4,122);
	public static final Color VEHICLE_TYPE_BORDER_COLOR=new Color(177,135,0);
	public static final Color VEHICLE_TYPE_BORDER_FONT_COLOR=new Color(177,135,0);
	public static final Color ROAD_BORDER_COLOR=new Color(177,63,107);
	public static final Color ROAD_BORDER_FONT_COLOR=new Color(177,63,107);
	public static final Border BORDER_SPACE=new EmptyBorder(15,3,15,3);
	private static ImageIcon rem;
	private static ImageIcon addIcon;
	private static ImageIcon open;
	private static ImageIcon save;
	private static ImageIcon run;
	public static ImageIcon getRem() {
		return rem;
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
}
