package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

public class AppFrame extends JFrame {
	private static final long serialVersionUID = 14320973455L;
	private RoadContext rdCxt;
	private StatusPanel statusPnl;
	private JunctionsPanel jp;
	private RoadNetwork rn;
	private ToolBar toolbar;
	private JunctionsListener jl;
	private MovsimConfigContext mvCxt;
	JMenuBar menuBar;
	ToolsPanel tpnl;
	public RoadContext getrdCxt(){
		return rdCxt;
	}
	public AppFrame() {
		setPreferredSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(700, 500));
		setTitle("Vehicular Traffic  Flow Scenario Development Environment");
		
		
		menuBar = new JMenuBar();
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
		
		JMenuItem mntmChangeToastDelay = new JMenuItem("Change toast delay");
		mnEdit.add(mntmChangeToastDelay);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		JTabbedPane tabPane=new JTabbedPane();
		ImageIcon icon=new ImageIcon(getClass().getClassLoader().getResource("road_icon.png"));
		icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		ImageIcon icon2=new ImageIcon(getClass().getClassLoader().getResource("Junctions_tab_icon.png"));
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
		toolbar.setBlocked(false);
		tabPane.addTab("Road properties", icon, rdCxt.getSp(), "Editing panel for currently selected road");
		tabPane.addTab("Junctions Editor", icon2,jp.getSp() , "Editiong panel for junctions");
		getContentPane().add(tabPane, BorderLayout.EAST);
		statusPnl=new StatusPanel();
		statusPnl.setStatus("Status");
//		getContentPane().add(statusPnl, BorderLayout.SOUTH);
		ms.setStatusPnl(statusPnl);
		getContentPane().add(toolbar,BorderLayout.NORTH);
		
		mvCxt=new MovsimConfigContext(MovsimScenario.getMovsim(),rdCxt);
		rdCxt.setMvCxt(mvCxt);
		toolbar.setMvCxt(mvCxt);
		mvCxt.setUpdateCanvas(true);
		AppFrameListener appListener=new  AppFrameListener(mvCxt);
		appListener.setOpen(mntmOpen);
		appListener.setSave(mntmSave);
		appListener.setReset(mntmReset);
		appListener.setRun(mntmRun);
		mntmOpen.addActionListener(appListener);
		mntmRun.addActionListener(appListener);
		mntmSave.addActionListener(appListener);
		mntmReset.addActionListener(appListener);
		tpnl=new ToolsPanel(mvCxt);
		getContentPane().add(tpnl, BorderLayout.WEST);
		JPanel southPanel=new JPanel(new BorderLayout());
		southPanel.add(mvCxt,BorderLayout.CENTER);
		southPanel.add(statusPnl,BorderLayout.SOUTH);
		getContentPane().add(southPanel,BorderLayout.SOUTH);
		appListener.setBlocked(false);
		GraphicsHelper.finalizeFrame(this);
	}
	public StatusPanel getStatusPnl() {
		return statusPnl;
	}
	public void setStatusPnl(StatusPanel statusPnl) {
		this.statusPnl = statusPnl;
	}
	public JunctionsPanel getJp() {
		return jp;
	}
	public ToolBar getToolbar() {
		return toolbar;
	}
	public JunctionsListener getJl() {
		return jl;
	}
	public JMenuBar getMenuBar2() {
		return menuBar;
	}
	public ToolsPanel getTpnl() {
		return tpnl;
	}
}
