package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.tde.tdescenariodeveloper.eventhandling.AppFrameListener;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaMouseListener;
import org.tde.tdescenariodeveloper.eventhandling.JunctionsListener;
import org.tde.tdescenariodeveloper.eventhandling.Shortcuts;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
/**
 * Main application UI Frame
 * @author Shmeel
 *
 */
public class AppFrame extends JFrame {
	JCheckBoxMenuItem name,id,axis,dropRoadAtLast,showSelectedGeometry,showSelectedLane,showSpeedLimits,showLinks,showSignals; //,useTheme;
	private static final long serialVersionUID = 14320973455L;
	private RoadContext rdCxt;
	private StatusPanel statusPnl;
	private JunctionsPanel jp;
	private RoadNetwork rn;
	private ToolBar toolbar;
	private JunctionsListener jl;
	private MovsimConfigContext mvCxt;
	JMenuBar menuBar;
	org.movsim.viewer.ui.AppFrame movsimFrame;
	private static AppFrame thisAppFrame;
	
	public org.movsim.viewer.ui.AppFrame getMovsimFrame() {
		return movsimFrame;
	}
	public void setMovsimFrame(org.movsim.viewer.ui.AppFrame movsimFrame) {
		this.movsimFrame = movsimFrame;
	}
	ToolsPanel tpnl;
	/**
	 * 
	 * @return return {@link RoadContext}
	 */
	
	public static AppFrame getAppFrame(){
		return thisAppFrame;
	}
	
	public RoadContext getrdCxt(){
		return rdCxt;
	}
	public AppFrame() {
		GraphicsHelper.setNativeUI();
		setPreferredSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(700, 500));
		setTitle("Vehicular Traffic Scenario Development Environment");
		setIconImage(TDEResources.getResources().APP_ICON);
		name=new JCheckBoxMenuItem("Show road names");
		id=new JCheckBoxMenuItem("Show road id's");
		axis=new JCheckBoxMenuItem("Show axis");
		showSelectedGeometry=new JCheckBoxMenuItem("Show selected geometry");
		showSelectedGeometry.setToolTipText("Yellow boundry line shows currently selected geometry/road segment.");
		showSelectedLane=new JCheckBoxMenuItem("Show lane boundry");
		showSpeedLimits=new JCheckBoxMenuItem("Show speed limits");
		showLinks=new JCheckBoxMenuItem("Show road links");
		showSignals=new JCheckBoxMenuItem("Show signals");
		//useTheme=new JCheckBoxMenuItem("Use color theme");
		dropRoadAtLast=new JCheckBoxMenuItem("Auto locate new road");
		dropRoadAtLast.setToolTipText("Drop new road at end of the last road in network");
		
		id.setSelected(true);
		axis.setSelected(true);
		showLinks.setSelected(true);
		showSignals.setSelected(true);
		//useTheme.setSelected(Preferences.userRoot().node(TDEResources.class.getName()).getBoolean("useTheme",false));
		showSelectedLane.setSelected(true);
		showSpeedLimits.setSelected(true);
		showSelectedGeometry.setSelected(true);
		dropRoadAtLast.setSelected(false);
		
		
		
		menuBar = new TDEMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open",TDEResources.getResources().getOpen());
		mnFile.add(mntmOpen);
		
		JMenuItem mntmRun = new JMenuItem("Run",TDEResources.getResources().getRun());
		mnFile.add(mntmRun);
		
		JMenuItem mntmSave = new JMenuItem("save",TDEResources.getResources().getSave());
		mnFile.add(mntmSave);
		
		JMenuItem mntmReset = new JMenuItem("Reset",TDEResources.getResources().getReset());
		mnFile.add(mntmReset);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmChangeToastDelay = new JMenuItem("Change toast delay",TDEResources.getResources().getSimulation());
		mnEdit.add(mntmChangeToastDelay);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About",TDEResources.getResources().getLinker());
		mnHelp.add(mntmAbout);

		JMenuItem mntmEmail= new JMenuItem("Request tutorials",TDEResources.getResources().getEmail());
		mnHelp.add(mntmEmail);
		//TODO: This is the culprit side tabPane which provides functions of Road and Junction
		JTabbedPane tabPane=new JTabbedPane();
		ImageIcon icon=new ImageIcon(getClass().getClassLoader().getResource("road_icon.png"));
		icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		ImageIcon icon2=new ImageIcon(getClass().getClassLoader().getResource("Junc_icon.png"));
		icon2.setImage(icon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		rn=new RoadNetwork();
		new OpenDriveHandlerJaxb().create("", RoadNetworkUtils.getNewOdr(), rn);
		rdCxt= new RoadContext(rn,this);
		jl=new JunctionsListener(rdCxt);
		jp=new JunctionsPanel(rdCxt,jl);
		if(rdCxt!=null && rdCxt.getRn().getOdrNetwork()!=null && rdCxt.getRn().getOdrNetwork().getJunction()!=null && rdCxt.getRn().getOdrNetwork().getJunction().size()>0)jp.updateJunction();
		jl.setBlocked(false);
		DrawingArea drawingArea = new DrawingArea(rdCxt);
		rdCxt.setDrawingArea(drawingArea);
		DrawingAreaMouseListener ms=(DrawingAreaMouseListener)drawingArea.getMouseMotionListeners()[0];
		rdCxt.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "Road Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(drawingArea, BorderLayout.CENTER);
		
		toolbar=new ToolBar(drawingArea);
		

		mnEdit.add(name);
		mnEdit.add(id);
		mnEdit.add(showSelectedGeometry);
		mnEdit.add(showSelectedLane);
		mnEdit.add(showSpeedLimits);
		mnEdit.add(showLinks);
		mnEdit.add(showSignals);
		mnEdit.add(axis);
		mnEdit.add(dropRoadAtLast);
		//mnEdit.add(useTheme);
		
		
		
		toolbar.setBlocked(false);
		tabPane.setPreferredSize(new Dimension(275,700));
		tabPane.addTab("Road", icon, rdCxt.getSp(), "Editing panel for currently selected road");
		tabPane.addTab("Junctions", icon2,jp.getSp() , "Editiong panel for junctions");
		getContentPane().add(tabPane, BorderLayout.EAST);
		statusPnl=new StatusPanel();
		statusPnl.setStatus("Status");
		ms.setStatusPnl(statusPnl);
		getContentPane().add(toolbar,BorderLayout.NORTH);
		
		mvCxt=new MovsimConfigContext(MovsimScenario.getMovsim(),rdCxt);
		rdCxt.setMvCxt(mvCxt);
		toolbar.setMvCxt(mvCxt);
		Shortcuts.setMvCxt(mvCxt);
		AppFrameListener appListener=new  AppFrameListener(mvCxt);
		name.addActionListener(appListener);
		id.addActionListener(appListener);
		showSelectedGeometry.addActionListener(appListener);
		showSelectedLane.addActionListener(appListener);
		showSpeedLimits.addActionListener(appListener);
		showLinks.addActionListener(appListener);
		showSignals.addActionListener(appListener);
		//useTheme.addActionListener(appListener);
		axis.addActionListener(appListener);
		dropRoadAtLast.addActionListener(appListener);

		appListener.setName(name);
		appListener.setId(id);
		appListener.setShowSelectedGeometry(showSelectedGeometry);
		appListener.setShowSelectedLane(showSelectedLane);
		appListener.setShowSpeedLimits(showSpeedLimits);
		appListener.setShowLinks(showLinks);
		appListener.setShowSignals(showSignals);
		//appListener.setUseTheme(useTheme);
		appListener.setAxis(axis);
		appListener.setDropRoadAtLast(dropRoadAtLast);
		
		
		appListener.setOpen(mntmOpen);
		appListener.setSave(mntmSave);
		appListener.setReset(mntmReset);
		appListener.setChangeToastDelay(mntmChangeToastDelay);
		appListener.setRun(mntmRun);
		appListener.setAbout(mntmAbout);
		appListener.setEmail(mntmEmail);
		mntmOpen.addActionListener(appListener);
		mntmRun.addActionListener(appListener);
		mntmSave.addActionListener(appListener);
		mntmReset.addActionListener(appListener);
		mntmChangeToastDelay.addActionListener(appListener);
		mntmAbout.addActionListener(appListener);
		mntmEmail.addActionListener(appListener);
		tpnl=new ToolsPanel(mvCxt);
		getContentPane().add(tpnl, BorderLayout.WEST);
		JPanel southPanel=new JPanel(new BorderLayout());
		southPanel.add(mvCxt,BorderLayout.CENTER);
		southPanel.add(statusPnl,BorderLayout.SOUTH);
		getContentPane().add(southPanel,BorderLayout.SOUTH);
		appListener.setBlocked(false);
		GraphicsHelper.finalizeFrame(this);
		thisAppFrame = this;
	}
	public JCheckBoxMenuItem getDropRoadAtLast() {
		return dropRoadAtLast;
	}
	public StatusPanel getStatusPnl() {
		return statusPnl;
	}
	public void setStatusPnl(StatusPanel statusPnl) {
		this.statusPnl = statusPnl;
	}
	/**
	 * 
	 * @return return {@link JunctionsPanel}
	 */
	public JunctionsPanel getJp() {
		return jp;
	}
	public ToolBar getToolbar() {
		return toolbar;
	}
	/**
	 * 
	 * @return returns {@link JunctionsListener}
	 */
	public JunctionsListener getJl() {
		return jl;
	}
	public JMenuBar getMenuBar2() {
		return menuBar;
	}
	/**
	 * 
	 * @return returns {@link ToolsPanel}
	 */
	public ToolsPanel getTpnl() {
		return tpnl;
	}
}
